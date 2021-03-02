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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.taketoday.context.OrderedSupport;
import cn.taketoday.context.utils.StringUtils;
import cn.taketoday.web.RequestContext;
import cn.taketoday.web.annotation.DateTimeFormat;
import cn.taketoday.web.handler.MethodParameter;
import cn.taketoday.web.resolver.ParameterConversionException;
import cn.taketoday.web.resolver.ParameterResolver;

/**
 * @author TODAY 2021/2/23 20:10
 */
public class DateParameterResolver extends OrderedSupport implements ParameterResolver {

  private String defaultPattern = "yyyy-MM-dd HH:mm:ss";

  @Override
  public boolean supports(MethodParameter parameter) {
    return parameter.is(Date.class);
  }

  @Override
  public Object resolveParameter(RequestContext context, MethodParameter parameter) throws Throwable {
    final String parameterValue = context.parameter(parameter.getName());
    final SimpleDateFormat simpleDateFormat = getFormatter(parameter);

    try {
      return simpleDateFormat.parse(parameterValue);
    }
    catch (ParseException e) {
      throw new ParameterConversionException(parameter, parameterValue, e);
    }
  }

  protected SimpleDateFormat getFormatter(MethodParameter parameter) {
    final DateTimeFormat dateTimeFormat = parameter.getAnnotation(DateTimeFormat.class);
    if (dateTimeFormat != null) {
      final String pattern = dateTimeFormat.value();
      if (StringUtils.isNotEmpty(pattern)) {
        return new SimpleDateFormat(pattern);
      }
    }
    return new SimpleDateFormat(defaultPattern);
  }

  public void setDefaultPattern(String defaultPattern) {
    this.defaultPattern = defaultPattern;
  }

  public String getDefaultPattern() {
    return defaultPattern;
  }
}