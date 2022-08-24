/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaBundledProdOfferOptionData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates {@link TmaBundledProdOfferOptionData} based on {@link TmaBundledProdOfferOptionModel}
 *
 * @since 2011
 */
public class TmaBundledProdOfferOptionPopulator
		implements Populator<TmaBundledProdOfferOptionModel, TmaBundledProdOfferOptionData>
{
	private Converter<ProductModel, ProductData> productConverter;

	public TmaBundledProdOfferOptionPopulator(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	@Override
	public void populate(TmaBundledProdOfferOptionModel source, TmaBundledProdOfferOptionData target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setLowerLimit(source.getLowerLimit());
		target.setUpperLimit(source.getUpperLimit());
		target.setDefaultValue(source.getDefault());
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}
}
