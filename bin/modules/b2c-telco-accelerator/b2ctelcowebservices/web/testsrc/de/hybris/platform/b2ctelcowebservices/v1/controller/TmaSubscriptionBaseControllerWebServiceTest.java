/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v1.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.hybris.bootstrap.annotations.IntegrationTest;

import de.hybris.platform.b2ctelcowebservices.dto.TmaSubscriptionBaseWsDto;
import de.hybris.platform.b2ctelcowebservices.v1.util.BaseWebServiceTest;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import de.hybris.platform.b2ctelcowebservices.constants.B2ctelcowebservicesConstants;


@NeedsEmbeddedServer(webExtensions = { B2ctelcowebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class TmaSubscriptionBaseControllerWebServiceTest extends BaseWebServiceTest
{
	private static final String SUBSCRIPTION_BASE_ENDPOINT = VERSION_URL + "/subscriptionBases";
	private static final String SUBSCRIPTION_BASE_ID = "testSubscriptionBase_ws2";

	@Test
	public void testCreateSubscriptionBase()
	{

		String subscriberId = "subscriberId";
		String billingAccountId = "BA-24564";
		final TmaSubscriptionBaseWsDto subscriptionBaseWsDto = new TmaSubscriptionBaseWsDto();
		subscriptionBaseWsDto.setSubscriberIdentity(subscriberId);
		subscriptionBaseWsDto.setBillingSystemId(BILLING_SYSTEM_ID);
		subscriptionBaseWsDto.setBillingAccountId(billingAccountId);

		final Response response = getWsSecuredRequestBuilder()
				.path(SUBSCRIPTION_BASE_ENDPOINT)
				.build()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(subscriptionBaseWsDto, MediaType.APPLICATION_JSON));

		assertCreateOperationResponseStatus(response);

		final TmaSubscriptionBaseWsDto result = response.readEntity(TmaSubscriptionBaseWsDto.class);
		assertNotNull(result);
		assertEquals(result.getSubscriberIdentity(), subscriberId);
	}

	@Test
	public void testDeleteSubscriptionBase()
	{

		final Response response = getWsSecuredRequestBuilder()
				.path(SUBSCRIPTION_BASE_ENDPOINT)
				.path(BILLING_SYSTEM_ID)
				.path(SUBSCRIPTION_BASE_ID)
				.build()
				.delete();

		assertDeleteOperationResponseStatus(response);
	}
}



