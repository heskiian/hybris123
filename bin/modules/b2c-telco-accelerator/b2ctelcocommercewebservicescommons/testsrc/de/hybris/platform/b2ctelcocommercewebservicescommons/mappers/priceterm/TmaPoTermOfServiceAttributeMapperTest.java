/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.priceterm;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceRenewalData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPoTermOfServiceAttributeMapper}
 */
@UnitTest
public class TmaPoTermOfServiceAttributeMapperTest
{
	@InjectMocks
	private final TmaPoTermOfServiceAttributeMapper mapper = new TmaPoTermOfServiceAttributeMapper();

	MappingContext context;

	SubscriptionTermData source;
	ProductOfferingTermWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new SubscriptionTermData();
		target = new ProductOfferingTermWsDTO();

		final TermOfServiceFrequencyData termOfServiceFrequency = new TermOfServiceFrequencyData();
		final TermOfServiceRenewalData termOfServiceRenewal = new TermOfServiceRenewalData();
		source.setCancellable(true);
		source.setTermOfServiceFrequency(termOfServiceFrequency);
		source.setTermOfServiceNumber(0);
		source.setTermOfServiceRenewal(termOfServiceRenewal);

	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.isCancellable(), target.getCancellable());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullSource()
	{
		mapper.populateTargetAttributeFromSource(null, target, context);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullTarget()
	{
		mapper.populateTargetAttributeFromSource(source, null, context);
	}

}
