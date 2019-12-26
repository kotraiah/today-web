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
package cn.taketoday.web.view;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import cn.taketoday.context.exception.ConfigurationException;
import cn.taketoday.context.io.Resource;
import cn.taketoday.context.utils.ResourceUtils;
import cn.taketoday.context.utils.StringUtils;
import cn.taketoday.web.Constant;
import cn.taketoday.web.MessageConverter;
import cn.taketoday.web.RequestContext;
import cn.taketoday.web.ui.ModelAndView;
import cn.taketoday.web.ui.RedirectModel;
import cn.taketoday.web.utils.WebUtils;
import cn.taketoday.web.view.template.TemplateViewResolver;

/**
 * @author TODAY <br>
 *         2019-07-14 10:47
 */
public abstract class AbstractResultHandler implements ResultHandler, RuntimeResultHandler {

    private int downloadFileBuf;
    /** view resolver **/
    private MessageConverter messageConverter;
    /** Template view resolver */
    private TemplateViewResolver templateViewResolver;

    public AbstractResultHandler() {

    }

    public AbstractResultHandler(TemplateViewResolver viewResolver, MessageConverter messageConverter, int downloadFileBuf) {
        setTemplateViewResolver(viewResolver);
        setMessageConverter(messageConverter);
        setDownloadFileBufferSize(downloadFileBuf);
    }

    @Override
    public boolean supportsResult(Object result) {
        return false;
    }

    public void handleObject(final RequestContext requestContext, final Object view) throws Throwable {

        if (view instanceof String) {
            handleTemplateView((String) view, requestContext);
        }
        else if (view instanceof File) {
            downloadFile(requestContext, ResourceUtils.getResource((File) view));
        }
        else if (view instanceof Resource) {
            downloadFile(requestContext, (Resource) view);
        }
        else if (view instanceof ModelAndView) {
            resolveModelAndView(requestContext, (ModelAndView) view);
        }
        else if (view instanceof RenderedImage) {
            handleImageView((RenderedImage) view, requestContext);
        }
        else {
            obtainMessageConverter().write(requestContext, view);
        }
    }

    /**
     * Resolve {@link ModelAndView} return type
     * 
     * @since 2.3.3
     */
    public void resolveModelAndView(final RequestContext context, final ModelAndView modelAndView) throws Throwable {
        if (modelAndView.hasView()) {
            handleObject(context, modelAndView.getView());
        }
    }

    /**
     * Download file to client.
     *
     * @param request
     *            Current request context
     * @param download
     *            {@link Resource} to download
     * @param bufferSize
     *            Download buffer size
     * @since 2.1.x
     */
    public void downloadFile(final RequestContext context, final Resource download) throws IOException {
        WebUtils.downloadFile(context, download, getDownloadFileBufferSize());
    }

    public void handleRedirect(final String redirect, final RequestContext context) throws IOException {

        if (StringUtils.isEmpty(redirect) || redirect.startsWith(Constant.HTTP)) {
            context.redirect(redirect);
        }
        else {
            context.redirect(context.contextPath().concat(redirect));
        }
    }

    public void handleTemplateView(final String resource, final RequestContext requestContext) throws Throwable {

        if (resource.startsWith(Constant.REDIRECT_URL_PREFIX)) {
            handleRedirect(resource.substring(Constant.REDIRECT_URL_PREFIX_LENGTH), requestContext);
        }
        else {
            final RedirectModel redirectModel = requestContext.redirectModel();
            if (redirectModel != null) {
                for (final Entry<String, Object> entry : redirectModel.asMap().entrySet()) {
                    requestContext.attribute(entry.getKey(), entry.getValue());
                }
                requestContext.redirectModel(null);
            }
            getTemplateViewResolver().resolveView(resource, requestContext);
        }
    }

    /**
     * Resolve image
     * 
     * @param requestContext
     *            Current request context
     * @param image
     *            Image instance
     * @throws IOException
     * @since 2.3.3
     */
    public void handleImageView(final RenderedImage image, final RequestContext requestContext) throws IOException {
        // need set content type
        ImageIO.write(image, Constant.IMAGE_PNG, requestContext.getOutputStream());
    }

    public int getDownloadFileBufferSize() {
        return downloadFileBuf;
    }

    public void setDownloadFileBufferSize(int downloadFileBuf) {
        this.downloadFileBuf = downloadFileBuf;
    }

    public MessageConverter getMessageConverter() {
        return messageConverter;
    }

    public MessageConverter obtainMessageConverter() {
        final MessageConverter messageConverter = getMessageConverter();
        if (messageConverter == null) {
            throw new ConfigurationException("message converter must not be null");
        }
        return messageConverter;
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public TemplateViewResolver getTemplateViewResolver() {
        return templateViewResolver;
    }

    public void setTemplateViewResolver(TemplateViewResolver templateViewResolver) {
        this.templateViewResolver = templateViewResolver;
    }

}