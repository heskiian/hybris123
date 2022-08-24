/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.v1.util;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.junit.Assert.assertTrue;

import de.hybris.platform.b2ctelcowebservices.constants.B2ctelcowebservicesConstants;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.webservicescommons.testsupport.client.WsSecuredRequestBuilder;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;


public class BaseWebServiceTest extends ServicelayerTest
{

	private static final String OAUTH_CLIENT_ID = "trusted_client";
	private static final String OAUTH_CLIENT_PASS = "secret";
	protected static final String VERSION_URL = "/v1";
	protected static final String BILLING_SYSTEM_ID = "IN";

	@Before
	public void setUpTestData() throws ImpExException
	{
		importCsv("/b2ctelcowebservices/test/essentialTestData.impex", "utf-8");
	}

	@Test
	public void testB2ctelcowebservices()
	{
		final boolean testTrue = true;
		assertTrue("true is not true", testTrue);
	}

	/**
	 * Returns a new authenticated request to web services.
	 *
	 * @return authenticated request
	 */
	protected WsSecuredRequestBuilder getWsSecuredRequestBuilder()
	{
		return new WsSecuredRequestBuilder().extensionName(B2ctelcowebservicesConstants.EXTENSIONNAME)
				.client(OAUTH_CLIENT_ID, OAUTH_CLIENT_PASS).grantClientCredentials();
	}

	protected void assertCreateOperationResponseStatus(final Response response)
	{
		assertResponse(Response.Status.CREATED, response);
	}

	protected void assertUpdateOperationResponseStatus(final Response response)
	{
		assertResponse(Response.Status.OK, response);
	}

	protected void assertDeleteOperationResponseStatus(final Response response)
	{
		assertResponse(Response.Status.NO_CONTENT, response);
	}
}
