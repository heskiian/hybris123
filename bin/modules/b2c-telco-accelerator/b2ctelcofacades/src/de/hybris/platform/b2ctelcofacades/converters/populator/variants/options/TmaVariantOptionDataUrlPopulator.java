/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants.options;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates {@link VariantOptionData} with variant URL data.
 *
 * @since 1810
 */
public class TmaVariantOptionDataUrlPopulator<SOURCE extends TmaPoVariantModel, TARGET extends VariantOptionData> implements
		Populator<SOURCE, TARGET>
{
	private UrlResolver<ProductModel> productModelUrlResolver;

	@Override
	public void populate(final SOURCE poVariantModel, final TARGET variantOptionData)
	{
		variantOptionData.setUrl(getProductModelUrlResolver().resolve(poVariantModel));
	}

	public UrlResolver<ProductModel> getProductModelUrlResolver()
	{
		return productModelUrlResolver;
	}

	@Required
	public void setProductModelUrlResolver(final UrlResolver<ProductModel> productModelUrlResolver)
	{
		this.productModelUrlResolver = productModelUrlResolver;
	}
}
