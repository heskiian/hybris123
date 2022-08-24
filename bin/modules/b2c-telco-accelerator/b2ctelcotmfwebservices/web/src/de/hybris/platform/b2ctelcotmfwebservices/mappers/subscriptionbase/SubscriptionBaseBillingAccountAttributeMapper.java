/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscriptionbase;

import de.hybris.platform.b2ctelcofacades.data.TmaDetailedSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BillingAccountRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionBase;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for billing account attribute between {@link TmaDetailedSubscriptionBaseData} and
 * {@link SubscriptionBase}
 *
 * @since 1907
 */
public class SubscriptionBaseBillingAccountAttributeMapper
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
		if (source.getBillingAccountData() != null)
		{
			target.setBillingAccount(getMapperFacade().map(source.getBillingAccountData(), BillingAccountRef.class, context));
		}
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
