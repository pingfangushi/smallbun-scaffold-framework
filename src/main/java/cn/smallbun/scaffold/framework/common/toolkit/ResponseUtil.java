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


package cn.smallbun.scaffold.framework.common.toolkit;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * Response 工具类
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/4/29
 */
public class ResponseUtil {
	/**
	 * 返回JSON数据
	 *
	 * @param response HttpServletResponse
	 * @param status   状态码
	 * @param data     数据
	 */
	public static void responseJson(HttpServletResponse response, int status, Object data) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "*");
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(status);
			response.getWriter().write(JSONObject.toJSONString(data));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Wrap the optional into a {@link ResponseEntity} with an {@link HttpStatus#OK} status, or if it's empty, it
	 * returns a {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}.
	 *
	 * @param <X>           type of the response
	 * @param maybeResponse response to return if present
	 * @return response containing {@code maybeResponse} if present or {@link HttpStatus#NOT_FOUND}
	 */
	public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
		return wrapOrNotFound(maybeResponse, null);
	}

	/**
	 * Wrap the optional into a {@link ResponseEntity} with an {@link HttpStatus#OK} status with the headers, or if it's
	 * empty, it returns a {@link ResponseEntity} with {@link HttpStatus#NOT_FOUND}.
	 *
	 * @param <X>           type of the response
	 * @param maybeResponse response to return if present
	 * @param header        headers to be added to the response
	 * @return response containing {@code maybeResponse} if present or {@link HttpStatus#NOT_FOUND}
	 */
	public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
		return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
}
