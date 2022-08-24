/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart.entries;



import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps BPO code between {@link OrderEntryData} and {@link OrderEntryWsDTO}
 *
 * @since 1911
 */
public class TmaCartEntriesRootBpoCodeAttributeMapperTest
{
	@InjectMocks
	private final TmaCartEntriesRootBpoCodeAttributeMapper mapper = new TmaCartEntriesRootBpoCodeAttributeMapper();

	MappingContext context;

	OrderEntryData source;
	OrderEntryWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new OrderEntryData();
		target = new OrderEntryWsDTO();
		final ProductData bpo = new ProductData();
		bpo.setCode("mobileDeal");
		source.setBpo(bpo);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getBpo().getCode(), target.getRootBpoCode());
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

