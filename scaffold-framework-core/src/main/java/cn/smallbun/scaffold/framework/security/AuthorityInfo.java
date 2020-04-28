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

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 权限IFNO
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/4/9 11:44
 */
@Data
public class AuthorityInfo implements Serializable {
    /**
     * 路由权限
     */
    private List<AuthorityItem> routers;
    /**
     * 操作权限
     */
    private List<AuthorityItem> actions;
    /**
     * 接口权限
     */
    private List<AuthorityItem> interfaces;

    @Data
    public static class AuthorityItem {
        /**
         * 授权
         */
        private String auth;
        /**
         * 描述
         */
        private String describe;

    }
}
