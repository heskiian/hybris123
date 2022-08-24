/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.subscribedproduct;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductCharacteristicWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.SubscribedProductWsDTO;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for PSCV attribute between {@link OrderEntryData} and {@link SubscribedProductWsDTO}
 *
 * @since 1911
 */
public class TmaSubscribedProductPscvAttributeMapper extends TmaAttributeMapper<OrderEntryData, SubscribedProductWsDTO>
{
	private final MapperFacade mapperFacade;

	public TmaSubscribedProductPscvAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final OrderEntryData source, final SubscribedProductWsDTO target,
			final MappingContext context)
	{
		if (source.getProductSpecCharacteristics() != null)
		{
			final List<ProductCharacteristicWsDTO> productCharacteristics = source.getProductSpecCharacteristics().stream()
					.map(abstractOrderEntryProductCharacteristic -> getMapperFacade()
							.map(abstractOrderEntryProductCharacteristic, ProductCharacteristicWsDTO.class, context))
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
