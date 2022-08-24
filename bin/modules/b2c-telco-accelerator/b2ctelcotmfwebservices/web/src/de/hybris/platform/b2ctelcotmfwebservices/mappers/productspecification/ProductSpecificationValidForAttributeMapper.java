/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productspecification;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecification;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriod;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link TmaProductSpecificationData} and
 * {@link ProductSpecification}
 *
 * @since 2102
 */
public class ProductSpecificationValidForAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecificationData, ProductSpecification>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecificationData source, final ProductSpecification target,
			final MappingContext context)
	{
		final TimePeriod timePeriod = new TimePeriod();
		if (source.getOnlineDate() != null)
		{
			timePeriod.setStartDateTime(source.getOnlineDate());
		}

		if (source.getOfflineDate() != null)
		{
			timePeriod.setEndDateTime(source.getOfflineDate());
		}

		target.setValidFor(timePeriod);
	}
}
