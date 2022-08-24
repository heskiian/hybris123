/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.usagespecificationref;

import de.hybris.platform.b2ctelcofacades.data.TmaProductUsageSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ExternalIdentifier;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.UsageSpecificationRef;

import org.apache.commons.collections4.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for externalIdentifier attribute between {@link TmaProductUsageSpecificationData}
 * and {@link UsageSpecificationRef}
 *
 * @since 2102
 */
public class UsageSpecificationRefExternalIdentifierAttributeMapper
		extends TmaAttributeMapper<TmaProductUsageSpecificationData, UsageSpecificationRef>
{
	private MapperFacade mapperFacade;

	public UsageSpecificationRefExternalIdentifierAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaProductUsageSpecificationData source,
			final UsageSpecificationRef target, final MappingContext context)
	{
		if (CollectionUtils.isNotEmpty(source.getExternalIds()))
		{
			target.setExternalIdentifier(getMapperFacade().mapAsList(source.getExternalIds(), ExternalIdentifier.class, context));
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}