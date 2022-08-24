/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfresources.v1.mappers.productofferingpriceentityref;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcotmfresources.constants.B2ctelcotmfresourcesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfresources.v1.dto.EntityRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atSchemaLocation attribute between {@link TmaProductOfferingPriceModel} and
 * {@link EntityRef}
 *
 * @since 2105
 */
public class PopEntityRefSchemaLocationAttributeMapper extends TmaAttributeMapper<TmaProductOfferingPriceModel, EntityRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductOfferingPriceModel source, final EntityRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(B2ctelcotmfresourcesConstants.TMA_API_SCHEMA_V3);
		}
	}
}
