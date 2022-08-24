/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaBundledProdOfferOptionData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProdOfferOptionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ObjectUtils;


/**
 * Populates {@link ProductData} from a {@link ProductModel} entity. It is use for populating list of children to
 * ProductData for Type Bundle product offering
 *
 * @since 1810
 */
public class TmaBpoChildrenPopulator extends AbstractProductPopulator<ProductModel, ProductData>
{
	private TmaPoService poService;
	private Converter<ProductModel, ProductData> productConverter;
	private Converter<TmaBundledProdOfferOptionModel, TmaBundledProdOfferOptionData> bpoOptionConverter;

	public TmaBpoChildrenPopulator(
			final Converter<TmaBundledProdOfferOptionModel, TmaBundledProdOfferOptionData> bpoOptionConverter,
			final TmaPoService poService)
	{
		this.bpoOptionConverter = bpoOptionConverter;
		this.poService = poService;
	}

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		if (!(source instanceof TmaBundledProductOfferingModel))
		{
			return;
		}
		final TmaBundledProductOfferingModel bundledProductOfferingModel = (TmaBundledProductOfferingModel) source;
		if (!ObjectUtils.isEmpty(bundledProductOfferingModel.getChildren()))
		{
			target.setChildren(getBundledProductOfferingChildren(bundledProductOfferingModel.getChildren(),
					(TmaBundledProductOfferingModel) source));
		}
	}

	private List<ProductData> getBundledProductOfferingChildren(
			final Collection<TmaProductOfferingModel> bundledProductOfferingModels,
			final TmaBundledProductOfferingModel bundledProductOffering)
	{
		final List<ProductData> productOfferingDataList = new ArrayList<>();
		if (ObjectUtils.isEmpty(bundledProductOfferingModels))
		{
			return productOfferingDataList;
		}
		for (final TmaProductOfferingModel productModel : bundledProductOfferingModels)
		{
			final ProductData productData = new ProductData();
			getProductConverter().convert(productModel, productData);

			getPoService().getBundledProdOfferOptionFor(bundledProductOffering, productModel)
					.ifPresent((TmaBundledProdOfferOptionModel tmaBundledProdOfferOptionModel) -> productData
							.setBundledProdOfferOption(getBpoOptionConverter().convert(tmaBundledProdOfferOptionModel)));

			productOfferingDataList.add(productData);
		}
		return productOfferingDataList;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	protected Converter<TmaBundledProdOfferOptionModel, TmaBundledProdOfferOptionData> getBpoOptionConverter()
	{
		return bpoOptionConverter;
	}

	protected TmaPoService getPoService()
	{
		return poService;
	}
}
