/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.solr.indexing;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;


/**
 * Resolves Solr Property for the spo-{@link TmaSimpleProductOfferingModel} given an {@link PriceRowModel}.
 * For SimplePrices - it will return the direct object.
 * For PriceOverrides - it will return the affected product.
 *
 * @since 1810
 */
public class TmaSpoSource
{
	/**
	 * Resolves Solr Property for the spo-{@link TmaSimpleProductOfferingModel} given an {@link PriceRowModel}.
	 * For SimplePrices - it will return the direct object.
	 * For PriceOverrides - it will return the affected product.
	 *
	 * @param price
	 * 		the price
	 * @return the product
	 */
	public ProductModel getProduct(PriceRowModel price)
	{
		ProductModel product = price.getProduct();
		if (product instanceof TmaSimpleProductOfferingModel || product instanceof TmaFixedBundledProductOfferingModel)
		{
			return product;
		}
		else if (product instanceof TmaBundledProductOfferingModel)
		{
			return price.getAffectedProductOffering();
		}
		else
		// all products should be Spos / Bpos but who knows
		{
			return product;
		}
	}
}
