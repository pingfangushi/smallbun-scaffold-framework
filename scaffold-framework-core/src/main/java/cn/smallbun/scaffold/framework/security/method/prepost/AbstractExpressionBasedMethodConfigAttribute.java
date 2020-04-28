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
package cn.smallbun.scaffold.framework.security.method.prepost;

import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.util.Assert;

/**
 * Contains both filtering and authorization expression meta-data for Spring-EL based
 * access control.
 * <p>
 * Base class for pre or post-invocation phases of a method invocation.
 * <p>
 * Either filter or authorization expressions may be null, but not both.
 *
 * @author Luke Taylor
 * @since 3.0
 */
abstract class AbstractExpressionBasedMethodConfigAttribute implements ConfigAttribute {
    private final Expression filterExpression;
    private final Expression authorizeExpression;

    /**
     * Parses the supplied expressions as Spring-EL.
     */
    AbstractExpressionBasedMethodConfigAttribute(String filterExpression,
                                                 String authorizeExpression) throws ParseException {
        Assert.isTrue(filterExpression != null || authorizeExpression != null,
            "Filter and authorization Expressions cannot both be null");
        SpelExpressionParser parser = new SpelExpressionParser();
        this.filterExpression = filterExpression == null ? null
            : parser.parseExpression(filterExpression);
        this.authorizeExpression = authorizeExpression == null ? null
            : parser.parseExpression(authorizeExpression);
    }

    AbstractExpressionBasedMethodConfigAttribute(Expression filterExpression,
                                                 Expression authorizeExpression) throws ParseException {
        Assert.isTrue(filterExpression != null || authorizeExpression != null,
            "Filter and authorization Expressions cannot both be null");
        this.filterExpression = filterExpression;
        this.authorizeExpression = authorizeExpression;
    }

    Expression getFilterExpression() {
        return filterExpression;
    }

    Expression getAuthorizeExpression() {
        return authorizeExpression;
    }

    @Override
    public String getAttribute() {
        return null;
    }
}
