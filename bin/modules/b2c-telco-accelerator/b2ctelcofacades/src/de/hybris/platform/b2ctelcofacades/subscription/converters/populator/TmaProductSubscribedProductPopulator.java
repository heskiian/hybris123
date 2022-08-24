/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;


/**
 * Populates {@link ProductData} with ProductOffering related attributes from TmaSubscribedProductModel
 * {@link TmaSubscribedProductModel}
 *
 * @since 2102
 */
public class TmaProductSubscribedProductPopulator implements Populator<TmaSubscribedProductModel, ProductData>
{
	@Override
	public void populate(final TmaSubscribedProductModel source, final ProductData target)
	{
		target.setCode(source.getProductCode());
		target.setName(source.getName());
		target.setPurchasable(false);
	}
}
