/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.config;

import static de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants.AUTHORIZATION_URL;
import static de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants.CLIENT_CREDENTIAL_AUTHORIZATION_NAME;
import static de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants.DOCUMENTATION_DESC_PROPERTY;
import static de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants.DOCUMENTATION_TITLE_PROPERTY;
import static de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants.PASSWORD_AUTHORIZATION_NAME;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;

import com.google.common.collect.ImmutableSet;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.ClientCredentialsGrant;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;


/**
 * Swagger Configuration for b2ctelcotmfwebservices extension
 *
 * @since 1810
 */
public class SwaggerConfig
{
	private static final String PASS_AUTHORIZATION_SCOPE = "b2ctelcotmfwebservices.oauth2.password.scope";
	private static final String CLIENT_CREDENTIAL_AUTHORIZATION_SCOPE = "b2ctelcotmfwebservices.oauth2.clientCredentials.scope";
	private static final String VERSION_SCOPE = "b2ctelcotmfwebservices.default.version";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Bean
	public Docket apiDocumentation()
	{
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().paths(PathSelectors.any()).build()
				.produces(ImmutableSet.of("application/json")).securitySchemes(Arrays.asList(clientCredentialFlow(), passwordFlow()))
				.securityContexts(Collections.singletonList(oauthSecurityContext()));
	}

	protected ApiInfo apiInfo()
	{
		return new ApiInfoBuilder().title(getPropertyValue(DOCUMENTATION_TITLE_PROPERTY))
				.description(getPropertyValue(DOCUMENTATION_DESC_PROPERTY)).version(getVersion()).build();
	}

	protected OAuth passwordFlow()
	{
		final AuthorizationScope authorizationScope = new AuthorizationScope(getPropertyValue(PASS_AUTHORIZATION_SCOPE),
				StringUtils.EMPTY);
		final ResourceOwnerPasswordCredentialsGrant resourceOwnerPasswordCredentialsGrant = new ResourceOwnerPasswordCredentialsGrant(
				AUTHORIZATION_URL);
		return new OAuth(PASSWORD_AUTHORIZATION_NAME, Arrays.asList(authorizationScope),
				Arrays.asList(resourceOwnerPasswordCredentialsGrant));
	}

	protected OAuth clientCredentialFlow()
	{
		final AuthorizationScope authorizationScope = new AuthorizationScope(
				getPropertyValue(CLIENT_CREDENTIAL_AUTHORIZATION_SCOPE), StringUtils.EMPTY);
		final ClientCredentialsGrant clientCredentialsGrant = new ClientCredentialsGrant(AUTHORIZATION_URL);
		return new OAuth(CLIENT_CREDENTIAL_AUTHORIZATION_NAME, Arrays.asList(authorizationScope),
				Arrays.asList(clientCredentialsGrant));
	}

	protected String getPropertyValue(final String propertyName)
	{
		return getConfigurationService().getConfiguration().getString(propertyName);
	}

	protected SecurityContext oauthSecurityContext()
	{
		return SecurityContext.builder().securityReferences(oauthSecurityReferences()).forPaths(PathSelectors.any()).build();
	}

	protected List<SecurityReference> oauthSecurityReferences()
	{
		final AuthorizationScope[] authorizationScopes = {};
		return Arrays.asList(new SecurityReference(PASSWORD_AUTHORIZATION_NAME, authorizationScopes),
				new SecurityReference(CLIENT_CREDENTIAL_AUTHORIZATION_NAME, authorizationScopes));
	}

	protected String getVersion()
	{
		return getPropertyValue(VERSION_SCOPE);
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
