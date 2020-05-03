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
package cn.smallbun.scaffold.framework.security.access.vote;

import cn.smallbun.scaffold.framework.configurer.SmallBunProperties;
import cn.smallbun.scaffold.framework.context.ApplicationContextHelp;
import cn.smallbun.scaffold.framework.security.utils.SecurityUtils;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.core.Authentication;

import java.util.Collection;
import java.util.List;

/**
 * AffirmativeBased
 *
 * @author SanLi
 * Created by qinggang.zuo@gmail.com / 2689170096@qq.com on 2020/5/2 21:36
 */
public class AffirmativeBased extends AbstractAccessDecisionManager {

    public AffirmativeBased(List<AccessDecisionVoter<?>> decisionVoters) {
        super(decisionVoters);
    }

    // ~ Methods
    // ========================================================================================================

    /**
     * This concrete implementation simply polls all configured
     * {@link AccessDecisionVoter}s and grants access if any
     * <code>AccessDecisionVoter</code> voted affirmatively. Denies access only if there
     * was a deny vote AND no affirmative votes.
     * <p>
     * If every <code>AccessDecisionVoter</code> abstained from voting, the decision will
     * be based on the {@link #isAllowIfAllAbstainDecisions()} property (defaults to
     * false).
     * </p>
     *
     * @param authentication   the caller invoking the method
     * @param object           the secured object
     * @param configAttributes the configuration attributes associated with the method
     *                         being invoked
     * @throws AccessDeniedException if access is denied
     */
    @Override
    public void decide(Authentication authentication, Object object,
                       Collection<ConfigAttribute> configAttributes) throws AccessDeniedException {
        // global user
        String username = ApplicationContextHelp.getBean(SmallBunProperties.class)
            .getSecurity().getUsername();
        if (SecurityUtils.getCurrentUserLogin().isPresent()) {
            // global user
            if (username.equals(SecurityUtils.getCurrentUserLogin().get())) {
                return;
            }
        }
        int deny = 0;

        for (AccessDecisionVoter voter : getDecisionVoters()) {
            int result = voter.vote(authentication, object, configAttributes);

            if (logger.isDebugEnabled()) {
                logger.debug("Voter: " + voter + ", returned: " + result);
            }

            switch (result) {
                case AccessDecisionVoter.ACCESS_GRANTED:
                    return;

                case AccessDecisionVoter.ACCESS_DENIED:
                    deny++;

                    break;

                default:
                    break;
            }
        }

        if (deny > 0) {
            throw new AccessDeniedException(messages
                .getMessage("AbstractAccessDecisionManager.accessDenied", "Access is denied"));
        }

        // To get this far, every AccessDecisionVoter abstained
        checkAllowIfAllAbstainDecisions();
    }
}
