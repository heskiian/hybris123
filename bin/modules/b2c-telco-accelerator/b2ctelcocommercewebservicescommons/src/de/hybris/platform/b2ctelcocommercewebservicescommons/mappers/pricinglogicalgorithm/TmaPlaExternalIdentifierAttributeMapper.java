/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.pricinglogicalgorithm;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ExternalIdentifierWsDto;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PricingLogicAlgorithmWsDto;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for external identifier between {@link TmaPricingLogicAlgorithmData} and {@link PricingLogicAlgorithmWsDto}
 *
 * @since 2102
 */
public class TmaPlaExternalIdentifierAttributeMapper
		extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, PricingLogicAlgorithmWsDto>
{
	private MapperFacade mapperFacade;

	public TmaPlaExternalIdentifierAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source,
			final PricingLogicAlgorithmWsDto target, final MappingContext context)
	{
		if (CollectionUtils.isNotEmpty(source.getExternalIds()))
		{
			target.setExternalIdentifier(
					getMapperFacade().mapAsList(source.getExternalIds(), ExternalIdentifierWsDto.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
