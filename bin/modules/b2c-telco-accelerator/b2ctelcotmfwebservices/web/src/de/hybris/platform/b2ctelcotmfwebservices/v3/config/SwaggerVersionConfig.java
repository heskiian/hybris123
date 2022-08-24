/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.config;

import de.hybris.platform.b2ctelcotmfwebservices.config.SwaggerConfig;

import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Swagger Version Configuration for b2ctelcotmfwebservices extension
 *
 * @since 2007
 */
@EnableSwagger2
@Component("SwaggerV3")
@ImportResource(
		{ "WEB-INF/config/v3/springmvc-v3-servlet.xml" })
public class SwaggerVersionConfig extends SwaggerConfig
{
	private static final String VERSION_SCOPE = "b2ctelcotmfwebservices.v3.version";

	@Override
	protected String getVersion()
	{
		return getPropertyValue(VERSION_SCOPE);
	}
}

