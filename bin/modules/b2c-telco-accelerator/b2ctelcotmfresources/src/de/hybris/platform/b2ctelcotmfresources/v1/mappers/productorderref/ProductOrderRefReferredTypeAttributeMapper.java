/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfresources.v1.mappers.productorderref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfresources.constants.B2ctelcotmfresourcesConstants;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.ProductOrderRef;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link OrderModel} and
 * {@link ProductOrderRef}
 *
 * @since 2105
 */
public class ProductOrderRefReferredTypeAttributeMapper extends TmaAttributeMapper<OrderModel, ProductOrderRef>
{
	@Override
	public void populateTargetAttributeFromSource(final OrderModel source, final ProductOrderRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setAtreferredType(Config.getParameter(B2ctelcotmfresourcesConstants.TMA_PRODUCT_ORDER_DEFAULT_REFERRED));
		}
	}
}
