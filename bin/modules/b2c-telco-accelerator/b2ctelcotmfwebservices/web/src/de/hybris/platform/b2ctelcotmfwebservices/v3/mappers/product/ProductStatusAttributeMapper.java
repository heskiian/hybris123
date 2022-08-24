/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.product;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.StatusType;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.Product;

import java.util.Locale;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for status attribute between {@link TmaSubscribedProductData} and
 * {@link Product}
 *
 * @since 2102
 */
public class ProductStatusAttributeMapper extends TmaAttributeMapper<TmaSubscribedProductData, Product>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaSubscribedProductData source, final Product target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getSubscriptionStatus()))
		{
			return;
		}

		switch (source.getSubscriptionStatus())
		{
			case "EXPIRED":
				target.setStatus(StatusType.TERMINATED);
				break;
			case "PENDING_ACTIVATION":
			case "INACTIVE":
				target.setStatus(StatusType.PENDINGACTIVE);
				break;
			case "PENDING_TERMINATE":
				target.setStatus(StatusType.PENDINGTERMINATE);
				break;
			default:
				target.setStatus(StatusType.fromValue(source.getSubscriptionStatus().toLowerCase(Locale.ENGLISH)));
				break;
		}
	}
}
