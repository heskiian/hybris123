/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproductservices.services.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.subscribedproductservices.model.SpiProductComponentModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;


/**
 * IntegrationTest of {@link SpiGenericService}.
 *
 * @since 2111
 */
@IntegrationTest
public class DefaultSpiGenericServiceIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String PRODUCT_ID = "spiProductId";

	@Resource
	private SpiGenericService spiGenericService;


	@Test
	public void testCreateAndSaveProduct()
	{
		final SpiProductModel productComponentModel = (SpiProductModel) getSpiGenericService()
				.createItem(SpiProductComponentModel.class);
		productComponentModel.setId(PRODUCT_ID);
		getSpiGenericService().saveItem(productComponentModel);

		final SpiProductModel retrievedProductComponentModel = (SpiProductModel) getSpiGenericService()
				.getItem(SpiProductModel._TYPECODE, PRODUCT_ID);
		Assert.assertEquals(PRODUCT_ID, retrievedProductComponentModel.getId());
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}
}
