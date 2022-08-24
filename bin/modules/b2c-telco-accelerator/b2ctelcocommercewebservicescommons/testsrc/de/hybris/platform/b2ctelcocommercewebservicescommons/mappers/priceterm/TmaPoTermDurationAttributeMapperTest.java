/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.priceterm;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingTermWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.QuantityWsDTO;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionfacades.data.TermOfServiceFrequencyData;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPoTermDurationAttributeMapper}
 *
 * @deprecated Since 1911
 */
@Deprecated(since = "1911", forRemoval= true)
@UnitTest
public class TmaPoTermDurationAttributeMapperTest
{
	@InjectMocks
	private final TmaPoTermDurationAttributeMapper mapper = new TmaPoTermDurationAttributeMapper();

	SubscriptionTermData source;

	MappingContext context;

	ProductOfferingTermWsDTO target;

	final QuantityWsDTO duration = new QuantityWsDTO();

	private static final String SERVICE_FREQUENCY_CODE = "annually";
	private static final int SERVICE_NUMBER = 24;
	private static final Long AMOUNT = 20L;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new SubscriptionTermData();
		target = new ProductOfferingTermWsDTO();
		duration.setAmount(AMOUNT);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final TermOfServiceFrequencyData termOfServiceFrequency = new TermOfServiceFrequencyData();
		termOfServiceFrequency.setCode(SERVICE_FREQUENCY_CODE);
		source.setTermOfServiceFrequency(termOfServiceFrequency);
		source.setTermOfServiceNumber(SERVICE_NUMBER);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertNotNull(target.getDuration());
	}

	@Test
	public void testPopulateSourceAttributeFromTarget()
	{
		target.setDuration(duration);
		source.setTermOfServiceNumber(SERVICE_NUMBER);
		mapper.populateSourceAttributeFromTarget(target, source, context);
		Assert.assertEquals(source.getTermOfServiceNumber(), target.getDuration().getAmount().intValue());
		Assert.assertNotNull(target.getDuration());
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
