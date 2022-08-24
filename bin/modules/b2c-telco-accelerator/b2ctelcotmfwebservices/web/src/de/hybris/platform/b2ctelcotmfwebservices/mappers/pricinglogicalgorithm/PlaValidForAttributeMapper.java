/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.pricinglogicalgorithm;

import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PricingLogicAlgorithm;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriod;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link TmaPricingLogicAlgorithmData} and {@link PricingLogicAlgorithm}
 *
 * @since 2102
 */
public class PlaValidForAttributeMapper extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, PricingLogicAlgorithm>
{
	private MapperFacade mapperFacade;

	public PlaValidForAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source, final PricingLogicAlgorithm target,
			final MappingContext context)
	{
		if (source.getOnlineDate() != null || source.getOfflineDate() != null)
		{
			target.validFor(getMapperFacade().map(source, TimePeriod.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
