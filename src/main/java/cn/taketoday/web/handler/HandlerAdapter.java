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
package cn.taketoday.web.handler;

import cn.taketoday.web.RequestContext;

/**
 * @author TODAY <br>
 *         2019-12-08 20:23
 */
public interface HandlerAdapter {

    /**
     * Given a handler instance, return whether or not this
     * {@code RequestHandlerAdapter} can support it. Typical RequestHandlerAdapters
     * will base the decision on the handler type. RequestHandlerAdapters will
     * usually only support one handler type each.
     * <p>
     * A typical implementation:
     * <p>
     * {@code
     * return (handler instanceof MyHandler);
     * }
     * 
     * @param handler
     *            handler object to check
     * @return whether or not this object can use the given handler
     */
    boolean supports(Object handler);

    /**
     * Use the given handler to handle this request. The workflow that is required
     * may vary widely.
     * 
     * @param context
     *            current HTTP request context
     * @param handler
     *            handler to use. This object must have previously been passed to
     *            the {@code supports} method of this interface, which must have
     *            returned {@code true}.
     * @throws Throwable
     *             in case of errors
     * @return a object with the name of the view and the required model data, or
     *         {@code null} if the request has been handled directly
     */
    Object handle(RequestContext context, Object handler) throws Throwable;

    /**
     * Same contract as for HttpServlet's {@code getLastModified} method. Can simply
     * return -1 if there's no support in the handler class.
     * 
     * @param request
     *            current HTTP request
     * @param handler
     *            handler to use
     * @return the lastModified value for the given handler
     * @see LastModified#getLastModified
     */
    long getLastModified(RequestContext context, Object handler);

//    
//    /**
//     * Get Etag
//     *
//     * @return Etag
//     */
//    String getETag(RequestContext context, Object handler);
}