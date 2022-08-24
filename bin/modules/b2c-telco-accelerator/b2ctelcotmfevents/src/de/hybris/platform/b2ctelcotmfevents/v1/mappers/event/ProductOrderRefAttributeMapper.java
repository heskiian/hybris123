/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfevents.v1.mappers.event;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfevents.data.TmaOrderEventPayload;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOrderEvent;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOrderRef;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for event attribute between {@link TmaOrderEventPayload} and
 * {@link ProductOrderEvent}
 *
 * @since 2105
 */
public class ProductOrderRefAttributeMapper extends TmaAttributeMapper<TmaOrderEventPayload, ProductOrderEvent>
{
	private MapperFacade mapperFacade;

	public ProductOrderRefAttributeMapper(final MapperFacade mapperFacade){
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaOrderEventPayload source,
			final ProductOrderEvent target, final MappingContext context)
	{
		if(ObjectUtils.isEmpty(source.getOrder()))
		{
			return;
		}
		target.setProductOrderRef(getMapperFacade().map(source.getOrder(), ProductOrderRef.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
