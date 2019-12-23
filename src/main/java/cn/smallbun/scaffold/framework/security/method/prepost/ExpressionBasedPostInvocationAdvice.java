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
package cn.smallbun.scaffold.framework.security.method.prepost;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import cn.smallbun.scaffold.framework.context.ApplicationContextHelp;
import cn.smallbun.scaffold.framework.security.utils.SecurityUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PostInvocationAttribute;
import org.springframework.security.access.prepost.PostInvocationAuthorizationAdvice;
import org.springframework.security.core.Authentication;

/**
 *
 * @author Luke Taylor
 * @since 3.0
 */
public class ExpressionBasedPostInvocationAdvice implements PostInvocationAuthorizationAdvice {
    protected final Log                           logger = LogFactory.getLog(getClass());

    private final MethodSecurityExpressionHandler expressionHandler;

    public ExpressionBasedPostInvocationAdvice(MethodSecurityExpressionHandler expressionHandler) {
        this.expressionHandler = expressionHandler;
    }

    @Override
    public Object after(Authentication authentication, MethodInvocation mi,
                        PostInvocationAttribute postAttr,
                        Object returnedObject) throws AccessDeniedException {
        PostInvocationExpressionAttribute pia = (PostInvocationExpressionAttribute) postAttr;
        EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication, mi);
        Expression postFilter = pia.getFilterExpression();
        Expression postAuthorize = pia.getAuthorizeExpression();

        if (postFilter != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Applying PostFilter expression " + postFilter);
            }

            if (returnedObject != null) {
                returnedObject = expressionHandler.filter(returnedObject, postFilter, ctx);
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Return object is null, filtering will be skipped");
                }
            }
        }

        expressionHandler.setReturnObject(returnedObject, ctx);
        // customize
        SmallBunProperties properties = ApplicationContextHelp.getBean(SmallBunProperties.class);
        //最高用户
        String username = properties.getSecurity().getUsername();
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            //最高用户
            if (username.equals(SecurityUtils.getCurrentUserLogin().get())) {
                return returnedObject;
            }
        }

        if (postAuthorize != null && !ExpressionUtils.evaluateAsBoolean(postAuthorize, ctx)) {
            if (logger.isDebugEnabled()) {
                logger.debug("PostAuthorize expression rejected access");
            }
            throw new AccessDeniedException("Access is denied");
        }

        return returnedObject;
    }
}
