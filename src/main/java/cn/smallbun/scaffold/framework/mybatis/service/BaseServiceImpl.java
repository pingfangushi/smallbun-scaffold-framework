/*
 * smallbun-scaffold-framework - smallbun企业级开发脚手架-核心框架
 * Copyright © 2019 SanLi (qinggang.zuo@gmail.com) ${company}
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
package cn.smallbun.scaffold.framework.mybatis.service;

import cn.smallbun.scaffold.framework.common.toolkit.ReflectionUtil;
import cn.smallbun.scaffold.framework.mybatis.domain.BaseEntity;
import cn.smallbun.scaffold.framework.mybatis.mapper.BaseMapper;
import cn.smallbun.scaffold.framework.mybatis.page.PageModel;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static cn.smallbun.scaffold.framework.common.toolkit.ReflectionHelper.getAllFields;
import static cn.smallbun.scaffold.framework.common.toolkit.ReflectionUtil.getFieldAll;
import static com.baomidou.mybatisplus.core.metadata.TableInfoHelper.getTableInfo;

/**
 *
 * 自定义BaseServiceImpl
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/5/9 12:00
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity>
                            extends ServiceImpl<M, T> implements BaseService<T> {
    /**
     * 日志
     */
    @SuppressWarnings("WeakerAccess")
    public final Logger        logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 主键ID
     */
    @SuppressWarnings("WeakerAccess")
    public static final String ID     = "id";

    /**
     * 分页查询
     * @param page {@link PageModel}
     * @return {@link IPage}
     */
    @Override
    public IPage<T> page(PageModel page) {
        try {
            Page<T> p = getPage(page);
            //查询
            logger.debug("{}", page);
            return baseMapper.selectPage(p, new QueryWrapper<>());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分页查询
     * @param page  {@link PageModel}
     * @param wrapper {@link QueryWrapper}
     * @return  {@link IPage}
     */
    @SuppressWarnings("DuplicatedCode")
    @Override
    public IPage<T> page(PageModel page, Wrapper<T> wrapper) {
        try {
            //构建Page对象
            Page<T> p = getPage(page);
            logger.debug("{}{}", page, wrapper);
            //查询
            return baseMapper.selectPage(p, wrapper);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 唯一记录
     * @param t t
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unique(T t) {
        //构建查询条件,将实体转换为map ，不包含ID
        QueryWrapper<T> queryWrapper = new QueryWrapper<T>().allEq(beanToMapExcludeId(t), false);
        //查询
        List<T> ts = baseMapper.selectList(queryWrapper);
        //表示为不是唯一
        boolean flag = false;
        //如果没有记录，唯一
        if (CollectionUtils.isEmpty(ts)) {
            flag = true;
        }
        //如果有记录
        else {
            //循环字段
            for (T u : ts) {
                for (Field f : getFieldAll(u)) {
                    //TODO 这里是针对修改的情况，获取ID字段的值，和当前ID字段值进行对比，如果ID相同，可以通过，如果不同，flag任为false
                    if (ID.equals(f.getName())) {
                        try {
                            f.setAccessible(true);
                            if (!StringUtils.isEmpty(t.getId())) {
                                if (t.getId().equals(f.get(u))) {
                                    flag = true;
                                }
                            }
                        } catch (IllegalAccessException e) {
                            logger.error("method uniqueResult Exception{}",
                                (Object) e.getStackTrace());
                        }
                    }
                }
            }
        }
        return flag;
    }

    /**
     * mybatis plus bean 转 map,排除ID字段
     * @param t T
     * @return map
     */
    private Map<String, Object> beanToMapExcludeId(T t) {
        //构建查询条件
        Map<String, Object> map = Maps.newHashMap();
        TableInfo tableInfo = getTableInfo(t.getClass());
        tableInfo.getFieldList().forEach(u -> getAllFields(t.getClass()).forEach(s -> {
            try {
                //暴力访问
                s.setAccessible(true);
                //其余字段
                if (u.getProperty().equals(s.getName())) {
                    map.put(u.getColumn(), s.get(t));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
        return map;
    }

    /**
     * 封装分页数据和字段
     * @param page  {@link PageModel} page
     * @return {@link Page}
     */
    public Page<T> getPage(PageModel page) {
        try {
            //构建Page对象
            Page<T> p = new Page<>();
            //当前页
            p.setCurrent(page.getCurrent());
            //每页显示条数
            p.setSize(page.getPageSize());
            //获取到实体类，根据字段名称获取对应的数据库字段、
            //为数据库表字段
            if (org.apache.commons.lang3.StringUtils.isNotBlank(page.getSorter())) {
                // 获取泛型类型
                TableInfo tableInfo = getTableInfo(
                    ReflectionUtil.getSuperClassGenricType(this.getClass(), 1));
                //排序
                List<OrderItem> items = Lists.newArrayList();
                //获取字段
                tableInfo.getFieldList().forEach(i -> {
                    if (i.getProperty().equals(page.getSorter())) {
                        OrderItem order = new OrderItem();
                        order.setAsc(page.isAsc());
                        order.setColumn(i.getColumn());
                        items.add(order);
                    }
                });
                p.setOrders(items);
            }
            return p;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
