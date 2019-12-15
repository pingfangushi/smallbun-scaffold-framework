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


package cn.smallbun.scaffold.framework.web.interceptor;

import cn.smallbun.scaffold.framework.common.toolkit.UserAgentUtil;
import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 判断访问类型拦截器
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/9/2
 */
@Slf4j
public class UserAgentInterceptor implements HandlerInterceptor {

	private final SmallBunProperties properties;

	public UserAgentInterceptor(SmallBunProperties properties) {
		this.properties = properties;
	}

	/**
	 * 请求之前
	 *
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param handler {@link Object}
	 * @return boolean
	 * @throws Exception Exception
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("----------------------------------------------------------");
		//如果是PC通过
		if (UserAgentUtil.isComputer(request)) {
			log.debug("用户请求设备类型为:{},放行", UserAgentUtil.getDeviceType(request));
			log.debug("----------------------------------------------------------");
			return true;
		}
		log.debug("用户请求设备类型为:{},跳转", UserAgentUtil.getDeviceType(request));
		//设置返回客户端的编码
		response.setContentType("text/html;charset=UTF-8");
		//重定向到手机访问页面
		response.sendRedirect(properties.getWeb().getIsPhonePath());
		log.debug("----------------------------------------------------------");
		return false;

	}

	/**
	 * 请求之后
	 *
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param handler {@link Object}
	 * @param modelAndView {@link ModelAndView}
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {
	}

	/**
	 * 整个请求结束后
	 *
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 * @param handler {@link Object}
	 * @param ex {@link Exception}
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
	}

}
