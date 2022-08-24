/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productorder;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.commercefacades.order.data.OrderData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class  maps data for baseSiteId attribute between {@link OrderData} and {@link ProductOrder}
 *
 * @since 2102
 */
public class ProductOrderBaseSiteIdAttributeMapper extends TmaAttributeMapper<OrderData, ProductOrder>
{
	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final ProductOrder target, MappingContext context)
	{
		if (source.getSite() != null)
		{
			target.setBaseSiteId(source.getSite());
		}
	}
}
