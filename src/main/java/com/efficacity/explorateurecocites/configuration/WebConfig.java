/*
 * Explorateur Écocités
 * Copyright (C) 2019 l'État, ministère chargé du logement
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.efficacity.explorateurecocites.configuration;

import com.efficacity.explorateurecocites.utils.filter.handler.FilteringSpecificationArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/img/**")
				.addResourceLocations("classpath:/static/img/").setCacheControl(
				CacheControl.maxAge(1, TimeUnit.HOURS)
		);
		registry.addResourceHandler("/css/**")
				.addResourceLocations("classpath:/static/css/").setCacheControl(
				CacheControl.maxAge(1, TimeUnit.HOURS)
		);
		registry.addResourceHandler("/fonts/**")
				.addResourceLocations("classpath:/static/fonts/").setCacheControl(
				CacheControl.maxAge(1, TimeUnit.HOURS)
		);
	}

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
	argumentResolvers.add(new FilteringSpecificationArgumentResolver());
  }
}
