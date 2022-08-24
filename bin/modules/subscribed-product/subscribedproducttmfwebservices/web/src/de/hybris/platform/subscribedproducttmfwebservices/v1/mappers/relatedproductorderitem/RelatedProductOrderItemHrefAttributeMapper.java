/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.relatedproductorderitem;

import de.hybris.platform.subscribedproductservices.model.SpiRelatedProductOrderItemModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.RelatedProductOrderItem;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productOrderHref attribute between {@link SpiRelatedProductOrderItemModel} and
 * {@link RelatedProductOrderItem}
 *
 * @since 2105
 */
public class RelatedProductOrderItemHrefAttributeMapper
		extends SpiAttributeMapper<SpiRelatedProductOrderItemModel, RelatedProductOrderItem>
{
	public RelatedProductOrderItemHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiRelatedProductOrderItemModel source,
			final RelatedProductOrderItem target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getProductOrderId()))
		{
			target.setProductOrderHref(
					SubscribedproducttmfwebservicesConstants.RELATED_PRODUCT_ORDER_ITEM_API_URL + source.getProductOrderId());
		}
	}
}
