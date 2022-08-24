/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.promotionref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PromotionRef;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for message attribute between {@link PromotionResultData} and{@link PromotionRef}
 *
 * @since 1911
 */
public class PromotionRefMessageAttributeMapper extends TmaAttributeMapper<PromotionResultData, PromotionRef>
{
	@Override
	public void populateTargetAttributeFromSource(PromotionResultData source, PromotionRef target, MappingContext context)
	{
		if (source.getPromotionData() != null || CollectionUtils.isNotEmpty(source.getPromotionData().getFiredMessages()))
		{
			target.setMessage(source.getDescription());
		}
	}
}
