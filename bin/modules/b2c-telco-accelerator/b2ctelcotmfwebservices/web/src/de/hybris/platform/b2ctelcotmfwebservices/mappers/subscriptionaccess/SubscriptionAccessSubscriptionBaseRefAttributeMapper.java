/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscriptionaccess;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionAccess;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionBaseRef;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;


/**
 * This attribute Mapper class maps data for subscription base attribute between {@link TmaSubscriptionAccessData} and {@link SubscriptionAccess}
 *
 * @since 1907
 */
public class SubscriptionAccessSubscriptionBaseRefAttributeMapper
		extends TmaAttributeMapper<TmaSubscriptionAccessData, SubscriptionAccess>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(TmaSubscriptionAccessData source, SubscriptionAccess target,
			MappingContext context)
	{
		if (source.getSubscriptionBase() != null)
		{
			SubscriptionBaseRef subscriptionBaseRef = getMapperFacade().map(source.getSubscriptionBase(), SubscriptionBaseRef.class);
			target.setSubscriptionBase(subscriptionBaseRef);
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
