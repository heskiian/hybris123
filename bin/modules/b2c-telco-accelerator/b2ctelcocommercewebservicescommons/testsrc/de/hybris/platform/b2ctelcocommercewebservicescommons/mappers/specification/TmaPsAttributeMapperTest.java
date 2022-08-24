/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.specification;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProdSpecCharValueUseWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecificationWsDTO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaPsAttributeMapper}
 *
 */
@UnitTest
public class TmaPsAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;

	@InjectMocks
	private final TmaPsAttributeMapper mapper = new TmaPsAttributeMapper();

	TmaProductSpecCharacteristicValueUseData source;

	MappingContext context;

	ProdSpecCharValueUseWsDTO target;

	ProductSpecificationWsDTO productSpecificationRef;

	private static final String CODE = "iPhone_8";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new ProdSpecCharValueUseWsDTO();
		productSpecificationRef = new ProductSpecificationWsDTO();
		source = new TmaProductSpecCharacteristicValueUseData();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		source.setName(CODE);
		final TmaProductSpecificationData productSpecificationData = new TmaProductSpecificationData();
		source.setProductSpecification(productSpecificationData);
		Mockito.when(mapperFacade.map(source.getProductSpecification(), ProductSpecificationWsDTO.class, context))
				.thenReturn(productSpecificationRef);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(productSpecificationRef, target.getProductSpecification());
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
