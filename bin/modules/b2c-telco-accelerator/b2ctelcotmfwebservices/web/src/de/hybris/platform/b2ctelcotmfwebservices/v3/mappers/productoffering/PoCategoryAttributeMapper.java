/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.CategoryRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.List;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for category attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoCategoryAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	private MapperFacade mapperFacade;

	public PoCategoryAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (source.getCategories() == null)
		{
			return;
		}

		final List<CategoryRef> categoryRefs = new ArrayList<>();
		source.getCategories().forEach(categoryData ->
		{
			final CategoryRef categoryWsDto = getMapperFacade().map(categoryData, CategoryRef.class, context);
			categoryRefs.add(categoryWsDto);
		});

		target.setCategory(categoryRefs);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
