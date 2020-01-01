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
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package cn.taketoday.web.servlet;

import java.util.Set;

import javax.servlet.ServletContext;

import cn.taketoday.context.StandardApplicationContext;
import cn.taketoday.context.factory.AbstractBeanFactory;
import cn.taketoday.context.factory.StandardBeanFactory;
import cn.taketoday.context.utils.StringUtils;
import cn.taketoday.web.utils.WebUtils;

/**
 * @author TODAY <br>
 *         2018-07-10 1:16:17
 */
public class StandardWebServletApplicationContext extends StandardApplicationContext
        implements WebServletApplicationContext, ConfigurableWebServletApplicationContext {

    /** Servlet context */
    private ServletContext servletContext;

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public StandardWebServletApplicationContext() {
        super();
        WebUtils.setWebApplicationContext(this);
    }

    public StandardWebServletApplicationContext(ServletContext servletContext) {
        this();
        this.servletContext = servletContext;
        loadContext();
    }

    /**
     * @param classes
     *            class set
     * @param servletContext
     * @since 2.3.3
     */
    public StandardWebServletApplicationContext(Set<Class<?>> classes, ServletContext servletContext) {
        this();
        this.servletContext = servletContext;
        loadContext(classes);
    }

    /**
     * @param servletContext
     *            {@link ServletContext}
     * @param properties
     *            properties location
     * @param locations
     *            package locations
     * @since 2.3.3
     */
    public StandardWebServletApplicationContext(ServletContext servletContext, String propertiesLocation, String... locations) {
        this();
        if (StringUtils.isNotEmpty(propertiesLocation)) {
            setPropertiesLocation(propertiesLocation);
        }
        this.servletContext = servletContext;
        loadContext(locations);
    }

    public StandardWebServletApplicationContext(StandardBeanFactory beanFactory) {
        super(beanFactory);
    }

    @Override
    protected StandardBeanFactory createBeanFactory() {
        return new StandardWebServletBeanFactory(this);
    }

    @Override
    protected void postProcessBeanFactory(AbstractBeanFactory beanFactory) {
        // register WebApplicationContext
        registerSingleton(this);

        super.postProcessBeanFactory(beanFactory);
    }

    @Override
    public String getContextPath() {
        return servletContext.getContextPath();
    }

}
