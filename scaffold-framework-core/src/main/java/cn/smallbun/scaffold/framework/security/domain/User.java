/*
 * scaffold-framework-core - smallbun企业级开发脚手架-核心框架
 * Copyright © 2018-2020 SanLi (qinggang.zuo@gmail.com)
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
package cn.smallbun.scaffold.framework.security.domain;

import cn.smallbun.scaffold.framework.security.authority.AuthorityInfo;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import java.util.ArrayList;

import static cn.smallbun.scaffold.framework.security.utils.SecurityUtils.getGrantedAuthority;

/**
 * User
 *
 * 继承spring security User 加入自定义用户ID，权限信息等
 *
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
    private String             id;
    /**
     * 权限INFO
     */
    private AuthorityInfo      authorityInfo;

    /**
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     *
     * @param username      用户名
     * @param userId        用户ID
     * @param authorityInfo 权限信息
     */
    public User(String username, String userId, AuthorityInfo authorityInfo) {
        // 权限转换
        super(username, "", getGrantedAuthority(authorityInfo));
        this.id = userId;
        this.authorityInfo = authorityInfo;
    }

    /**
     * Calls the more complex constructor with all boolean arguments set to {@code true}.
     *
     * @param username      用户名
     * @param password      密码
     * @param userId        用户ID
     * @param authorityInfo 权限信息
     */
    public User(String username, String password, String userId, AuthorityInfo authorityInfo) {
        // 权限转换
        super(username, password, getGrantedAuthority(authorityInfo));
        this.id = userId;
        this.authorityInfo = authorityInfo;
    }

    /**
     * 这里把父类的权限去除了，传给super为空集合，主要是为了时时权限，通过角色进行处理
     * Construct the <code>User</code> with the details required by
     * {@link DaoAuthenticationProvider}.
     *
     * @param username              the username presented to the
     *                              <code>DaoAuthenticationProvider</code>
     * @param password              the password that should be presented to the
     * @param id                    the user id
     *                              <code>DaoAuthenticationProvider</code>
     * @param enabled               set to <code>true</code> if the user is enabled
     * @param accountNonExpired     set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not
     *                              expired
     * @param accountNonLocked      set to <code>true</code> if the account is not locked
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as
     *                                  a parameter or as an element in the <code>GrantedAuthority</code> collection
     */
    public User(String username, String password, String id, boolean enabled,
                boolean accountNonExpired, boolean credentialsNonExpired,
                boolean accountNonLocked) {

        super(username, password, enabled, accountNonExpired, credentialsNonExpired,
            accountNonLocked, new ArrayList<>());
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuthorityInfo getAuthorityInfo() {
        return authorityInfo;
    }

    public void setAuthorityInfo(AuthorityInfo authorityInfo) {
        this.authorityInfo = authorityInfo;
    }
}
