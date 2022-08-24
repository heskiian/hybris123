/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.util.CollectionUtils;


/**
 * Populates {@link ProductData} from {@link ProductModel} with the list of belonging parent bundled product offerings.
 *
 * @since 1903
 */
public class TmaProductOfferingParentsPopulator implements Populator<TmaProductOfferingModel, ProductData>
{
	private TmaPoService poService;
	private Converter<ProductModel, ProductData> productDataConverter;

	@Override
	public void populate(final TmaProductOfferingModel tmaProductOfferingModel, final ProductData productData)
			throws ConversionException
	{
		final Collection<TmaBundledProductOfferingModel> parentBpos = getPoService().getAllParents(tmaProductOfferingModel);
		productData.setParents(getBundledProductOfferingParents(parentBpos));

	}

	private List<ProductData> getBundledProductOfferingParents(
			final Collection<TmaBundledProductOfferingModel> bundledProductOfferingModels)
	{
		final List<ProductData> productOfferingDataList = new ArrayList();
		if (CollectionUtils.isEmpty(bundledProductOfferingModels))
		{
			return productOfferingDataList;
		}
		for (final ProductModel productModel : bundledProductOfferingModels)
		{
			productOfferingDataList.add(getProductDataConverter().convert(productModel));
		}
		return productOfferingDataList;
	}


	protected TmaPoService getPoService()
	{
		return poService;
	}

	public void setPoService(TmaPoService poService)
	{
		this.poService = poService;
	}

	public Converter<ProductModel, ProductData> getProductDataConverter()
	{
		return productDataConverter;
	}

	public void setProductDataConverter(
			Converter<ProductModel, ProductData> productDataConverter)
	{
		this.productDataConverter = productDataConverter;
	}
}
