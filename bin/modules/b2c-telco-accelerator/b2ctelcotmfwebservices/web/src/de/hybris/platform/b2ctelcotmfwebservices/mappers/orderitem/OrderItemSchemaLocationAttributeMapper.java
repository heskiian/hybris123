/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.orderitem;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OrderItem;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link OrderEntryData} and {@link OrderItem}
 *
 * @since 1907
 */
public class OrderItemSchemaLocationAttributeMapper extends TmaAttributeMapper<OrderEntryData, OrderItem>
{
	@Override
	public void populateTargetAttributeFromSource(OrderEntryData source, OrderItem target,
			MappingContext context)
	{
		target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
	}
}
