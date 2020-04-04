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
package cn.taketoday.web.view.template;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.taketoday.context.Ordered;
import cn.taketoday.context.annotation.Autowired;
import cn.taketoday.context.annotation.MissingBean;
import cn.taketoday.context.annotation.Order;
import cn.taketoday.context.annotation.Props;
import cn.taketoday.context.annotation.condition.ConditionalOnClass;
import cn.taketoday.context.utils.Assert;
import cn.taketoday.context.utils.StringUtils;
import cn.taketoday.web.Constant;
import cn.taketoday.web.RequestContext;
import cn.taketoday.web.ServletContextAware;
import cn.taketoday.web.WebApplicationContext;
import cn.taketoday.web.config.WebMvcConfiguration;
import cn.taketoday.web.servlet.WebServletApplicationContext;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.ext.jsp.TaglibFactory.MetaInfTldSource;
import freemarker.ext.servlet.AllHttpScopesHashModel;
import freemarker.ext.servlet.FreemarkerServlet;
import freemarker.ext.servlet.HttpRequestHashModel;
import freemarker.ext.servlet.HttpRequestParametersHashModel;
import freemarker.ext.servlet.HttpSessionHashModel;
import freemarker.ext.servlet.ServletContextHashModel;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateHashModel;

/**
 * @author TODAY <br>
 *         2018-06-26 19:16:46
 */
