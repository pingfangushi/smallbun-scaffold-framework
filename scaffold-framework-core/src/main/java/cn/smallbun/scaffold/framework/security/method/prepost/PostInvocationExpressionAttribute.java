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
import org.springframework.security.access.prepost.PostInvocationAttribute;

/**
 *
 * @author Luke Taylor
 * @since 3.0
 */
class PostInvocationExpressionAttribute extends AbstractExpressionBasedMethodConfigAttribute
                                        implements PostInvocationAttribute {

    PostInvocationExpressionAttribute(String filterExpression,
                                      String authorizeExpression) throws ParseException {
        super(filterExpression, authorizeExpression);
    }

    PostInvocationExpressionAttribute(Expression filterExpression,
                                      Expression authorizeExpression) throws ParseException {
        super(filterExpression, authorizeExpression);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Expression authorize = getAuthorizeExpression();
        Expression filter = getFilterExpression();
        sb.append("[authorize: '")
            .append(authorize == null ? "null" : authorize.getExpressionString());
        sb.append("', filter: '").append(filter == null ? "null" : filter.getExpressionString())
            .append("']");
        return sb.toString();
    }
}
