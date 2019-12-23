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
package cn.smallbun.scaffold.framework.common.toolkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2019/2/16 17:40
 */
public class ReflectionHelper {
    /**
     * 获取指定类型内的所有方法
     *
     * @param clazz 类型
     * @return Method[]
     */
    public static Method[] getMethods(Class<?> clazz) {
        return clazz.getDeclaredMethods();
    }

    /**
     * 递归获取指定类型内以及类型的所有上级内定义的方法
     *
     * @param clazz 类型
     * @return List<Method>
     */
    public static List<Method> getAllMethods(Class<?> clazz) {
        //自动注册继承的接口
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces != null && interfaces.length > 0) {
            for (Class<?> anInterface : interfaces) {
                getAllMethods(anInterface);
            }
        }
        return null;
    }

    /**
     * 获取实体类内 & 父类内的所有字段,如果父类存在和子类相同的属性，排除父类的，使用子类的
     *
     * @param clazz clazz
     * @return List
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        List<Field> result = new LinkedList<>(Arrays.asList(fields));
        Class<?> superClass = clazz.getSuperclass();
        if (superClass.equals(Object.class)) {
            return result;
        }
        // 获取父类全部字段
        List<Field> superAllFields = getAllFields(superClass);
        // 过滤排除
        for (Field i : result) {
            superAllFields = superAllFields.stream().filter(j -> !i.getName().equals(j.getName()))
                .collect(Collectors.toList());
        }
        result.addAll(superAllFields);
        return result;
    }

    /**
     * 获取实体类内的所有字段并自动排除存在@Transient注解的字段
     *
     * @param clazz clazz
     * @return List
     */
    public static List<Field> getAllFieldsExcludeTransient(Class<?> clazz) {
        List<Field> result = new LinkedList<Field>();
        List<Field> list = getAllFields(clazz);
        for (Field field : list) {
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            result.add(field);
        }
        return result;
    }

    /**
     * 获取字段
     * 检索本类内是否存在，检索不到再去找父类内的字段
     *
     * @param clazz clazz
     * @param fieldName fieldName
     * @return Field
     */
    public static Field getField(Class<?> clazz, String fieldName) throws Exception {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                field = clazz.getSuperclass().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e1) {
                try {
                    field = clazz.getSuperclass().getSuperclass().getDeclaredField(fieldName);
                } catch (NoSuchFieldException ignored) {

                }
            }
        }
        if (field == null) {
            throw new RuntimeException("Not Found Field：" + fieldName + " in Class "
                                       + clazz.getName() + " and super Class.");
        }
        return field;
    }

}
