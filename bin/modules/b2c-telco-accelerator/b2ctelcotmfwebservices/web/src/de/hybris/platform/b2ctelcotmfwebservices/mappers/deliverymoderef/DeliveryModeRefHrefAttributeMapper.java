/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.deliverymoderef;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.DeliveryModeRef;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for href attribute between {@link DeliveryModeData} and {@link DeliveryModeRef}
 *
 * @since 1911
 */
public class DeliveryModeRefHrefAttributeMapper extends TmaAttributeMapper<DeliveryModeData, DeliveryModeRef>
{

	@Override
	public void populateTargetAttributeFromSource(DeliveryModeData source, DeliveryModeRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.DELIVERY_MODE_API_URL + source.getCode());
		}
	}
}
