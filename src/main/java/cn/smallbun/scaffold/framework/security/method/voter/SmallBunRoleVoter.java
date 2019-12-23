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
package cn.smallbun.scaffold.framework.security.method.voter;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import cn.smallbun.scaffold.framework.context.ApplicationContextHelp;
import cn.smallbun.scaffold.framework.security.utils.SecurityUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * SmallBunRoleVoter
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/12/1 13:22
 */
public class SmallBunRoleVoter implements AccessDecisionVoter<Object> {
    // ~ Instance fields
    // ================================================================================================

    private String rolePrefix = "ROLE_";

    // ~ Methods
    // ========================================================================================================

    public String getRolePrefix() {
        return rolePrefix;
    }

    /**
     * Allows the default role prefix of <code>ROLE_</code> to be overridden. May be set
     * to an empty value, although this is usually not desirable.
     *
     * @param rolePrefix the new prefix
     */
    public void setRolePrefix(String rolePrefix) {
        this.rolePrefix = rolePrefix;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return (attribute.getAttribute() != null)
               && attribute.getAttribute().startsWith(getRolePrefix());
    }

    /**
     * This implementation supports any type of class, because it does not query the
     * presented secure object.
     *
     * @param clazz the secure object
     *
     * @return always <code>true</code>
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object object,
                    Collection<ConfigAttribute> attributes) {
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

        if (authentication == null) {
            return ACCESS_DENIED;
        }
        int result = ACCESS_ABSTAIN;
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);

        for (ConfigAttribute attribute : attributes) {
            if (this.supports(attribute)) {
                result = ACCESS_DENIED;

                // Attempt to find a matching granted authority
                for (GrantedAuthority authority : authorities) {
                    if (attribute.getAttribute().equals(authority.getAuthority())) {
                        return ACCESS_GRANTED;
                    }
                }
            }
        }

        return result;
    }

    Collection<? extends GrantedAuthority> extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities();
    }
}
