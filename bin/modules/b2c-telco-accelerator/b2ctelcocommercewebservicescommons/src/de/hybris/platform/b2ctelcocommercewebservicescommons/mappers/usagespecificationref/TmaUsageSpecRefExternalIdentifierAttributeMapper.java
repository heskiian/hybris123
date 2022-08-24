/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.usagespecificationref;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ExternalIdentifierWsDto;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.UsageSpecificationRefWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaProductUsageSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import org.apache.commons.collections4.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for externalIdentifier attribute between {@link TmaProductUsageSpecificationData}
 * and {@link UsageSpecificationRefWsDTO}
 *
 * @since 2102
 */
public class TmaUsageSpecRefExternalIdentifierAttributeMapper
		extends TmaAttributeMapper<TmaProductUsageSpecificationData, UsageSpecificationRefWsDTO>
{
	private MapperFacade mapperFacade;

	public TmaUsageSpecRefExternalIdentifierAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductUsageSpecificationData source,
			final UsageSpecificationRefWsDTO target, final MappingContext context)
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