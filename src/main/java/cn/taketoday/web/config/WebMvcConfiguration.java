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
package cn.taketoday.web.config;

import java.util.Set;

import javax.servlet.ServletRegistration;

import cn.taketoday.web.mapping.ResourceMappingRegistry;
import cn.taketoday.web.multipart.AbstractMultipartResolver;

/**
 * @author TODAY <br>
 *         2019-05-17 17:46
 */
public interface WebMvcConfiguration {

	default void configResourceMappings(ResourceMappingRegistry registry) {

	}

	default void configResourceServletUrlMappings(Set<String> urlMappings) {

	}

	default void configDefaultServlet(ServletRegistration servletRegistration) {

	}

	default void configMultipartResolver(AbstractMultipartResolver multipartResolver) {
	
	}

}
