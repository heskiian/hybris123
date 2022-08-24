/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.promotionref;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PromotionRef;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at referred type attribute between {@link PromotionResultData} and{@link PromotionRef}
 *
 * @since 1911
 */
public class PromotionRefAtReferredTypeAttributeMapper extends TmaAttributeMapper<PromotionResultData, PromotionRef>
{
	@Override
	public void populateTargetAttributeFromSource(PromotionResultData source, PromotionRef target, MappingContext context)
	{
		if (source.getPromotionData() != null && StringUtils.isNotEmpty(source.getPromotionData().getCode()))
		{
			target.setAtreferredType(Config.getParameter(B2ctelcotmfwebservicesConstants.PROMOTION_REF_API_REFERRED_TYPE));
		}
	}
}
