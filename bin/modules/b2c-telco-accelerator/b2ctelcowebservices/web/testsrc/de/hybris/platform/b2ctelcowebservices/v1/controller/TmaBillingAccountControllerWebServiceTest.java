/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v1.controller;

import org.junit.Test;
import org.springframework.http.MediaType;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcowebservices.constants.B2ctelcowebservicesConstants;
import de.hybris.platform.b2ctelcowebservices.dto.TmaBillingAccountRequestWsDto;
import de.hybris.platform.b2ctelcowebservices.dto.TmaBillingAccountWsDto;
import de.hybris.platform.b2ctelcowebservices.v1.util.BaseWebServiceTest;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@NeedsEmbeddedServer(webExtensions = { B2ctelcowebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class TmaBillingAccountControllerWebServiceTest extends BaseWebServiceTest
{
	private static final String BILLING_ACCOUNT_ENDPOINT = VERSION_URL + "/billingAccounts";
	private static final String BILLING_ACCOUNT_ID = "BA-45499";
	private static final String BILLING_ACCOUNT_ID_FOR_DELETE = "BA-24564";
	private static final String PARENT_BILLING_ACCOUNT_ID = "BA-24564";

	@Test
	public void testCreate()
	{
		final TmaBillingAccountRequestWsDto billingAccountRequestWsDto = generateBillingAccountRequestWsDto();

		final Response response = getWsSecuredRequestBuilder()
				.path(BILLING_ACCOUNT_ENDPOINT)
				.build()
				.accept(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE)
				.post(Entity.entity(billingAccountRequestWsDto, MediaType.APPLICATION_JSON_VALUE));

		assertCreateOperationResponseStatus(response);

		final TmaBillingAccountWsDto result = response.readEntity(TmaBillingAccountWsDto.class);
		assertNotNull("Isn't null", result);
		assertTrue("Hasn't the expected billing system id", BILLING_SYSTEM_ID.equals(result.getBillingSystemId()));
		assertTrue("Hasn't the expected billing acccount id", BILLING_ACCOUNT_ID.equals(result.getBillingAccountId()));
	}

	@Test
	public void testDelete()
	{
		final Response response = getWsSecuredRequestBuilder()
				.path(BILLING_ACCOUNT_ENDPOINT)
				.path(BILLING_SYSTEM_ID)
				.path(BILLING_ACCOUNT_ID_FOR_DELETE)
				.build()
				.delete();
		assertDeleteOperationResponseStatus(response);
	}

	private TmaBillingAccountRequestWsDto generateBillingAccountRequestWsDto()
	{
		final TmaBillingAccountRequestWsDto billingAccountRequestWsDto = new TmaBillingAccountRequestWsDto();
		billingAccountRequestWsDto.setBillingSystemId(BILLING_SYSTEM_ID);
		billingAccountRequestWsDto.setBillingAccountId(BILLING_ACCOUNT_ID);
		billingAccountRequestWsDto.setParentBillingAccountId(PARENT_BILLING_ACCOUNT_ID);
		return billingAccountRequestWsDto;
	}
}
