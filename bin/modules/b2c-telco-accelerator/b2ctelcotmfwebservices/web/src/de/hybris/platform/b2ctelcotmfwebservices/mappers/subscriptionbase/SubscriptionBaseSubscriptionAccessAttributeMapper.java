/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscriptionbase;

import de.hybris.platform.b2ctelcofacades.data.TmaDetailedSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionAccess;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionBase;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for subscription access attribute between {@link TmaDetailedSubscriptionBaseData} and
 * {@link SubscriptionBase}
 *
 * @since 1907
 */
public class SubscriptionBaseSubscriptionAccessAttributeMapper
		extends TmaAttributeMapper<TmaDetailedSubscriptionBaseData, SubscriptionBase>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(TmaDetailedSubscriptionBaseData source, SubscriptionBase target,
			MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getSubscriptionAccessData()))
		{
			return;
		}

		List<SubscriptionAccess> subscriptionAccessList = new ArrayList<>();
		for (TmaSubscriptionAccessData subscriptionAccessData : source.getSubscriptionAccessData())
		{
			subscriptionAccessList.add(getMapperFacade().map(subscriptionAccessData, SubscriptionAccess.class, context));
		}
		target.setSubscriptionAccess(subscriptionAccessList);

	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
