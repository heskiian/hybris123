/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.pricinglogicalgorithm;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PricingLogicAlgorithmWsDto;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for pla spec id between {@link TmaPricingLogicAlgorithmData} and {@link PricingLogicAlgorithmWsDto}
 *
 * @since 2102
 */
public class TmaPlaSpecIdAttributeMapper extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, PricingLogicAlgorithmWsDto>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source,
			final PricingLogicAlgorithmWsDto target, final MappingContext context)
	{
		if (source.getPricingLogicAlgorithmSpec() != null && StringUtils.isNotEmpty(source.getPricingLogicAlgorithmSpec().getId()))
		{
			target.setPlaSpecId(source.getPricingLogicAlgorithmSpec().getId());
		}
	}
}
