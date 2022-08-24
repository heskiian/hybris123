/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscriptionbase;

import de.hybris.platform.b2ctelcofacades.data.TmaDetailedSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionBase;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link TmaDetailedSubscriptionBaseData} and {@link SubscriptionBase}
 *
 * @since 1907
 */
public class SubscriptionBaseAtTypeAttributeMapper extends TmaAttributeMapper<TmaDetailedSubscriptionBaseData, SubscriptionBase>
{
	@Override
	public void populateTargetAttributeFromSource(TmaDetailedSubscriptionBaseData source, SubscriptionBase target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
