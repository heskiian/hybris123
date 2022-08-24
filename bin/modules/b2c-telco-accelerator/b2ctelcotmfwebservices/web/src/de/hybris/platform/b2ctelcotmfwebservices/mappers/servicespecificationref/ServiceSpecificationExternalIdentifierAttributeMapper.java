/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.servicespecificationref;

import de.hybris.platform.b2ctelcofacades.data.TmaServiceSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ExternalIdentifier;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ServiceSpecificationRef;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for external identifier attribute between {@link TmaServiceSpecificationData} and {@link ServiceSpecificationRef}
 *
 * @since 2102
 */
public class ServiceSpecificationExternalIdentifierAttributeMapper extends TmaAttributeMapper<TmaServiceSpecificationData,
		ServiceSpecificationRef>
{
	private MapperFacade mapperFacade;

	public ServiceSpecificationExternalIdentifierAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final TmaServiceSpecificationData source, final ServiceSpecificationRef target,
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
