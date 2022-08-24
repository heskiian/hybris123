/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.product;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Product;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductCharacteristic;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps product spec characteristic attribute between {@link OrderEntryData} and
 * {@link Product}
 *
 * @since 1911
 */
public class ProductPscvAttributeMapper extends TmaAttributeMapper<OrderEntryData, Product>
{
	private final MapperFacade mapperFacade;

	public ProductPscvAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final Product target, final MappingContext context)
	{
		if (source.getProductSpecCharacteristics() != null)
		{
			final List<ProductCharacteristic> productCharacteristics = source.getProductSpecCharacteristics().stream()
					.map(abstractOrderEntryProductCharacteristic -> getMapperFacade()
							.map(abstractOrderEntryProductCharacteristic, ProductCharacteristic.class, context))
					.collect(Collectors.toList());

			if (CollectionUtils.isNotEmpty(productCharacteristics))
			{
				target.setCharacteristic(productCharacteristics);
			}
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
