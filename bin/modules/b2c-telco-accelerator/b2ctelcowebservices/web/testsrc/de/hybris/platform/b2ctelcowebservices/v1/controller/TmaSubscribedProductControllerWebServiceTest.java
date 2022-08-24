/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v1.controller;

import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcowebservices.constants.B2ctelcowebservicesConstants;
import de.hybris.platform.b2ctelcowebservices.dto.TmaSubscribedProductWsDto;
import de.hybris.platform.b2ctelcowebservices.dto.UpdatableTmaSubscribedProductWsDto;
import de.hybris.platform.b2ctelcowebservices.v1.util.BaseWebServiceTest;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import static de.hybris.platform.b2ctelcowebservices.v1.util.TmaSubscribedProductTestDataSetup.generateTmaSubscribedProductWsDto;
import static de.hybris.platform.b2ctelcowebservices.v1.util.TmaSubscribedProductTestDataSetup.generateUpdatableTmaSubscribedProductWsDto;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@NeedsEmbeddedServer(webExtensions = { B2ctelcowebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class TmaSubscribedProductControllerWebServiceTest extends BaseWebServiceTest
{
	private static final String SERVICE_ENDPOINT = VERSION_URL + "/subscribedProducts";
	private static final String UPDATED_SERVICE_NAME = "UpdatedName";
	private static final String EXISTING_BILLING_SUBSCRIPTION_ID = "RO_0747690826";

	@Test
	public void createSubscribedProduct()
	{
		final TmaSubscribedProductWsDto subscribedProduct = generateTmaSubscribedProductWsDto(new TmaSubscribedProductWsDto());

		final Response response = getWsSecuredRequestBuilder()
				.path(SERVICE_ENDPOINT)
				.build()
				.post(Entity.entity(subscribedProduct, MediaType.APPLICATION_JSON));

		assertCreateOperationResponseStatus(response);
		final TmaSubscribedProductWsDto result = response.readEntity(TmaSubscribedProductWsDto.class);
		assertNotNull(result);
	}

	@Test
	public void updateSubscribedProduct()
	{
		final UpdatableTmaSubscribedProductWsDto subscribedProduct = generateUpdatableTmaSubscribedProductWsDto(
				new UpdatableTmaSubscribedProductWsDto());
		subscribedProduct.setName(UPDATED_SERVICE_NAME);

		final Response response = getWsSecuredRequestBuilder()
				.path(SERVICE_ENDPOINT)
				.path(BILLING_SYSTEM_ID)
				.path(EXISTING_BILLING_SUBSCRIPTION_ID)
				.build()
				.put(Entity.entity(subscribedProduct, MediaType.APPLICATION_JSON));

		assertUpdateOperationResponseStatus(response);
		final TmaSubscribedProductWsDto result = response.readEntity(TmaSubscribedProductWsDto.class);
		assertNotNull(result);
		assertEquals(UPDATED_SERVICE_NAME, result.getName());
	}

	@Test
	public void deleteSubscribedProduct()
	{
		final Response response = getWsSecuredRequestBuilder()
				.path(SERVICE_ENDPOINT)
				.path(BILLING_SYSTEM_ID)
				.path(EXISTING_BILLING_SUBSCRIPTION_ID)
				.build()
				.delete();

		assertDeleteOperationResponseStatus(response);
	}

}
