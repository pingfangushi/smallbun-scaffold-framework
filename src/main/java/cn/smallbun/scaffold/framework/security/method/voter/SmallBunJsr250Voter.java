/*
 * smallbun-scaffold-framework - smallbun企业级开发脚手架-核心框架
 * Copyright © 2019 zuoqinggang (qinggang.zuo@gmail.com / 2689170096@qq.com)
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
package cn.smallbun.scaffold.framework.security.method.voter;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import cn.smallbun.scaffold.framework.context.ApplicationContextHelp;
import cn.smallbun.scaffold.framework.security.utils.SecurityUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.annotation.Jsr250SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/12/1 13:23
 */
public class SmallBunJsr250Voter implements AccessDecisionVoter<Object> {

    /**
     * The specified config attribute is supported if its an instance of a
     * {@link Jsr250SecurityConfig}.
     *
     * @param configAttribute The config attribute.
     * @return whether the config attribute is supported.
     */
    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return configAttribute instanceof Jsr250SecurityConfig;
    }

    /**
     * All classes are supported.
     *
     * @param clazz the class.
     * @return true
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    /**
     * Votes according to JSR 250.
     * <p>
     * If no JSR-250 attributes are found, it will abstain, otherwise it will grant or
     * deny access based on the attributes that are found.
     *
     * @param authentication The authentication object.
     * @param object The access object.
     * @param definition The configuration definition.
     * @return The vote.
     */
    @Override
    public int vote(Authentication authentication, Object object,
                    Collection<ConfigAttribute> definition) {

        // customize
        SmallBunProperties properties = ApplicationContextHelp.getBean(SmallBunProperties.class);
        //最高用户
        String username = properties.getSecurity().getUsername();
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            //最高用户
            if (username.equals(SecurityUtils.getCurrentUserLogin().get())) {
                return ACCESS_GRANTED;
            }
        }

        // copy spring security
        boolean jsr250AttributeFound = false;

        for (ConfigAttribute attribute : definition) {
            if (Jsr250SecurityConfig.PERMIT_ALL_ATTRIBUTE.equals(attribute)) {
                return ACCESS_GRANTED;
            }

            if (Jsr250SecurityConfig.DENY_ALL_ATTRIBUTE.equals(attribute)) {
                return ACCESS_DENIED;
            }

            if (supports(attribute)) {
                jsr250AttributeFound = true;
                // Attempt to find a matching granted authority
                for (GrantedAuthority authority : authentication.getAuthorities()) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }

        return jsr250AttributeFound ? ACCESS_DENIED : ACCESS_ABSTAIN;
    }
}
