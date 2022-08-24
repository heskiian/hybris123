/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscriptionaccess;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionAccess;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.AccessType;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for type attribute between {@link TmaSubscriptionAccessData} and {@link SubscriptionAccess}
 *
 * @since 1907
 */
public class SubscriptionAccessTypeAttributeMapper extends TmaAttributeMapper<TmaSubscriptionAccessData, SubscriptionAccess>
{
	@Override
	public void populateTargetAttributeFromSource(TmaSubscriptionAccessData source,
			SubscriptionAccess target, MappingContext context)
	{
		if(source.getAccessType() != null)
		{
			AccessType accessType = AccessType.valueOf(source.getAccessType().name());
			target.setAccessType(accessType);
		}
	}
}
