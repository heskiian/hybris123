/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atBaseType attribute for Subscription between
 * {@link TmaSubscriptionBaseData} and {@link Product}
 *
 * @since 2102
 */
public class ProductAtBaseTypeAttributeMapper extends TmaAttributeMapper<TmaSubscriptionBaseData, Product>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaSubscriptionBaseData source, final Product target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getSubscriberIdentity()))
		{
			target.setAtbaseType(
					Config.getString(B2ctelcotmfwebservicesConstants.TMA_PRODUCTAPI_BASETYPE, B2ctelcotmfwebservicesConstants.PRODUCT));
		}
	}
}
