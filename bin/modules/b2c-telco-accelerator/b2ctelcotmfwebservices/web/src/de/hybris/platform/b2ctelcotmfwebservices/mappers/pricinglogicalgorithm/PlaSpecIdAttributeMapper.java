/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.pricinglogicalgorithm;

import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PricingLogicAlgorithm;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for pla spec id attribute between {@link TmaPricingLogicAlgorithmData} and {@link PricingLogicAlgorithm}
 *
 * @since 2102
 */
public class PlaSpecIdAttributeMapper extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, PricingLogicAlgorithm>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source, final PricingLogicAlgorithm target,
			final MappingContext context)
	{
		if (source.getPricingLogicAlgorithmSpec() != null && StringUtils.isNotEmpty(source.getPricingLogicAlgorithmSpec().getId()))
		{
			target.setPlaSpecId(source.getPricingLogicAlgorithmSpec().getId());
		}
	}
}
