/*
 * Copyright (c) 2018-2019.‭‭‭‭‭‭‭‭‭‭‭‭[zuoqinggang] www.pingfangushi.com
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */


package cn.smallbun.scaffold.framework.common.toolkit;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * 反射的 Utils 函数集合 提供访问私有变量, 获取泛型类型 Class, 提取集合中元素属性等 Utils 函数
 *
 * @author SanLi
 */
@Slf4j
public class ReflectionUtil {


	/**
	 * 通过反射, 获得定义 Class 时声明的父类的泛型参数的类型 如: public EmployeeDao extends
	 * BaseDao<Employee, String>
	 *
	 * @param clazz
	 * @param index
	 * @return
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			log.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			log.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
					+ params.length);
			return Object.class;
		}

		if (!(params[index] instanceof Class)) {
			log.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
			return Object.class;
		}

		return (Class<?>) params[index];
	}

	/**
	 * 通过反射, 获得 Class 定义中声明的父类的泛型参数类型 如:
	 *
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperGenericType(Class<?> clazz) {
		return (Class<T>) getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 获取所有字段
	 * @param object
	 * @return
	 */
	public static List<Field> getFieldAll(Object object) {
		List<Field> fieldList = Lists.newArrayList();
		Class aClass = object.getClass();
		//当父类为null的时候说明到达了最上层的父类(Object类).
		while (aClass != null) {
			fieldList.addAll(Arrays.asList(aClass.getDeclaredFields()));
			//得到父类,然后赋给自己
			aClass = aClass.getSuperclass();
		}
		return fieldList;
	}

	/**
	 * 根据字段名称获取字段
	 * @param object
	 * @param field
	 * @return
	 */
	public static Field getFieldAll(Object object, String field) throws NoSuchFieldException {
		for (Field field1 : getFieldAll(object)) {
			if (field1.getName().equals(field)) {
				return field1;
			}

		}
		throw new NoSuchFieldException();
	}
}