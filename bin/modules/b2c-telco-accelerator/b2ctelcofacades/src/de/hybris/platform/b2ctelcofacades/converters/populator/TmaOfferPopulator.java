/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;


/**
 * Populator for {@link TmaOfferData}.
 *
 * @since 6.7
 */
public class TmaOfferPopulator implements Populator<ProductData, TmaOfferData>
{
	@Override
	public void populate(final ProductData source, final TmaOfferData target)
	{
		target.setProduct(source);
	}
}
