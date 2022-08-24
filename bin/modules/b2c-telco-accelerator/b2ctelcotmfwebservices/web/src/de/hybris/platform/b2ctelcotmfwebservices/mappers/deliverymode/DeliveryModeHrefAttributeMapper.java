/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.deliverymode;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.DeliveryMode;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link DeliveryModeData} and {@link DeliveryMode}
 *
 * @since 1907
 */
public class DeliveryModeHrefAttributeMapper extends TmaAttributeMapper<DeliveryModeData, DeliveryMode>
{
	@Override
	public void populateTargetAttributeFromSource(DeliveryModeData source, DeliveryMode target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.DELIVERY_MODE_API_URL + source.getCode());
		}
	}
}
