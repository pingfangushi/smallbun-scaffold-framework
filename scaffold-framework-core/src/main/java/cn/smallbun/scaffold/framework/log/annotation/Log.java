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
package cn.smallbun.scaffold.framework.log.annotation;

import cn.smallbun.scaffold.framework.log.enmus.Operate;
import cn.smallbun.scaffold.framework.log.enmus.Platform;

import java.lang.annotation.*;

/**
 * 日志注解
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/10/7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Inherited
public @interface Log {
    /**
     * 模块，只有注解在类上才会解析
     *
     * @return {@link String}
     */
    String module() default "";

    /**
     * 功能，只有方法上才会解析
     * @return {@link String}
     */
    String feature() default "";

    /**
     * 操作类型
     * @return {@link String}
     */
    Operate action() default Operate.UNKNOWN;

    /**
     * 平台类型
     */
    Platform platform() default Platform.UNKNOWN;

    /**
     * 是否保存入库
     */
    boolean save() default true;
}
