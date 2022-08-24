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
 * This attribute Mapper class maps data for at base type attribute between {@link TmaPricingLogicAlgorithmData} and {@link PricingLogicAlgorithm}
 *
 * @since 2102
 */
public class PlaAtBaseTypeAttributeMapper extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, PricingLogicAlgorithm>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source, final PricingLogicAlgorithm target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtbaseType(target.getClass().getSimpleName());
		}
	}
}
