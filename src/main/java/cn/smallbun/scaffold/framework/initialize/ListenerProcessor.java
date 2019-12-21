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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

/**
 * 监听器处理器，本来想初始化带有自定义注解的方法的，突然想到spring提供了一个@PostConstruct 决定不实现了，留着这个类吧
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/11/18 19:42
 */
@Slf4j
public class ListenerProcessor implements BeanPostProcessor {
    /**
     * 初始化之前
     * @param bean {@link Object} bean
     * @param beanName {@link String} beanName
     * @return {@link Object}
     * @throws BeansException BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(@Nullable Object bean,
                                                  String beanName) throws BeansException {
        return bean;
    }

    /**
     * bean初始化后
     * @param bean {@link Object} bean
     * @param beanName {@link String} beanName
     * @return {@link Object}
     * @throws BeansException BeansException
     */
    @Override
    public Object postProcessAfterInitialization(@Nullable Object bean,
                                                 String beanName) throws BeansException {
        return bean;
    }

}
