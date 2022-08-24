/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productorder;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOrder;
import de.hybris.platform.commercefacades.order.data.OrderData;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link OrderData} and {@link ProductOrder}
 *
 * @since 1907
 */
public class ProductOrderHrefAttributeMapper extends TmaAttributeMapper<OrderData, ProductOrder>
{
	@Override
	public void populateTargetAttributeFromSource(final OrderData source, final ProductOrder target,
			final MappingContext context)
	{
		target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_ORDER_API_URL + source.getCode());
	}
}
