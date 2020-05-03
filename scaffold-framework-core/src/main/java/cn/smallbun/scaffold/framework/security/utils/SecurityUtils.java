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
package cn.smallbun.scaffold.framework.security.utils;

import com.google.common.collect.Lists;
import cn.smallbun.scaffold.framework.security.authority.AuthorityInfo;
import cn.smallbun.scaffold.framework.security.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Spring Security的实用程序类。
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on  2019/5/7
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                return (String) authentication.getPrincipal();
            }
            return null;
        });
    }

    /**
     * Get the login of the current user id.
     *
     * @return the login of the current user id.
     */
    public static Optional<String> getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof User) {
                User springSecurityUser = (User) authentication.getPrincipal();
                return springSecurityUser.getId();
            }
            return null;
        });
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the {@code isUserInRole()} method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority)))
            .orElse(false);
    }

    /**
     * 获取权限信息
     *
     * @return {@link AuthorityInfo}
     */
    public static AuthorityInfo getAuthorityInfo() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        User user = (User) securityContext.getAuthentication().getPrincipal();
        return user.getAuthorityInfo();
    }

    /**
     * 获取权限
     *
     * @param info {@link AuthorityInfo}
     * @return {@link List <GrantedAuthority>}
     */
    public static List<GrantedAuthority> getGrantedAuthority(AuthorityInfo info) {
        //操作权限
        List<String> auth = info.getActions().stream().map(AuthorityInfo.AuthorityItem::getAuth)
            .collect(Collectors.toList());
        //接口权限
        auth.addAll(info.getInterfaces().stream().map(AuthorityInfo.AuthorityItem::getAuth)
            .collect(Collectors.toList()));
        //路由权限
        auth.addAll(info.getRouters().stream().map(AuthorityInfo.AuthorityItem::getAuth)
            .collect(Collectors.toList()));
        List<GrantedAuthority> authorities = Lists.newArrayList();
        for (String s : auth) {
            authorities.addAll(AuthorityUtils.createAuthorityList(s));
        }
        return authorities;
    }

    /**
     * 是否为系统用户
     *
     * @param username 用户名
     * @return true false
     */
    public static boolean userAvailable(String username) {
        return false;
    }
}
