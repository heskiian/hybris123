/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfresources.v1.mappers.productorderentityref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.EntityRef;
import de.hybris.platform.core.model.order.OrderModel;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for ID attribute between {@link OrderModel} and {@link EntityRef}
 *
 * @since 2105
 */
public class ProductOrderEntityRefIdAttributeMapper extends TmaAttributeMapper<OrderModel, EntityRef>
{
	@Override
	public void populateTargetAttributeFromSource(final OrderModel source, final EntityRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}
}
