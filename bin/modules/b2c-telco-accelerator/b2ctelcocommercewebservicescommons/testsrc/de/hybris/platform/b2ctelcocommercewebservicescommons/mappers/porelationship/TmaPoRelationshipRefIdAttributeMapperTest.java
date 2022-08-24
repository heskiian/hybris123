/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.porelationship;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PoRelationshipWsDTO;
import de.hybris.platform.commercefacades.product.data.ProductData;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPoRelationshipRefIdAttributeMapper}
 *
 */
@UnitTest
public class TmaPoRelationshipRefIdAttributeMapperTest
{
	@InjectMocks
	private final TmaPoRelationshipRefIdAttributeMapper mapper = new TmaPoRelationshipRefIdAttributeMapper();

	ProductData source;

	MappingContext context;

	PoRelationshipWsDTO target;

	private static final String PRODUCTCODE = "iPhone_8";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new PoRelationshipWsDTO();
		source = new ProductData();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		source.setCode(PRODUCTCODE);
		mapper.populateTargetAttributeFromSource(source, target, context);
		assertEquals(source.getCode(), target.getId());
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
