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


package cn.smallbun.scaffold.framework.exception;

import lombok.extern.slf4j.Slf4j;


/**
 * 自定义异常基类
 *
 * @author SanLi
 * Created by 2689170096@qq.com  on 2018/1/9
 */
@Slf4j
public abstract class BaseException extends RuntimeException {

	/**
	 * 异常状态码
	 */
	private String status;
	/**
	 * 错误信息
	 */
	private String msg;

	public BaseException(String status, String msg) {
		super(msg);
		this.status = status;
		this.msg = msg;
		log.error("异常原因(status:{},msg:{})", status, msg);
	}

	public BaseException(String message, String status, String msg) {
		super(message);
		this.status = status;
		this.msg = msg;
	}

	public BaseException(String message, Throwable cause, String status, String msg) {
		super(message, cause);
		this.status = status;
		this.msg = msg;
	}

	public BaseException(Throwable cause, String status, String msg) {
		super(cause);
		this.status = status;
		this.msg = msg;
	}

	public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace,
			String status, String msg) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.status = status;
		this.msg = msg;
	}

	public String getStatus() {
		return status;
	}

	public void status(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void msg(String msg) {
		this.msg = msg;
	}
}
