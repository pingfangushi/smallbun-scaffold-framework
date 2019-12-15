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


package cn.smallbun.scaffold.framework.initialize;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * Spring容器加载完毕,用于初始化操作
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/9/3
 */
@Slf4j
public class ContextInitListener implements ApplicationListener<ContextRefreshedEvent> {
	/**
	 * 初始化方法名称
	 */
	private static final String INIT = "initialize";


	@Override
	public void onApplicationEvent(@Nullable ContextRefreshedEvent contextRefreshedEvent) {
		log.debug("初始化带有 @Initialize 注解Bean的 {}() 开始", INIT);
		// spring初始化完毕后，通过反射调用所有使用Initialize注解的方法
		Map<String, Object> initServices = Objects.requireNonNull(contextRefreshedEvent).getApplicationContext()
				.getBeansWithAnnotation(Initialize.class);
		if (!CollectionUtils.isEmpty(initServices)) {
			for (Object service : initServices.values()) {
				log.debug("执行 @Initialize 注解Bean的 {}() {}", INIT, service.getClass().getName());
				try {
					Method initMapper = service.getClass().getMethod(INIT);
					initMapper.invoke(service);
				} catch (Exception e) {
					if (e instanceof NoSuchMethodException) {
						log.warn("找不到{}", e.getMessage());
					} else {
						log.error("初始化带有 @Initialize 注解Bean的 {}() 异常 {}", INIT, e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		log.debug("初始化带有 @Initialize 注解Bean的 {}()结束", INIT);
		// Spring 框架初始化完毕后，通过反射调用所有实现 InitInterface 接口的 initialize 方法
		log.debug("初始化 InitInterface 接口 {}() 开始", INIT);
		Map<String, InitInterface> initInterface = contextRefreshedEvent.getApplicationContext()
				.getBeansOfType(InitInterface.class);
		if (!CollectionUtils.isEmpty(initInterface)) {
			for (Object service : initInterface.values()) {
				log.debug("执行 InitInterface{}.{}()", INIT, service.getClass().getName());
				try {
					Method init = service.getClass().getMethod(INIT);
					init.invoke(service);
				} catch (Exception e) {
					log.error("初始化 InitInterface 接口 {}()异常{}", INIT, e.getMessage());
					e.printStackTrace();
				}
			}
		}
		log.debug("初始化 InitInterface 接口 {}()结束", INIT);
	}


}
