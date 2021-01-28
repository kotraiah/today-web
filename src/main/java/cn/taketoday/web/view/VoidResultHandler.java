/**
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
package cn.taketoday.web.view;

import cn.taketoday.web.MessageConverter;
import cn.taketoday.web.RequestContext;
import cn.taketoday.web.handler.HandlerMethod;
import cn.taketoday.web.ui.ModelAndView;
import cn.taketoday.web.view.template.TemplateViewResolver;

/**
 * @author TODAY <br>
 * 2019-07-14 00:53
 */
public class VoidResultHandler extends ModelAndViewResultHandler {

  public VoidResultHandler(TemplateViewResolver viewResolver, MessageConverter messageConverter, int downloadFileBuf) {
    super(viewResolver, messageConverter, downloadFileBuf);
  }

  @Override
  public boolean supports(HandlerMethod handlerMethod) {
    return handlerMethod.is(void.class);
  }

  @Override
  public boolean supportsResult(Object result) {
    return result == null;
  }

  @Override
  public void handleResult(final RequestContext context,
                           final Object handler, final Object result) throws Throwable {

    final ModelAndView modelAndView = context.modelAndView();
    if (modelAndView != null) {
      resolveModelAndView(context, modelAndView);
    }
  }

}
