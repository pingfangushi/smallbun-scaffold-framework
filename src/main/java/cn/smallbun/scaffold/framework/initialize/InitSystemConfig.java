/*
 * smallbun-scaffold-framework - smallbun企业级开发脚手架-核心框架
 * Copyright © 2019 zuoqinggang (qinggang.zuo@gmail.com / 2689170096@qq.com)
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
package cn.smallbun.scaffold.framework.initialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;

/**
 * 初始化系统配置
 * <p>
 * 如果有多个这样的类时，可以通过Order指定执行顺序，数值越小执行优先级越高@Order(value = 0)
 * </p>
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/10/6
 */
@Slf4j
@Component
public class InitSystemConfig implements CommandLineRunner, EnvironmentAware {
    /**
     * 在服务启动后执行，会在@Bean实例化之后执行，故如果@Bean需要依赖这里的话会出问题
     *
     * @param args ${@link String}
     */
    @Override
    public void run(String... args) {
        //这里可以根据数据库返回结果创建一些对象、启动一些线程等

    }

    /**
     * 在@Bean实例化之前执行常用于读取数据库配置以供其它bean使用
     * environment对象可以获取配置文件的配置，也可以把配置设置到该对象中
     *
     * @param environment ${@link Environment }
     */
    @Override
    public void setEnvironment(@Nullable Environment environment) {

    }
}
