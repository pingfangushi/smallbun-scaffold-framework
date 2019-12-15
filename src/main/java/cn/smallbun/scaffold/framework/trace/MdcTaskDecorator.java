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


package cn.smallbun.scaffold.framework.trace;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 *线程池任务执行程序
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/7/30 8:56
 */
public class MdcTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		try {
			// 现在：Web线程上下文 !
			// 抓取当前线程MDC数据
			Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();

			return () -> {
				// 现在：@Async线程上下文！
				// 恢复Web线程上下文的MDC数据
				if (!CollectionUtils.isEmpty(copyOfContextMap)) {
					MDC.setContextMap(copyOfContextMap);
				}
				runnable.run();
			};
		} finally {
			MDC.clear();
		}
	}

}
