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
package cn.smallbun.scaffold.framework.mybatis.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import cn.smallbun.scaffold.framework.mybatis.page.Page;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;

/**
 * 结果映射工具类
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/6/20 21:17
 */
public class MappingHelp implements Serializable {
    /**
     * 单个值转换
     * @param source source
     * @param destination destination
     * @param <TSource> TSource
     * @param <TDestination> TDestination
     * @return TDestination
     */
    public static <TSource, TDestination> TDestination mapping(TSource source,
                                                               TDestination destination) {
        if (!Objects.isNull(source)) {
            Method[] srcMethods = source.getClass().getMethods();
            Method[] destMethods = destination.getClass().getMethods();
            for (Method m : srcMethods) {
                String srcMethodName = m.getName();
                //调用get方法
                if (srcMethodName.startsWith("get")) {
                    try {
                        //获取当前方法返回值(获取当前属性值)
                        Object getValue = m.invoke(source);
                        for (Method dm : destMethods) {
                            //目标方法名称
                            String destMethodName = dm.getName();
                            if (destMethodName.startsWith("set")
                                && destMethodName.substring(3).equals(srcMethodName.substring(3))) {
                                dm.invoke(destination, getValue);
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
            return destination;
        }
        return null;
    }

    /**
     * list 转换
     * @param src src
     * @param targetClass targetClass
     * @param <S> S
     * @param <T> T
     * @return List
     */
    public static <S, T> List<T> listMapping(List<S> src, Class<T> targetClass) {
        List<T> target = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(src)) {
            for (S s : src) {
                try {
                    T object = targetClass.newInstance();
                    target.add(object);
                    mapping(s, object);
                } catch (Exception ignored) {
                }
            }
            return target;
        }
        return null;
    }

    /**
     * page 转换
     * @param src 来源
     * @param targetClass 目标类
     * @param <S> src
     * @param <T> targetClass
     * @return PaginationResult {@link Page}
     */
    public static <S, T> Page<T> pageMapping(IPage<S> src, Class<T> targetClass) {
        //数据集合
        Page<T> result = new Page<>();
        result.setList(listMapping(src.getRecords(), targetClass));
        //页面数据
        Page.Pagination pagination = new Page.Pagination();
        pagination.setCurrent(src.getCurrent());
        pagination.setPageSize(src.getSize());
        pagination.setTotal(src.getTotal());
        result.setPagination(pagination);
        return result;
    }
}
