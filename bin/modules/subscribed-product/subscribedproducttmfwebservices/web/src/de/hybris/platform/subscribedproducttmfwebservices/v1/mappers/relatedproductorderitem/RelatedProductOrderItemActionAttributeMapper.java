/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.relatedproductorderitem;

import de.hybris.platform.subscribedproductservices.enums.SpiOrderItemActionType;
import de.hybris.platform.subscribedproductservices.model.SpiRelatedProductOrderItemModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.RelatedProductOrderItem;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for orderItemAction attribute between {@link SpiRelatedProductOrderItemModel} and
 * {@link RelatedProductOrderItem}
 *
 * @since 2105
 */
public class RelatedProductOrderItemActionAttributeMapper
		extends SpiAttributeMapper<SpiRelatedProductOrderItemModel, RelatedProductOrderItem>
{
	public RelatedProductOrderItemActionAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiRelatedProductOrderItemModel source,
			final RelatedProductOrderItem target, final MappingContext context)
	{
		if (source.getOrderItemAction() == null)
		{
			return;
		}

		target.setOrderItemAction(source.getOrderItemAction().getCode());
	}

	@Override
	public void populateSourceAttributeFromTarget(final RelatedProductOrderItem target,
			final SpiRelatedProductOrderItemModel source, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getOrderItemAction()))
		{
			source.setOrderItemAction(SpiOrderItemActionType.valueOf(target.getOrderItemAction()));
		}
	}
}
