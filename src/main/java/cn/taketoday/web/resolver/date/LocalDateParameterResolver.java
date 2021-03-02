/*
 * Original Author -> 杨海健 (taketoday@foxmail.com) https://taketoday.cn
 * Copyright © TODAY & 2017 - 2021 All Rights Reserved.
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

package cn.taketoday.web.resolver.date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import cn.taketoday.web.handler.MethodParameter;
import cn.taketoday.web.resolver.ParameterResolver;
import cn.taketoday.web.utils.DateUtils;

/**
 * @author TODAY 2021/2/23 20:45
 */
public class LocalDateParameterResolver
        extends AbstractDateParameterResolver implements ParameterResolver {

  @Override
  public boolean supports(MethodParameter parameter) {
    return parameter.is(LocalDate.class);
  }

  @Override
  protected Object resolveInternal(String parameterValue, DateTimeFormatter formatter) {
    final TemporalAccessor temporalAccessor = formatter.parse(parameterValue);
    return DateUtils.ofDate(temporalAccessor);
  }

}