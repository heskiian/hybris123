/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.relatedorderitem;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.RelatedProductOrderItem;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atreferredType attribute between {@link TmaSubscribedProductData} and
 * {@link RelatedProductOrderItem}
 *
 * @since 2102
 */
public class RelatedProductOrderRefferedTypeAttributeMapper
		extends TmaAttributeMapper<TmaSubscribedProductData, RelatedProductOrderItem>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaSubscribedProductData source, final RelatedProductOrderItem target,
			final MappingContext context)
	{
		if (StringUtils.isNotBlank(source.getOrderNumber()))
		{
			target.setAtreferredType(Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_DEFAULT_REFERRED, StringUtils.EMPTY));
		}
	}
}
