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
package cn.smallbun.scaffold.framework.common.toolkit;

import com.google.common.collect.Lists;
import org.apache.ibatis.mapping.BoundSql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.List;

/**
 * 反射工具类
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/4/30
 */
public class ReflectUtil {

    /**
     * 获取成员变量的修饰符
     *
     * @param clazz clazz
     * @param field field
     * @return int
     * @throws Exception Exception
     */
    public static <T> int getFieldModifier(Class<T> clazz, String field) throws Exception {
        //getDeclaredFields可以获取所有修饰符的成员变量，包括private,protected等getFields则不可以
        Field[] fields = getFields(clazz);

        for (Field value : fields) {
            if (value.getName().equals(field)) {
                return value.getModifiers();
            }
        }
        throw new Exception(clazz + " has no field \"" + field + "\"");
    }

    /**
     * 获取成员方法的修饰符
     *
     * @param clazz clazz
     * @param method method
     * @return int
     * @throws Exception Exception
     */
    public static <T> int getMethodModifier(Class<T> clazz, String method) throws Exception {

        /*getDeclaredMethods可以获取所有修饰符的成员方法，包括private,protected等getMethods()则不可以*/
        Method[] m = clazz.getDeclaredMethods();

        for (Method value : m) {
            if (value.getName().equals(method)) {
                return value.getModifiers();
            }
        }
        throw new Exception(clazz + " has no method \"" + method + "\"");
    }

    /**
     * 根据成员变量名称获取对象值
     *
     * @param clazzInstance clazzInstance
     * @param field  field
     * @return Object
     * @throws IllegalAccessException IllegalAccessException
     * @throws IllegalArgumentException IllegalArgumentException
     * @throws SecurityException SecurityException
     */
    public static <T> Object getFieldValue(Object clazzInstance,
                                           Object field) throws IllegalArgumentException,
                                                         IllegalAccessException {

        Field[] fields = getFields(clazzInstance.getClass());

        for (Field value : fields) {
            if (value.getName().equals(field)) {
                //对于私有变量的访问权限，在这里设置，这样即可访问Private修饰的变量
                value.setAccessible(true);
                return value.get(clazzInstance);
            }
        }
        return null;
    }

    /**
     * 根据成员变量名称获取类值（默认值）
     *
     * @param clazz clazz
     * @param field field
     * @return Object
     * @throws IllegalArgumentException IllegalArgumentException
     * @throws IllegalAccessException IllegalAccessException
     * @throws InstantiationException InstantiationException
     */
    public static <T> Object getFieldValue(Class<T> clazz,
                                           String field) throws IllegalArgumentException,
                                                         IllegalAccessException,
                                                         InstantiationException {

        Field[] fields = getFields(clazz);

        for (Field value : fields) {
            if (value.getName().equals(field)) {
                //对于私有变量的访问权限，在这里设置，这样即可访问Private修饰的变量
                value.setAccessible(true);
                return value.get(clazz.newInstance());
            }
        }

        return null;
    }

    /**
     * 利用反射设置指定对象的指定属性为指定的值
     *
     * @param obj {@link BoundSql}
     * @param fieldName {@link String}
     * @param fieldValue {@link String}
     */
    public static void setFieldValue(BoundSql obj, String fieldName,
                                     String fieldValue) throws IllegalArgumentException,
                                                        IllegalAccessException {
        Field field = getField(obj.getClass(), fieldName);
        if (field != null) {
            field.setAccessible(true);
            field.set(obj, fieldValue);
        }
    }

