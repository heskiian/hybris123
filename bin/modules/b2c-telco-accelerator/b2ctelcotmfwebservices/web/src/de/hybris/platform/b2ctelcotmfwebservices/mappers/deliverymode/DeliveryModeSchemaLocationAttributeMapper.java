/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.deliverymode;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.DeliveryMode;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schemaLocation attribute between {@link DeliveryModeData} and {@link DeliveryMode}
 *
 * @since 1907
 */
public class DeliveryModeSchemaLocationAttributeMapper extends TmaAttributeMapper<DeliveryModeData, DeliveryMode>
{
	@Override
	public void populateTargetAttributeFromSource(final DeliveryModeData source, final DeliveryMode target,
			final MappingContext context)
	{
		target.setAtschemaLocation(B2ctelcotmfwebservicesConstants.TMA_API_SCHEMA);
	}
}
