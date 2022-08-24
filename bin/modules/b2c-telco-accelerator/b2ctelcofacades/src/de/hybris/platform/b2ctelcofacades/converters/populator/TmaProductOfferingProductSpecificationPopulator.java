/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;

import de.hybris.platform.servicelayer.dto.converter.Converter;


/**
 * Populates the product specification from {@link TmaProductOfferingModel} to {@link ProductData} entity.
 *
 * @since 1903
 */
public class TmaProductOfferingProductSpecificationPopulator extends AbstractProductPopulator<TmaProductOfferingModel, ProductData>
{
	/**
	 * Converter from {@link TmaProductSpecificationModel} to {@link TmaProductSpecificationData}
	 */
	private Converter<TmaProductSpecificationModel, TmaProductSpecificationData> productSpecificationConverter;

	@Override
	public void populate(final TmaProductOfferingModel source, final ProductData target)
	{
		if (source.getProductSpecification() == null || source instanceof TmaBundledProductOfferingModel)
		{
			return;
		}

		TmaProductSpecificationData productSpecificationData = getProductSpecificationConverter()
				.convert(source.getProductSpecification());
		target.setProductSpecification(productSpecificationData);
	}

	public Converter<TmaProductSpecificationModel, TmaProductSpecificationData> getProductSpecificationConverter()
	{
		return productSpecificationConverter;
	}

	public void setProductSpecificationConverter(
			Converter<TmaProductSpecificationModel, TmaProductSpecificationData> productSpecificationConverter)
	{
		this.productSpecificationConverter = productSpecificationConverter;
	}
}
