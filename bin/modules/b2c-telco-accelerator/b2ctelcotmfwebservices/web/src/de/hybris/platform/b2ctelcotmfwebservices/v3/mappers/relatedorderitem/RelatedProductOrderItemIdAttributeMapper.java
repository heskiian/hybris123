/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.relatedorderitem;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.RelatedProductOrderItem;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps orderItemId attribute between {@link TmaSubscribedProductData} and
 * {@link RelatedProductOrderItem}
 *
 * @since 2102
 */
public class RelatedProductOrderItemIdAttributeMapper
		extends TmaAttributeMapper<TmaSubscribedProductData, RelatedProductOrderItem>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaSubscribedProductData source, final RelatedProductOrderItem target,
			final MappingContext context)
	{
		target.setOrderItemId(source.getOrderEntryNumber().toString());
	}
}
