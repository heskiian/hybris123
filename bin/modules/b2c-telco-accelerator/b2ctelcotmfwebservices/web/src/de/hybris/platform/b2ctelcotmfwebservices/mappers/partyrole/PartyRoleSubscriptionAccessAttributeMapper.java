/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.partyrole;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PartyRole;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionAccess;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

import org.springframework.beans.factory.annotation.Required;


/**
 * This attribute Mapper class maps data for subscription access attribute between {@link TmaSubscriptionAccessData} and {@link PartyRole}
 *
 * @since 1907
 */
public class PartyRoleSubscriptionAccessAttributeMapper extends TmaAttributeMapper<TmaSubscriptionAccessData, PartyRole>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(TmaSubscriptionAccessData source, PartyRole target, MappingContext context)
	{
		SubscriptionAccess subscriptionAccesses = getMapperFacade().map(source, SubscriptionAccess.class);

		target.addSubscriptionAccessItem(subscriptionAccesses);
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
