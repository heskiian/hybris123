/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferingGroupData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.cmsfacades.uniqueidentifier.populator.ProductModelItemDataPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;


/**
 * Populates {@link TmaOfferingGroupData} with details from {@link TmaProductOfferingGroupModel}.
 *
 * @since 1903
 */

public class TmaOfferingGroupPopulator implements Populator<TmaProductOfferingGroupModel, TmaOfferingGroupData>
{
	private Converter<ProductModel, ProductData> productDataConverter;

	@Override
	public void populate(final TmaProductOfferingGroupModel productOfferingGroupModel,
			final TmaOfferingGroupData offeringGroupData) throws ConversionException
	{
		offeringGroupData.setId(productOfferingGroupModel.getCode());
		offeringGroupData.setName(productOfferingGroupModel.getName());
		if (productOfferingGroupModel.getParentBundleProductOffering() != null)
		{
			offeringGroupData.setParentBpo(getProductDataConverter().convert(productOfferingGroupModel
					.getParentBundleProductOffering()));

		}
		if (!CollectionUtils.isEmpty(productOfferingGroupModel.getChildProductOfferings()))
		{
			final List<ProductData> productOfferings = new ArrayList<>();
			for (TmaProductOfferingModel productOfferingModel : productOfferingGroupModel.getChildProductOfferings())
			{
				productOfferings.add(getProductDataConverter().convert(productOfferingModel));
			}
			offeringGroupData.setChildProductOfferings(productOfferings);
		}
	}

	protected Converter<ProductModel, ProductData> getProductDataConverter()
	{
		return productDataConverter;
	}

	public void setProductDataConverter(
			Converter<ProductModel, ProductData> productDataConverter)
	{
		this.productDataConverter = productDataConverter;
	}
}
