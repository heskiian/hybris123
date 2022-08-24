/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscriptionbaseref;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.SubscriptionBaseRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link TmaSubscriptionBaseData} and {@link SubscriptionBaseRef}
 *
 * @since 1907
 */
public class SubscriptionBaseRefHrefAttributeMapper extends TmaAttributeMapper<TmaSubscriptionBaseData, SubscriptionBaseRef>
{
	@Override
	public void populateTargetAttributeFromSource(TmaSubscriptionBaseData source, SubscriptionBaseRef target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(
					B2ctelcotmfwebservicesConstants.TMA_SUBSCRIPTION_BASE_API_URL + source.getCode());
		}
	}
}
