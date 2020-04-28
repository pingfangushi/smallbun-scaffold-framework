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
package cn.smallbun.scaffold.framework.demo.aspect;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import cn.smallbun.scaffold.framework.demo.annotation.DemoEnvironment;
import cn.smallbun.scaffold.framework.demo.exception.DemoEnvironmentException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 演示环境
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/11/23
 */
@Component
@Slf4j
@Order(value = 0)
@Aspect
public class DemoEnvironmentAspect {

    public DemoEnvironmentAspect(SmallBunProperties properties) {
        this.properties = properties;
    }

    /**
     * 配置切面
     */
    @Pointcut(value = "@annotation(environment)")
    public void log(DemoEnvironment environment) {
    }

    /**
     * 请求Controller 日志处理
     *
     * @param joinPoint {@link JoinPoint}
     */
    @Before(value = "log(environment)", argNames = "joinPoint,environment")
    public void demoBefore(JoinPoint joinPoint, DemoEnvironment environment) {
        //1.如果系统没开启演示环境，忽略注解
        //2.如果系统整体开启了，根据注解值进行判断是否拦截并提示，演示环境不允许操作
        if (properties.getDemo().isOpen()) {
            throw new DemoEnvironmentException("演示环境不允许操作");
        }
    }

    private final SmallBunProperties properties;
}
