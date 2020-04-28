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
package cn.smallbun.scaffold.framework.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * 安全认证权限提供者
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/31 15:07
 */
public interface SecurityAuthorizeProvider {
    /**
     * 获取授权
     *
     * @param userId {@link String} 用户ID
     * @return {@link List< GrantedAuthority >}
     */
    List<GrantedAuthority> getAuthority(String userId);

    /**
     * 获取授权
     *
     * @param userId {@link String} 用户ID
     * @return {@link AuthorityInfo}
     */
    AuthorityInfo getAuthorityInfo(String userId);
}
