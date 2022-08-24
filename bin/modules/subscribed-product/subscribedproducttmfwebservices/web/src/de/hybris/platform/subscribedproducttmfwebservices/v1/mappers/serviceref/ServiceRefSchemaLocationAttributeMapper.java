/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.serviceref;

import de.hybris.platform.subscribedproductservices.model.SpiServiceModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ServiceRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atSchemaLocation attribute between {@link SpiServiceModel} and {@link ServiceRef}
 *
 * @since 2105
 */
public class ServiceRefSchemaLocationAttributeMapper extends SpiAttributeMapper<SpiServiceModel, ServiceRef>
{
	public ServiceRefSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiServiceModel source, final ServiceRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(SubscribedproducttmfwebservicesConstants.SPI_API_SCHEMA);
		}
	}
}