@Props(prefix = "web.mvc.view.")
@Order(Ordered.LOWEST_PRECEDENCE - 100)
@MissingBean(type = TemplateViewResolver.class)
@ConditionalOnClass({ Constant.ENV_SERVLET, "freemarker.template.Configuration" })
public class FreeMarkerTemplateViewResolver
        extends AbstractFreeMarkerTemplateViewResolver implements WebMvcConfiguration, ServletContextAware {

    private TaglibFactory taglibFactory;
    private ServletContext servletContext;
    private ServletContextHashModel applicationModel;
    private static final String ATTR_SESSION_MODEL = ".freemarker.Session";

    private List/*<String>*/ classpathTlds;
    private List/*<MetaInfTldSource>*/ metaInfTldSources;

    public FreeMarkerTemplateViewResolver() {}

    public FreeMarkerTemplateViewResolver(WebServletApplicationContext context) {
        this(null, null, null, context);
    }

    public FreeMarkerTemplateViewResolver(
            @Autowired(required = false) ObjectWrapper wrapper,
            @Autowired(required = false) Configuration configuration,
            @Autowired(required = false) TaglibFactory taglibFactory,
            @Autowired(required = true) WebServletApplicationContext context)//
    {
        setObjectWrapper(wrapper);
        setConfiguration(configuration);

        setServletContext(context.getServletContext());
        this.taglibFactory = taglibFactory != null ? taglibFactory : new TaglibFactory(this.servletContext);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T> TemplateLoader createTemplateLoader(List<T> loaders) {

        if (loaders.isEmpty()) {
            if (StringUtils.isNotEmpty(prefix) && prefix.startsWith("/WEB-INF/")) {// prefix -> /WEB-INF/..
                return new WebappTemplateLoader(servletContext, prefix);
            }
            return new DefaultResourceTemplateLoader(prefix, suffix, cacheSize);
        }
        return new CompositeTemplateLoader((Collection<TemplateLoader>) loaders, cacheSize);
    }

    /**
     * Create Model Attributes.
     * 
     * @param context
     *            Current request context
     * @return {@link TemplateHashModel}
     */
    @Override
    protected TemplateHashModel createModel(final RequestContext context) {

        final ObjectWrapper wrapper = getObjectWrapper();
        final HttpServletRequest request = context.nativeRequest();
        
        final AllHttpScopesHashModel ret = new AllHttpScopesHashModel(wrapper, servletContext, request);
        ret.putUnlistedModel(FreemarkerServlet.KEY_JSP_TAGLIBS, getTaglibFactory());
        ret.putUnlistedModel(FreemarkerServlet.KEY_APPLICATION, getApplicationModel());
        // Create hash model wrapper for request
        ret.putUnlistedModel(FreemarkerServlet.KEY_REQUEST, new HttpRequestHashModel(request, wrapper));
        ret.putUnlistedModel(FreemarkerServlet.KEY_REQUEST_PARAMETERS, new HttpRequestParametersHashModel(request));
        // Create hash model wrapper for session

        HttpSessionHashModel sessionModel;
        HttpSession session = request.getSession(false);
        if (session != null) {
            sessionModel = (HttpSessionHashModel) session.getAttribute(ATTR_SESSION_MODEL);
            if (sessionModel == null) {
                sessionModel = new HttpSessionHashModel(session, wrapper);
                initializeSessionAndInstallModel(session, context, sessionModel);
            }
        }
        else {
            sessionModel = new HttpSessionHashModel(session, wrapper);
        }
        ret.putUnlistedModel(FreemarkerServlet.KEY_SESSION, sessionModel);
        return ret;
    }

    protected void initializeSessionAndInstallModel(final HttpSession session,
                                                    final RequestContext context,
                                                    final HttpSessionHashModel sessionModel) {
        session.setAttribute(ATTR_SESSION_MODEL, sessionModel);
        initializeSession(context);
    }

    /**
     * Called when servlet detects in a request processing that session-global (that
     * is, HttpSession-specific) attributes are not yet set. This is a generic hook
     * you might use in subclasses to perform a specific action on first request in
     * the session. By default it does nothing. It is only invoked on newly created
     * sessions; it's not invoked when a replicated session is reinstantiated in
     * another servlet container.
     * 
     * @param context
     *            the actual HTTP request context
     */
    protected void initializeSession(final RequestContext context) {}

    @Override
    public void initFreeMarker(WebApplicationContext context, Properties settings) {
        super.initFreeMarker(context, settings);
        classpathTlds = createDefaultClassPathTlds();
        metaInfTldSources = createDefaultMetaInfTldSources();
    }

    /**
     * Creates the default of the {@value #INIT_PARAM_META_INF_TLD_LOCATIONS}
     * init-param; if this init-param is specified, it will completelly
     * <em>replace</em> the default value.
     * 
     * <p>
     * The implementation in {@link FreemarkerServlet} returns
     * {@link TaglibFactory#DEFAULT_META_INF_TLD_SOURCES}.
     * 
     * @return A {@link List} of {@link MetaInfTldSource}-s; not {@code null}.
     * 
     * @since 2.3.7
     */
    protected List createDefaultMetaInfTldSources() {
        return TaglibFactory.DEFAULT_META_INF_TLD_SOURCES;
    }

    /**
     * Creates the default of the {@value #INIT_PARAM_CLASSPATH_TLDS} init-param; if
     * this init-param is specified, it will be appended <em>after</em> the default,
     * not replace it.
     * 
     * <p>
     * The implementation in {@link FreemarkerServlet} returns
     * {@link TaglibFactory#DEFAULT_CLASSPATH_TLDS}.
     * 
     * @return A {@link List} of {@link String}-s; not {@code null}.
     * 
     * @since 2.3.7
     */
    protected List createDefaultClassPathTlds() {
        return TaglibFactory.DEFAULT_CLASSPATH_TLDS;
    }

    public TaglibFactory getTaglibFactory() {
        if (taglibFactory != null) {
            return taglibFactory;
        }

        TaglibFactory taglibFactory = new TaglibFactory(getServletContext());
        taglibFactory.setObjectWrapper(getObjectWrapper());
        if (metaInfTldSources != null) {
            taglibFactory.setMetaInfTldSources(metaInfTldSources);
        }
        if (classpathTlds != null) {
            taglibFactory.setClasspathTlds(classpathTlds);
        }
        return this.taglibFactory = taglibFactory;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public ServletContextHashModel getApplicationModel() {
        return applicationModel;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        Assert.notNull(servletContext, "servletContext must not be null");

        this.servletContext = servletContext;
        // Create hash model wrapper for servlet context (the application)
        this.applicationModel = new ServletContextHashModel(servletContext, getObjectWrapper());
    }

}
