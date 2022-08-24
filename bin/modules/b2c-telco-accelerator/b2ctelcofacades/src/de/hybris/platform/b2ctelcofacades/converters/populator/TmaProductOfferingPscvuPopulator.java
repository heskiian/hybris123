/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharValueUseModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;


/**
 * Populates {@link ProductData} with the product specification characteristic value uses configured as product specification
 * characteristic values on {@link TmaProductOfferingModel}.
 *
 * @since 1903
 */

public class TmaProductOfferingPscvuPopulator extends AbstractProductPopulator<TmaProductOfferingModel, ProductData>
{
	/**
	 * The converter has been deprecated as PSCVU value of Product Offering has been taken to consideration rather than PSCV
	 * Converter from {@link TmaProductSpecCharacteristicValueModel} to {@link TmaProductSpecCharacteristicValueUseData}
	 *
	 * @deprecated since 2105
	 */
	@Deprecated(since = "2105")
	private Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueUseData> pscvuConverter;
	
	/**
	 * Converter from {@link TmaProductSpecificationModel} to {@link TmaProductSpecificationData}
	 */
	private Converter<TmaProductSpecificationModel, TmaProductSpecificationData> productSpecificationConverter;
	
	/**
	 * Converter from {@link TmaProductSpecCharValueUseModel} to {@link TmaProductSpecCharacteristicValueUseData}
	 */
	private Converter<TmaProductSpecCharValueUseModel, TmaProductSpecCharacteristicValueUseData> prodSpecCharValueUseConverter;

	public TmaProductOfferingPscvuPopulator(
			final Converter<TmaProductSpecCharValueUseModel, TmaProductSpecCharacteristicValueUseData> prodSpecCharValueUseConverter)
	{
		this.prodSpecCharValueUseConverter = prodSpecCharValueUseConverter;
	}

	@Override
	public void populate(final TmaProductOfferingModel productOfferingModel, ProductData productData)
			throws ConversionException
	{
		Set<TmaProductSpecCharValueUseModel> pscvuModels = productOfferingModel.getProductSpecCharValueUses();
		productData
				.setProductSpecCharValueUses(getPscvuDataListFromModels(productOfferingModel.getProductSpecification(), pscvuModels));
	}

	private List<TmaProductSpecCharacteristicValueUseData> getPscvuDataListFromModels(
			TmaProductSpecificationModel productSpecificationModel,
			Set<TmaProductSpecCharValueUseModel> pscvuModels)
	{
		final List<TmaProductSpecCharacteristicValueUseData> pscvuDataList = new ArrayList<>();

		if (CollectionUtils.isEmpty(pscvuModels))
		{
			return pscvuDataList;
		}

		pscvuModels.forEach(pscvuModel -> {
			TmaProductSpecCharacteristicValueUseData pscvuData = getProdSpecCharValueUseConverter().convert(pscvuModel);
			if (productSpecificationModel != null)
			{
				pscvuData.setProductSpecification(getProductSpecificationConverter().convert(productSpecificationModel));
			}
			pscvuDataList.add(pscvuData);
		});

		return pscvuDataList;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since = "2105")
	public Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueUseData> getPscvuConverter()
	{
		return pscvuConverter;
	}

	/**
	 * @deprecated since 2105
	 */
	@Deprecated(since = "2105")
	@Required
	public void setPscvuConverter(
			Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueUseData> pscvuConverter)
	{
		this.pscvuConverter = pscvuConverter;
	}
	
	public Converter<TmaProductSpecificationModel, TmaProductSpecificationData> getProductSpecificationConverter()
	{
		return productSpecificationConverter;
	}
	
	@Required
	public void setProductSpecificationConverter(
			Converter<TmaProductSpecificationModel, TmaProductSpecificationData> productSpecificationConverter)
	{
		this.productSpecificationConverter = productSpecificationConverter;
	}
	
	public Converter<TmaProductSpecCharValueUseModel, TmaProductSpecCharacteristicValueUseData> getProdSpecCharValueUseConverter()
	{
		return prodSpecCharValueUseConverter;
	}

}
