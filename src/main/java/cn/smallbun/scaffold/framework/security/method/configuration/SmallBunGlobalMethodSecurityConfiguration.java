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
package cn.smallbun.scaffold.framework.security.method.configuration;

import cn.smallbun.scaffold.framework.security.method.prepost.ExpressionBasedPostInvocationAdvice;
import cn.smallbun.scaffold.framework.security.method.voter.SmallBunJsr250Voter;
import cn.smallbun.scaffold.framework.security.method.voter.SmallBunPreInvocationAuthorizationAdviceVoter;
import cn.smallbun.scaffold.framework.security.method.voter.SmallBunRoleVoter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AfterInvocationProvider;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.intercept.AfterInvocationManager;
import org.springframework.security.access.intercept.AfterInvocationProviderManager;
import org.springframework.security.access.method.MethodSecurityMetadataSource;
import org.springframework.security.access.prepost.PostInvocationAdviceProvider;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.core.GrantedAuthorityDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * GlobalMethodSecurityConfig 主要为了封装全局用户
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2019/12/1 12:37
 */
@Configuration(proxyBeanMethods = false)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SmallBunGlobalMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration
                                                       implements BeanFactoryAware {
    private BeanFactory context;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.context = beanFactory;
        super.setBeanFactory(beanFactory);
    }

    /**
     * AccessDecisionManager
     * 访问决策管理器，主要为处理全局用户
     * @return {@link AccessDecisionManager} AccessDecisionManager
     */
    @Override
    protected AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();
        //SmallBunPreInvocationAuthorizationAdviceVoter
        ExpressionBasedPreInvocationAdvice expressionAdvice = new ExpressionBasedPreInvocationAdvice();
        expressionAdvice.setExpressionHandler(getExpressionHandler());
        decisionVoters.add(new SmallBunPreInvocationAuthorizationAdviceVoter(expressionAdvice));
        //jsr250
        decisionVoters.add(new SmallBunJsr250Voter());
        //role
        SmallBunRoleVoter roleVoter = new SmallBunRoleVoter();
        GrantedAuthorityDefaults grantedAuthorityDefaults = getSingleBeanOrNull(
            GrantedAuthorityDefaults.class);
        if (grantedAuthorityDefaults != null) {
            roleVoter.setRolePrefix(grantedAuthorityDefaults.getRolePrefix());
        }
        decisionVoters.add(roleVoter);
        decisionVoters.add(new AuthenticatedVoter());
        return new AffirmativeBased(decisionVoters);
    }

    /**
     * Provide a custom {@link AfterInvocationManager} for the default implementation of
     * {@link #methodSecurityInterceptor(MethodSecurityMetadataSource)}. The default is null
     * if pre post is not enabled. Otherwise, it returns a {@link AfterInvocationProviderManager}.
     *
     * <p>
     * Subclasses should override this method to provide a custom
     * {@link AfterInvocationManager}
     * </p>
     *
     * @return the {@link AfterInvocationManager} to use
     */
    @Override
    protected AfterInvocationManager afterInvocationManager() {
        AfterInvocationProviderManager invocationProviderManager = new AfterInvocationProviderManager();
        // SmallBunExpressionBasedPostInvocationAdvice
        ExpressionBasedPostInvocationAdvice postAdvice = new ExpressionBasedPostInvocationAdvice(
            getExpressionHandler());
        PostInvocationAdviceProvider postInvocationAdviceProvider = new PostInvocationAdviceProvider(
            postAdvice);
        List<AfterInvocationProvider> afterInvocationProviders = new ArrayList<>();
        afterInvocationProviders.add(postInvocationAdviceProvider);
        invocationProviderManager.setProviders(afterInvocationProviders);
        return invocationProviderManager;
    }

    private <T> T getSingleBeanOrNull(Class<T> type) {
        try {
            return context.getBean(type);
        } catch (NoSuchBeanDefinitionException ignored) {
        }
        return null;
    }
}
