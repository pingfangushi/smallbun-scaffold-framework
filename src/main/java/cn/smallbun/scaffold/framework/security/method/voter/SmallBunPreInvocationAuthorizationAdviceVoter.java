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
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.prepost.PreInvocationAttribute;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdvice;
import org.springframework.security.core.Authentication;

import java.util.Collection;

/**
 * 全局voter
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/12/1 12:38
 */
public class SmallBunPreInvocationAuthorizationAdviceVoter implements
                                                           AccessDecisionVoter<MethodInvocation> {
    protected final Log                            logger = LogFactory.getLog(getClass());

    private final PreInvocationAuthorizationAdvice preAdvice;

    public SmallBunPreInvocationAuthorizationAdviceVoter(PreInvocationAuthorizationAdvice pre) {
        this.preAdvice = pre;
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return attribute instanceof PreInvocationAttribute;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return MethodInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public int vote(Authentication authentication, MethodInvocation method,
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

        // Find prefilter and preauth (or combined) attributes
        // if both null, abstain
        // else call advice with them

        PreInvocationAttribute preAttr = findPreInvocationAttribute(attributes);

        if (preAttr == null) {
            // No expression based metadata, so abstain
            return ACCESS_ABSTAIN;
        }

        boolean allowed = preAdvice.before(authentication, method, preAttr);

        return allowed ? ACCESS_GRANTED : ACCESS_DENIED;
    }

    private PreInvocationAttribute findPreInvocationAttribute(Collection<ConfigAttribute> config) {
        for (ConfigAttribute attribute : config) {
            if (attribute instanceof PreInvocationAttribute) {
                return (PreInvocationAttribute) attribute;
            }
        }

        return null;
    }
}