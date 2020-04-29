/*
 * scaffold-framework-core - smallbun企业级开发脚手架-核心框架
 * Copyright © 2018-2020 SanLi (qinggang.zuo@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cn.smallbun.scaffold.framework.mybatis.interceptor;

import cn.smallbun.scaffold.framework.mybatis.page.PageModel;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisDefaultParameterHandler;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectFactory;
import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.baomidou.mybatisplus.extension.toolkit.SqlParserUtils;
import com.google.common.collect.Lists;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分页拦截器
 * Copy 最新版本的官方拦截器实现，然后处理 PageModule
 *
 * @author SanLi
 */
@Setter
@Accessors(chain = true)
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,
                                                                                     Integer.class }) })
public class PaginationInterceptor extends AbstractSqlParserHandler implements Interceptor {

    protected static final Log logger   = LogFactory.getLog(PaginationInterceptor.class);
    /**
     * COUNT SQL 解析
     */
    protected ISqlParser       countSqlParser;
    /**
     * 溢出总页数后是否进行处理
     */
    protected boolean          overflow = false;
    /**
     * 单页限制 500 条，小于 0 如 -1 不受限制
     */
    protected long             limit    = 500L;
    /**
     * 数据库类型
     *
     * @since 3.3.1
     */
    private DbType             dbType;
    /**
     * 方言实现类
     *
     * @since 3.3.1
     */
    private IDialect           dialect;
    /**
     * 方言类型(数据库名,全小写) <br>
     * 如果用的我们支持分页的数据库但获取数据库类型不正确则可以配置该值进行校正
     * @deprecated {@link #setDbType(DbType)}
     */
    @Deprecated
    protected String           dialectType;
    /**
     * 方言实现类<br>
     * 注意！实现 com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect 接口的子类
     * @deprecated 3.3.1 {@link #setDialect(IDialect)}
     */
    @Deprecated
    protected String           dialectClazz;

    /**
     * 查询SQL拼接Order By
     *
     * @param originalSql 需要拼接的SQL
     * @param page        page对象
     * @return ignore
     */
    public static String concatOrderBy(String originalSql, IPage<?> page) {
        if (CollectionUtils.isNotEmpty(page.orders())) {
            try {
                List<OrderItem> orderList = page.orders();
                Select selectStatement = (Select) CCJSqlParserUtil.parse(originalSql);
                if (selectStatement.getSelectBody() instanceof PlainSelect) {
                    PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
                    List<OrderByElement> orderByElements = plainSelect.getOrderByElements();
                    List<OrderByElement> orderByElementsReturn = addOrderByElements(orderList,
                        orderByElements);
                    plainSelect.setOrderByElements(orderByElementsReturn);
                    return plainSelect.toString();
                } else if (selectStatement.getSelectBody() instanceof SetOperationList) {
                    SetOperationList setOperationList = (SetOperationList) selectStatement
                        .getSelectBody();
                    List<OrderByElement> orderByElements = setOperationList.getOrderByElements();
                    List<OrderByElement> orderByElementsReturn = addOrderByElements(orderList,
                        orderByElements);
                    setOperationList.setOrderByElements(orderByElementsReturn);
                    return setOperationList.toString();
                } else if (selectStatement.getSelectBody() instanceof WithItem) {
                    // todo: don't known how to resole
                    return originalSql;
                } else {
                    return originalSql;
                }

            } catch (JSQLParserException e) {
                logger.warn("failed to concat orderBy from IPage, exception=" + e.getMessage());
            }
        }
        return originalSql;
    }

    private static List<OrderByElement> addOrderByElements(List<OrderItem> orderList,
                                                           List<OrderByElement> orderByElements) {
        orderByElements = CollectionUtils.isEmpty(orderByElements)
            ? new ArrayList<>(orderList.size())
            : orderByElements;
        List<OrderByElement> orderByElementList = orderList.stream()
            .filter(item -> StringUtils.isNotBlank(item.getColumn())).map(item -> {
                OrderByElement element = new OrderByElement();
                element.setExpression(new Column(item.getColumn()));
                element.setAsc(item.isAsc());
                element.setAscDescPresent(true);
                return element;
            }).collect(Collectors.toList());
        orderByElements.addAll(orderByElementList);
        return orderByElements;
    }

