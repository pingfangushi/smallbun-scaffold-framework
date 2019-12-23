/*
 * smallbun-scaffold-framework - smallbun企业级开发脚手架-核心框架
 * Copyright © 2019 SanLi (qinggang.zuo@gmail.com) ${company}
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
package cn.smallbun.scaffold.framework.security.exception;

import org.springframework.security.authentication.AccountStatusException;

/**
 * HaveNotAuthorityException
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/12/10 20:06
 */
public class HaveNotAuthorityException extends AccountStatusException {
    /**
     * Constructs an {@code AuthenticationException} with the specified message and root
     * cause.
     *  @param msg the detail message
     * @param t the root cause
     */
    public HaveNotAuthorityException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     * Constructs an {@code AuthenticationException} with the specified message and no
     * root cause.
     *
     * @param msg the detail message
     */
    public HaveNotAuthorityException(String msg) {
        super(msg);
    }
}
