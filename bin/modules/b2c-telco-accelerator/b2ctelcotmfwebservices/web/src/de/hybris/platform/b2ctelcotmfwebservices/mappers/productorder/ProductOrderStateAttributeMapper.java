/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productorder;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.StateType;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.enums.OrderStatus;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;

import java.util.Map;


/**
 * This attribute Mapper class maps data for attype attribute between {@link OrderData} and {@link ProductOrder}
 *
 * @since 1907
 */
public class ProductOrderStateAttributeMapper extends TmaAttributeMapper<OrderData, ProductOrder>
{
	/**
	 * Data source object containing mappings between order statuses defined in DB and states defined at API level
	 */
	private Map<String, StateType> orderStatusMapping;

	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final ProductOrder target,
			final MappingContext context)
	{
		if (source.getStatus() == null) {
			return;
		}

		target.setState(getOrderStatusMapping().get(source.getStatus().getCode()));
	}

	@Override
	public void populateSourceAttributeFromTarget(ProductOrder target, OrderData source,
			MappingContext context)
	{
		StateType orderState = target.getState();
		if (orderState == null) {
			return;
		}

		source.setStatus(OrderStatus.valueOf(orderState.name()));
	}

	public Map<String, StateType> getOrderStatusMapping()
	{
		return orderStatusMapping;
	}

	@Required
	public void setOrderStatusMapping(Map<String, StateType> orderStatusMapping)
	{
		this.orderStatusMapping = orderStatusMapping;
	}
}
