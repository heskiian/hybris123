/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.config;

import de.hybris.platform.partytmfwebservices.config.SwaggerConfig;

import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Swagger Version Configuration for partytmfwebservices extension
 *
 * @since 2108
 */
@EnableSwagger2
@Component("SwaggerV1")
@ImportResource(
{ "WEB-INF/config/v1/springmvc-v1-servlet.xml" })
public class SwaggerVersion1Config extends SwaggerConfig
{
	private static final String VERSION_SCOPE = "partytmfwebservices.v1.version";

	@Override
	protected String getVersion()
	{
		return getPropertyValue(VERSION_SCOPE);
	}
}

