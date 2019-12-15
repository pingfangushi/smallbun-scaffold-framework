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


package cn.smallbun.scaffold.framework.security.listener;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.annotation.Nullable;

/**
 * SecurityListener
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/9/3
 */
@Slf4j
public class SecurityListener implements ApplicationListener<ContextRefreshedEvent> {

	private Logger logger = LoggerFactory.getLogger(SecurityListener.class);

	@Override
	public void onApplicationEvent(@Nullable ContextRefreshedEvent contextRefreshedEvent) {
		if (properties.getSecurity().isPasswordGenerated()) {
			logger.info(String.format("%n%nUsing generated security password: %s%n",
					properties.getSecurity().getNativePassword()));
		}
	}

	@Autowired
	private SmallBunProperties properties;


}
