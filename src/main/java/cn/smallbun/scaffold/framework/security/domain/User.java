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

package cn.smallbun.scaffold.framework.security.domain;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * User
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/11/2 16:24
 */
public class User extends org.springframework.security.core.userdetails.User {
	/**
	 * 匿名用户
	 */
	public static final String ANONYMOUS_USER = "anonymousUser";
	/**
	 * UID
	 */
	private String id;

	/**
	 * Calls the more complex constructor with all boolean arguments set to {@code true}.
	 * @param username
	 * @param password
	 * @param authorities
	 */
	public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
	}

	/**
	 * Calls the more complex constructor with all boolean arguments set to {@code true}.
	 * @param username
	 * @param password
	 * @param id
	 * @param authorities
	 */
	public User(String username, String password, String id, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = id;
	}

	/**
	 * Construct the <code>User</code> with the details required by
	 * {@link DaoAuthenticationProvider}.
	 *
	 * @param username the username presented to the
	 * <code>DaoAuthenticationProvider</code>
	 * @param password the password that should be presented to the
	 * @param id the user id
	 * <code>DaoAuthenticationProvider</code>
	 * @param enabled set to <code>true</code> if the user is enabled
	 * @param accountNonExpired set to <code>true</code> if the account has not expired
	 * @param credentialsNonExpired set to <code>true</code> if the credentials have not
	 * expired
	 * @param accountNonLocked set to <code>true</code> if the account is not locked
	 * @param authorities the authorities that should be granted to the caller if they
	 * presented the correct username and password and the user is enabled. Not null.
	 *
	 * @throws IllegalArgumentException if a <code>null</code> value was passed either as
	 * a parameter or as an element in the <code>GrantedAuthority</code> collection
	 */
	public User(String username, String password, String id, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
