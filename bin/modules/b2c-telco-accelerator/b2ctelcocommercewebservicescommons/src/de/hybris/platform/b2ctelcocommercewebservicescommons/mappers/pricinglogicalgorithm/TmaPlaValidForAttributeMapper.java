/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.pricinglogicalgorithm;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PricingLogicAlgorithmWsDto;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TimePeriodWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link TmaPricingLogicAlgorithmData} and {@link PricingLogicAlgorithmWsDto}
 *
 * @since 2102
 */
public class TmaPlaValidForAttributeMapper extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, PricingLogicAlgorithmWsDto>
{
	private MapperFacade mapperFacade;

	public TmaPlaValidForAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source,
			final PricingLogicAlgorithmWsDto target, final MappingContext context)
	{
		if (source.getOnlineDate() != null || source.getOfflineDate() != null)
		{
			target.setValidFor(getMapperFacade().map(source, TimePeriodWsDTO.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

