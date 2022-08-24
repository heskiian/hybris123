/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;

import java.util.Optional;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;


/**
 * Populates a {@link ProductData} having as source a {@link TmaSubscriptionBaseModel}.
 *
 * @since 2102
 */
public class TmaProductSubscriptionBasePopulator implements Populator<TmaSubscriptionBaseModel, ProductData>
{
	@Override
	public void populate(final TmaSubscriptionBaseModel source, final ProductData target)
	{
		if (CollectionUtils.isEmpty(source.getSubscribedProducts()))
		{
			return;
		}
		final Optional<TmaSubscribedProductModel> bundledSubscribedProduct = source.getSubscribedProducts().stream()
				.filter(product -> StringUtils.isNotEmpty(product.getBundledProductCode())).findFirst();
		if (bundledSubscribedProduct.isPresent())
		{
			target.setCode(bundledSubscribedProduct.get().getBundledProductCode());
		}
		else
		{
			target.setCode(source.getSubscribedProducts().iterator().next().getId());
		}
		target.setPurchasable(true);
	}
}
