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


package cn.smallbun.scaffold.framework.logger;

import cn.smallbun.scaffold.framework.logger.domain.Logger;
import org.aspectj.lang.JoinPoint;

import java.io.Serializable;

/**
 * 日志逻辑接口，具体子类实现，方便后期扩展
 * @author SanLi
 * Created by 2689170096@qq.com on 2019/2/15 21:54
 */
public interface ILoggingRecord extends Serializable {

	/**
	 * 记录数据
	 * @param log {@link Logger} 日志数据
	 * @param joinPoint {@link JoinPoint}
	 */
	void recording(Logger log, JoinPoint joinPoint);
}