    /**
     * 返回指定字段
     *
     * @param clazz clazz
     * @param fieldName fieldName
     * @return Object
     */
    public static <T> Field getField(Class<T> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if (field == null) {
                field = clazz.getSuperclass().getDeclaredField(fieldName);
            }
            return field;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有的成员变量 (包含父类的成员变量)
     *
     * @param clazz clazz
     * @return Field[]
     */
    public static <T> Field[] getFields(Class<T> clazz) {

        Field[] superFields = clazz.getSuperclass().getDeclaredFields();
        Field[] extFields = clazz.getDeclaredFields();
        Field[] fields = new Field[superFields.length + extFields.length];
        System.arraycopy(superFields, 0, fields, 0, superFields.length);
        System.arraycopy(extFields, 0, fields, superFields.length, extFields.length);

        return fields;
    }

    /**
     * 获取所有的方法  (包含父类的方法 )
     *
     * @param clazz clazz
     * @return Field[]
     */
    public static <T> Method[] getMethods(Class<T> clazz) {
        Method[] superMethods = clazz.getSuperclass().getDeclaredMethods();
        Method[] extMethods = clazz.getDeclaredMethods();
        Method[] methods = new Method[superMethods.length + extMethods.length];
        System.arraycopy(superMethods, 0, methods, 0, superMethods.length);
        System.arraycopy(extMethods, 0, methods, superMethods.length, extMethods.length);

        return methods;
    }

    /**
     * 获取指定的方法 (包含父类的方法)
     *
     * @param clazz clazz
     * @param methodName methodName
     * @return Method
     */
    public static <T> Method getMethod(Class<T> clazz, String methodName) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        methods = clazz.getSuperclass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        return null;
    }

    /**
     * 获取持久化模型的持久属性,判断根据是:
     * 1. Field的Modifiers==Private
     * 2. Field的Type属于基本类型
     *
     * @param clazz clazz
     * @return object
     */
    public static <T> Field[] getPersistFields(Class<T> clazz) {
        Field[] fields = getFields(clazz);

        List<Field> result = Lists.newArrayList();

        for (Field field : fields) {
            if (field.getModifiers() != Modifier.PRIVATE) {
                continue;
            }
            result.add(field);
        }

        return result.toArray(new Field[] {});
    }

    /**
     * 对对象中为空的数据进行处理
     *
     * @param model 对象
     */
    public static void emptyProcessing(Object model) {
        // 获取实体类的所有属性，返回Field数组
        Field[] field = model.getClass().getDeclaredFields();
        try {
            // 遍历所有属性
            for (Field aField : field) {
                // 获取属性的名字
                String name = aField.getName();
                // 将属性的首字符大写，方便构造get，set方法
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                // 获取属性的类型
                String type = aField.getGenericType().toString();
                // 如果type是类类型，则前面包含"class "，后面跟类名
                if ("class java.lang.String".equals(type)) {
                    Method m = model.getClass().getMethod("get" + name);
                    // 调用getter方法获取属性值
                    m.setAccessible(true);
                    String value = (String) m.invoke(model);
                    if (value == null) {
                        m = model.getClass().getMethod("set" + name, String.class);
                        m.setAccessible(true);
                        m.invoke(model, "\"\"");
                    }
                }
                if ("class java.lang.Integer".equals(type)) {
                    Method m = model.getClass().getMethod("get" + name);
                    m.setAccessible(true);
                    Integer value = (Integer) m.invoke(model);
                    if (value == null) {
                        m = model.getClass().getMethod("set" + name, Integer.class);
                        m.setAccessible(true);
                        m.invoke(model, 0);
                    }
                }
                if ("class java.lang.Boolean".equals(type)) {
                    Method m = model.getClass().getMethod("get" + name);
                    m.setAccessible(true);
                    Boolean value = (Boolean) m.invoke(model);
                    if (value == null) {
                        m = model.getClass().getMethod("set" + name, Boolean.class);
                        m.setAccessible(true);
                        m.invoke(model, false);
                    }
                }
                if ("class java.util.Date".equals(type)) {
                    Method m = model.getClass().getMethod("get" + name);
                    m.setAccessible(true);
                    Date value = (Date) m.invoke(model);
                    if (value == null) {
                        m = model.getClass().getMethod("set" + name, Date.class);
                        m.setAccessible(true);
                        m.invoke(model, new Date());
                    }
                }
                // 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断
            }
        } catch (NoSuchMethodException | IllegalAccessException | SecurityException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
