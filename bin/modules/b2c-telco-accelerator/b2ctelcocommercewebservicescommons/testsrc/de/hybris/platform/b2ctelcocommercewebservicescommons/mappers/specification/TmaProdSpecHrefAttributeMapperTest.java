/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.specification;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.constants.B2ctelcocommercewebservicescommonsConstants;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecificationWsDTO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaProdSpecHrefAttributeMapper}
 *
 */
@UnitTest
public class TmaProdSpecHrefAttributeMapperTest
{
	@InjectMocks
	private final TmaProdSpecHrefAttributeMapper mapper = new TmaProdSpecHrefAttributeMapper();

	TmaProductSpecificationData source;

	MappingContext context;

	ProductSpecificationWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new ProductSpecificationWsDTO();
		source = new TmaProductSpecificationData();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		source.setId("ID");
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(B2ctelcocommercewebservicescommonsConstants.PRODUCT_SPECIFICATION_API_URL + source.getId(), target.getHref());
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
