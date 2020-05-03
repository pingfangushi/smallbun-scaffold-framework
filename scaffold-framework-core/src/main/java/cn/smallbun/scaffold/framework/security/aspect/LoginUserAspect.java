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
package cn.smallbun.scaffold.framework.security.aspect;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import cn.smallbun.scaffold.framework.security.domain.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 用户登录切面
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/4/29 11:43
 */
@Aspect
@Component
public class LoginUserAspect {

    /**
     * 这里主要是判断是否为全局最高用户
     *
     * @param pjp {@link JoinPoint}
     */
    @Around("execution(* org.springframework.security.core.userdetails.UserDetailsService.*loadUserByUsername(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // id
        String id = security.getId();
        // username
        String username = security.getUsername();
        // password
        String password = security.getPassword();
        // 如果等于全局用户
        if (pjp.getArgs()[0].toString().equals(username)) {
            return new User(username, password, id, true, true, true, true);
        }
        return pjp.proceed();
    }

    /**
     * SmallBunProperties
     */
    private final SmallBunProperties.Security security;

    public LoginUserAspect(SmallBunProperties properties) {
        this.security = properties.getSecurity();
    }

}
