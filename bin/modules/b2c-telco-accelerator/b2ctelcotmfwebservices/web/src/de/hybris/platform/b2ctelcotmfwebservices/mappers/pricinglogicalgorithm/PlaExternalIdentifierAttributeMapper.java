/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.pricinglogicalgorithm;

import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ExternalIdentifier;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PricingLogicAlgorithm;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for external identifier attribute between {@link TmaPricingLogicAlgorithmData} and {@link PricingLogicAlgorithm}
 *
 * @since 2102
 */
public class PlaExternalIdentifierAttributeMapper extends TmaAttributeMapper<TmaPricingLogicAlgorithmData, PricingLogicAlgorithm>
{
	private MapperFacade mapperFacade;

	public PlaExternalIdentifierAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaPricingLogicAlgorithmData source, final PricingLogicAlgorithm target,
			final MappingContext context)
	{
		if (CollectionUtils.isNotEmpty(source.getExternalIds()))
		{
			target.externalIdentifier(getMapperFacade().mapAsList(source.getExternalIds(), ExternalIdentifier.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
