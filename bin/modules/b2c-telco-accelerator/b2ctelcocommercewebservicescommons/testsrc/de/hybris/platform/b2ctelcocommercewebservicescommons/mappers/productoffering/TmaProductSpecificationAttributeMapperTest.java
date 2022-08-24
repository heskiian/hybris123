/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecificationWsDTO;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

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
 * JUnit Tests for the @{TmaProductSpecificationAttributeMapper}
 *
 */
@UnitTest
public class TmaProductSpecificationAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;

	@InjectMocks
	private final TmaProductSpecificationAttributeMapper mapper = new TmaProductSpecificationAttributeMapper();

	ProductData source;

	ProductWsDTO target;

	MappingContext context;

	private static final String PRODUCT_SPECIFIACTION = "full";

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new ProductWsDTO();
		source = new ProductData();
		final TmaProductSpecificationData productSpecificationData = new TmaProductSpecificationData();
		productSpecificationData.setId(PRODUCT_SPECIFIACTION);
		source.setProductSpecification(productSpecificationData);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final ProductSpecificationWsDTO productSpecificationWsDTO = new ProductSpecificationWsDTO();
		Mockito.when(mapperFacade.map(source.getProductSpecification(), ProductSpecificationWsDTO.class, context))
				.thenReturn(productSpecificationWsDTO);
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(productSpecificationWsDTO, target.getProductSpecification());
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
