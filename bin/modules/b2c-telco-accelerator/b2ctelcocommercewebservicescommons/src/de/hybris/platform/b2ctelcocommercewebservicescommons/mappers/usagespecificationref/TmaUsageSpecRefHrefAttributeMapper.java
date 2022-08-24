/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagespecificationref;

import de.hybris.platform.b2ctelcocommercewebservicescommons.constants.B2ctelcocommercewebservicescommonsConstants;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsageSpecificationRefWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaProductUsageSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaProductUsageSpecificationData}
 * and {@link UsageSpecificationRefWsDTO}
 *
 * @since 2102
 */
public class TmaUsageSpecRefHrefAttributeMapper
		extends TmaAttributeMapper<TmaProductUsageSpecificationData, UsageSpecificationRefWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductUsageSpecificationData source,
			final UsageSpecificationRefWsDTO target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(B2ctelcocommercewebservicescommonsConstants.TMA_USAGE_SPECIFICATION_API_URL + source.getId());
		}
	}
}