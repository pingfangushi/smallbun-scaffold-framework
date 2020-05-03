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
package cn.smallbun.scaffold.framework.security.authority;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

/**
 * SecurityAuthorizeProvider
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/3/31 15:07
 */
public interface SecurityAuthorizeProvider extends Serializable {

    /**
     * 根据用户ID获取授权
     *
     * @param id {@link String} 用户ID
     * @return {@link AuthorityInfo}
     */
    AuthorityInfo getAuthorityInfo(String id);

    /**
     * 获取全部授权信息
     *
     * @return {@link AuthorityInfo}
     */
    AuthorityInfo getAuthorityInfo();

    /**
     * 获取所有授权信息
     *
     * @return {@link List<GrantedAuthority>}
     */
    List<GrantedAuthority> getGrantedAuthority();

    /**
     * 通过用户ID获取授予的权限
     *
     * @param id {@link String} 用户ID
     * @return {@link List<GrantedAuthority>}
     */
    List<GrantedAuthority> getGrantedAuthority(String id);
}
