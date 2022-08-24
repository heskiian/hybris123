/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaPriceContextData;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


/**
 * Implementation of {@link TmaProductOfferFacade} using the approaches of composite pricing.
 *
 * @since 2007
 */
public class TmaProductOfferCpFacade extends DefaultTmaProductOfferFacade
{
	private Converter<PriceRowModel, PriceData> tmaPriceDataConverter;

	public TmaProductOfferCpFacade(final Converter<PriceRowModel, PriceData> tmaPriceDataConverter)
	{
		super();
		this.tmaPriceDataConverter = tmaPriceDataConverter;
	}

	@Override
	protected void setProductDataPrices(final String poCode, final TmaPriceContextData priceContext,
			final TmaProductOfferingModel productOffering, final ProductData productData)
	{
		final List<PriceRowModel> priceRowModelList = getPriceRowModels(poCode, priceContext, productOffering);

		if (productData.getPrices() == null)
		{
			productData.setPrices(new ArrayList<>());
		}

		productData.getPrices().addAll(getTmaPriceDataConverter().convertAll(priceRowModelList));
	}

	/**
	 * Sets the price override of the {@link TmaProductOfferingModel} on the BPO provided, price representing the
	 * best applicable price of the {@param po} if bought as part of the {@param bpoData}.
	 *
	 * @param po
	 * 		product offering which is the affected product of the price override
	 * @param bpoModel
	 * 		bpo as part of which the product will be bought, to retrieve information from
	 * @param bpoData
	 * 		bpo to be populated with the price of the {@param po} if bought as part of the {@param bpoData}
	 */
	@Override
	protected void setPoPriceOverrideOnBpo(final TmaProductOfferingModel po, final TmaBundledProductOfferingModel bpoModel,
			final ProductData bpoData)
	{
		if (!(po instanceof TmaSimpleProductOfferingModel))
		{
			return;
		}

		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(bpoModel)
				.withProcessTypes(new HashSet<>(Collections.singletonList(DEFAULT_PROCESS_TYPE)))
				.withtAffectedProduct(po)
				.build();
		final PriceData priceForBpo = getPriceFacade().getBestApplicablePrice(priceContext);

		if (priceForBpo == null)
		{
			return;
		}

		bpoData.setPrice(priceForBpo);
	}

	protected Converter<PriceRowModel, PriceData> getTmaPriceDataConverter()
	{
		return tmaPriceDataConverter;
	}
}
