/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.config;

import de.hybris.platform.billingaccounttmfwebservices.config.BaSwaggerConfig;

import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Version Configuration for billingaccounttmfwebservices extension
 *
 * @since 2105
 */
@EnableSwagger2
@Component("SwaggerV1")
@ImportResource(
		{ "WEB-INF/config/v1/springmvc-v1-servlet.xml" })
public class BaSwaggerVersionConfig extends BaSwaggerConfig
{
	private static final String VERSION_SCOPE = "billingaccounttmfwebservices.v1.version";

	@Override
	protected String getVersion()
	{
		return getPropertyValue(VERSION_SCOPE);
	}
}