    /**
     * Physical Page Interceptor for all the queries with parameter {@link RowBounds}
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

        // SQL 解析
        this.sqlParser(metaObject);

        // 先判断是不是SELECT操作  (2019-04-10 00:37:31 跳过存储过程)
        MappedStatement mappedStatement = (MappedStatement) metaObject
            .getValue("delegate.mappedStatement");
        if (SqlCommandType.SELECT != mappedStatement.getSqlCommandType()
            || StatementType.CALLABLE == mappedStatement.getStatementType()) {
            return invocation.proceed();
        }

        // 针对定义了rowBounds，做为mapper接口方法的参数
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object paramObj = boundSql.getParameterObject();

        // 判断参数里是否有page对象
        IPage<?> page = null;
        if (paramObj instanceof IPage) {
            page = (IPage<?>) paramObj;
        }
        //TODO 特殊处理过后的判断参数里是否有PageModel对象
        else if (paramObj instanceof PageModel) {
            page = getPage((PageModel) paramObj);
        } else if (paramObj instanceof Map) {
            for (Object arg : ((Map<?, ?>) paramObj).values()) {
                if (arg instanceof IPage) {
                    page = (IPage<?>) arg;
                    break;
                }
            }
        }

        /*
         * 不需要分页的场合，如果 size 小于 0 返回结果集
         */
        if (null == page || page.getSize() < 0) {
            return invocation.proceed();
        }

        if (this.limit > 0 && this.limit <= page.getSize()) {
            //处理单页条数限制
            handlerLimit(page);
        }

        String originalSql = boundSql.getSql();
        Connection connection = (Connection) invocation.getArgs()[0];

        if (page.isSearchCount() && !page.isHitCount()) {
            SqlInfo sqlInfo = SqlParserUtils.getOptimizeCountSql(page.optimizeCountSql(),
                countSqlParser, originalSql);
            this.queryTotal(sqlInfo.getSql(), mappedStatement, boundSql, page, connection);
            if (page.getTotal() <= 0) {
                return null;
            }
        }
        DbType dbType = Optional.ofNullable(this.dbType)
            .orElse(JdbcUtils.getDbType(connection.getMetaData().getURL()));
        IDialect dialect = Optional.ofNullable(this.dialect)
            .orElse(DialectFactory.getDialect(dbType));
        String buildSql = concatOrderBy(originalSql, page);
        DialectModel model = dialect.buildPaginationSql(buildSql, page.offset(), page.getSize());
        Configuration configuration = mappedStatement.getConfiguration();
        List<ParameterMapping> mappings = new ArrayList<>(boundSql.getParameterMappings());
        Map<String, Object> additionalParameters = (Map<String, Object>) metaObject
            .getValue("delegate.boundSql.additionalParameters");
        model.consumers(mappings, configuration, additionalParameters);
        metaObject.setValue("delegate.boundSql.sql", model.getDialectSql());
        metaObject.setValue("delegate.boundSql.parameterMappings", mappings);
        return invocation.proceed();
    }

    /**
     * 获取page
     * @param arg {@link PageModel}
     * @return {@link IPage}
     */
    private IPage<?> getPage(PageModel arg) {
        IPage<?> page;
        Page<?> modelPage = new Page<>(arg.getCurrent(), arg.getPageSize());
        List<OrderItem> orders = Lists.newArrayList();
        orders.add(new OrderItem()
            // 是否asc
            .setAsc(arg.isAsc())
            // 排序列
            .setColumn(arg.getSorter()));
        modelPage.setOrders(orders);
        page = modelPage;
        return page;
    }

    /**
     * 处理超出分页条数限制,默认归为限制数
     *
     * @param page IPage
     */
    protected void handlerLimit(IPage<?> page) {
        page.setSize(this.limit);
    }

    /**
     * 查询总记录条数
     *
     * @param sql             count sql
     * @param mappedStatement MappedStatement
     * @param boundSql        BoundSql
     * @param page            IPage
     * @param connection      Connection
     */
    protected void queryTotal(String sql, MappedStatement mappedStatement, BoundSql boundSql,
                              IPage<?> page, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            DefaultParameterHandler parameterHandler = new MybatisDefaultParameterHandler(
                mappedStatement, boundSql.getParameterObject(), boundSql);
            parameterHandler.setParameters(statement);
            long total = 0;
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    total = resultSet.getLong(1);
                }
            }
            page.setTotal(total);
            if (this.overflow && page.getCurrent() > page.getPages()) {
                //溢出总页数处理
                handlerOverflow(page);
            }
        } catch (Exception e) {
            throw ExceptionUtils.mpe("Error: Method queryTotal execution error of sql : \n %s \n",
                e, sql);
        }
    }

    /**
     * 处理页数溢出,默认设置为第一页
     *
     * @param page IPage
     */
    protected void handlerOverflow(IPage<?> page) {
        page.setCurrent(1);
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties prop) {
        String dialectType = prop.getProperty("dialectType");
        String dialectClazz = prop.getProperty("dialectClazz");
        if (StringUtils.isNotBlank(dialectType)) {
            setDialectType(dialectType);
        }
        if (StringUtils.isNotBlank(dialectClazz)) {
            setDialectClazz(dialectClazz);
        }
    }
}
