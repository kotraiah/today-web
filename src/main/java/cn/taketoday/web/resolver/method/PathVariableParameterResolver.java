/**
 * Original Author -> 杨海健 (taketoday@foxmail.com) https://taketoday.cn
 * Copyright © TODAY & 2017 - 2020 All Rights Reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *   
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/]
 */
package cn.taketoday.web.resolver.method;

import cn.taketoday.context.AntPathMatcher;
import cn.taketoday.context.PathMatcher;
import cn.taketoday.context.utils.ConvertUtils;
import cn.taketoday.context.utils.StringUtils;
import cn.taketoday.web.RequestContext;
import cn.taketoday.web.handler.MethodParameter;
import cn.taketoday.web.handler.PathVariableMethodParameter;
import cn.taketoday.web.utils.WebUtils;

/**
 * @author TODAY <br>
 *         2019-07-09 22:49
 */
public class PathVariableParameterResolver implements OrderedParameterResolver {

    private PathMatcher pathMatcher;

    public PathVariableParameterResolver() {
        this(new AntPathMatcher());
    }

    public PathVariableParameterResolver(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

    @Override
    public boolean supports(final MethodParameter parameter) {
        return parameter instanceof PathVariableMethodParameter;
    }

    /**
     * Resolve Path Variable parameter.
     */
    @Override
    public Object resolveParameter(final RequestContext requestContext, final MethodParameter p) throws Throwable {

        try {
            final PathVariableMethodParameter parameter = (PathVariableMethodParameter) p;

            String[] pathVariables = requestContext.pathVariables();
            if (pathVariables == null) {
                String requestURI = StringUtils.decodeUrl(requestContext.requestURI());
                final String[] extractVariables = getPathMatcher().extractVariables(parameter.getPathPattern(), requestURI);
                pathVariables = requestContext.pathVariables(extractVariables);
            }
            return ConvertUtils.convert(pathVariables[parameter.getPathIndex()], parameter.getParameterClass());
        }
        catch (Throwable e) {
            throw WebUtils.newBadRequest("Path variable", p.getName(), e);
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public void setPathMatcher(PathMatcher pathMatcher) {
        this.pathMatcher = pathMatcher;
    }

}
