/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.product;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.RelatedProductOrderItem;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOrderItem attribute between {@link TmaSubscribedProductData} and
 * {@link Product}
 *
 * @since 2102
 */
public class ProductRelatedProductOrderAttributeMapper extends TmaAttributeMapper<TmaSubscribedProductData, Product>
{
	private final MapperFacade mapperFacade;

	public ProductRelatedProductOrderAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaSubscribedProductData source, final Product target,
			final MappingContext context)
	{

		if (StringUtils.isBlank(source.getOrderNumber()))
		{
			return;
		}

		final RelatedProductOrderItem productOrder = getMapperFacade().map(source, RelatedProductOrderItem.class, context);

		target.setProductOrderItem(new ArrayList<>(Arrays.asList(productOrder)));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
