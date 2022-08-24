/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.networkproductref;

import de.hybris.platform.usageconsumptionservices.model.UcNetworkProductModel;
import de.hybris.platform.usageconsumptiontmfwebservices.constants.UsageconsumptiontmfwebservicesConstants;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.dto.NetworkProductRef;
import de.hybris.platform.usageconsumptiontmfwebservices.v1.mappers.UcAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link UcNetworkProductModel} and {@link NetworkProductRef}
 *
 * @since 2105
 */
public class NetworkProductRefHrefAttributeMapper extends UcAttributeMapper<UcNetworkProductModel, NetworkProductRef>
{
	public NetworkProductRefHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final UcNetworkProductModel source, final NetworkProductRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(UsageconsumptiontmfwebservicesConstants.NETWORK_PRODUCT_API_URL + source.getId());
		}
	}
}
