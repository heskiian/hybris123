/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v2.config;

import de.hybris.platform.b2ctelcowebservices.commons.SwaggerConfig;

import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@EnableSwagger2
@Component("SwaggerV2")
@ImportResource(
		{ "WEB-INF/config/v2/springmvc-v2-servlet.xml" })
public class SwaggerVersionConfig extends SwaggerConfig
{
	private static final String VERSION_SCOPE = "b2ctelcowebservices.v2.version";

	@Override
	protected String getVersion()
	{
		return getPropertyValue(VERSION_SCOPE);
	}
}
