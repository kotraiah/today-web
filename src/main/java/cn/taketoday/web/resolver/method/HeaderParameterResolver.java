/**
 * Original Author -> 杨海健 (taketoday@foxmail.com) https://taketoday.cn
 * Copyright © TODAY & 2017 - 2019 All Rights Reserved.
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

import cn.taketoday.context.annotation.Singleton;
import cn.taketoday.web.RequestContext;
import cn.taketoday.web.annotation.Header;
import cn.taketoday.web.mapping.MethodParameter;

/**
 * @author TODAY <br>
 *         2019-07-13 11:11
 */
@Singleton
public class HeaderParameterResolver extends TypeConverterParameterResolver implements ParameterResolver {

    @Override
    public boolean supports(MethodParameter parameter) {
        return parameter.isAnnotationPresent(Header.class);
    }

    @Override
    protected Object resolveSource(final RequestContext requestContext, final MethodParameter parameter) {
        return requestContext.requestHeader(parameter.getName());
    }
}