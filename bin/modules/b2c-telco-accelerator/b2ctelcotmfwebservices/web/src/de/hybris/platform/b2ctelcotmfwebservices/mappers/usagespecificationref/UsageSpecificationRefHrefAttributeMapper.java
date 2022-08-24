/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagespecificationref;

import de.hybris.platform.b2ctelcofacades.data.TmaProductUsageSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsageSpecificationRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaProductUsageSpecificationData}
 * and {@link UsageSpecificationRef}
 *
 * @since 2102
 */
public class UsageSpecificationRefHrefAttributeMapper
		extends TmaAttributeMapper<TmaProductUsageSpecificationData, UsageSpecificationRef>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductUsageSpecificationData source,
			final UsageSpecificationRef target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.TMA_USAGE_SPECIFICATION_REF_API_URL + source.getId());
		}
	}
}
