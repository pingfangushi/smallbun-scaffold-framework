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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 表工具类
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/5/17 18:45
 */
public class TableUtil {

    /**
     * 根据字段名称获取字段
     * @param aClass {@link Class} 类
     * @param name {@link String} 字段名称
     * @return  {@link Field}
     * @throws NoSuchFieldException
     */
    public static Field getField(Class<?> aClass, String name) throws NoSuchFieldException {
        List<Field> fields = TableInfoHelper.getAllFields(aClass);
        for (Field field : fields) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        throw new NoSuchFieldException();
    }

    /**
     * 根据字段名称获取表明
     * @param aClass {@link Class} 类
     * @param name {@link String} 字段名称
     * @return  {@link Field}
     * @throws NoSuchFieldException
     */
    public static String getColumnName(Class<?> aClass, String name) throws NoSuchFieldException {
        Field field = getField(aClass, name);
        if (field.getName().equals(name)) {
            TableField tableField = field.getAnnotation(TableField.class);
            if (StringUtils.isEmpty(tableField.value())) {
                return field.getName();
            }
            return tableField.value();
        }
        throw new NoSuchFieldException();
    }
}
