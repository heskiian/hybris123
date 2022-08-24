/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscriptionaccess;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionAccess;
import de.hybris.platform.commercefacades.user.data.PrincipalData;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for subscription access attribute between {@link TmaSubscriptionAccessData} and
 * {@link SubscriptionAccess}
 *
 * @since 1907
 */
public class SubscriptionAccessRelatedPartyAttributeMapper
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

		if (StringUtils.isEmpty(source.getPrincipalUid()))
		{
			return;
		}

		final PrincipalData principalData = new PrincipalData();
		principalData.setUid(source.getPrincipalUid());
		target.setRelatedParty(getMapperFacade().map(principalData, RelatedPartyRef.class, context));
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
