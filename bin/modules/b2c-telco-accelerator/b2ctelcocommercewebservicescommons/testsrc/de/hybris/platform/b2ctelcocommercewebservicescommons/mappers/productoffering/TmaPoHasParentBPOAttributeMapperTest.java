/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPoHasParentBPOAttributeMapper}
 *
 */
@UnitTest
public class TmaPoHasParentBPOAttributeMapperTest
{
	@InjectMocks
	private final TmaPoHasParentBPOAttributeMapper mapper = new TmaPoHasParentBPOAttributeMapper();

	ProductData source;

	MappingContext context;

	ProductWsDTO target;
	private static final String APPROVAL_STATUS = "approvalStatus";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new ProductWsDTO();
		source = new ProductData();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final ProductData data = new ProductData();
		data.setCode("1234");
		final List<ProductData> datas = new ArrayList<>();
		datas.add(data);
		source.setParents(datas);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertTrue(target.getHasParentBpos());
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
