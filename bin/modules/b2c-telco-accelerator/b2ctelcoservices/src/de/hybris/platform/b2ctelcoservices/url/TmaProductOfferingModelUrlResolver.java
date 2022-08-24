/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.url;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.commerceservices.url.impl.DefaultProductModelUrlResolver;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * URL resolver which handles base product for {@link TmaPoVariantModel}.
 *
 * @since 1810
 */
public class TmaProductOfferingModelUrlResolver extends DefaultProductModelUrlResolver
{
	protected ProductModel getBaseProduct(final ProductModel product)
	{
		ProductModel current = product;
		while (current instanceof TmaPoVariantModel)
		{
			final ProductModel baseProduct = ((TmaPoVariantModel) current).getTmaBasePo();
			if (baseProduct == null)
			{
				break;
			}
			else
			{
				current = baseProduct;
			}
		}
		return current;
	}
}
