/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.commercefacades.product.converters.populator.ProductPricePopulator;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * Custom implementation of the {@link ProductPricePopulator} with Telco price context logic for populating the prices.
 *
 * @param <SOURCE>
 * 		source to retrieve the information from
 * @param <TARGET>
 * 		target to be populated with information
 * @since 2007
 */
public class TmaProductPricePopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends ProductPricePopulator<SOURCE, TARGET>
{
	private CommonI18NService i18NService;
	private Converter<PriceRowModel, PriceData> priceDataConverter;

	public TmaProductPricePopulator(final CommonI18NService i18NService,
			final Converter<PriceRowModel, PriceData> priceDataConverter)
	{
		this.i18NService = i18NService;
		this.priceDataConverter = priceDataConverter;
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		if (!(source instanceof TmaProductOfferingModel))
		{
			return;
		}

		final TmaProductOfferingModel sourceProductOffering = (TmaProductOfferingModel) source;
		final Set<TmaProcessType> processTypes = new HashSet<>();
		if (sourceProductOffering.getProductSpecification() == null)
		{
			processTypes.add(TmaProcessType.DEVICE_ONLY);
		}
		else
		{
			processTypes.add(TmaProcessType.ACQUISITION);
		}

		if (source instanceof TmaSimpleProductOfferingModel)
		{
			final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
					.withProduct(sourceProductOffering)
					.withProcessTypes(processTypes)
					.withCurrency(getI18NService().getCurrentCurrency())
					.build();

			final PriceRowModel priceRow = getCommercePriceService().getBestApplicablePrice(priceContext);
			if (ObjectUtils.isEmpty(priceRow))
			{
				target.setPurchasable(false);
				return;
			}
			final PriceData priceData = getPriceDataConverter().convert(priceRow);

			final PriceDataType priceType = CollectionUtils.isEmpty(sourceProductOffering.getVariants()) ? PriceDataType.BUY
					: PriceDataType.FROM;

			priceData.setPriceType(priceType);
			target.setPrice(priceData);
		}
	}

	@Override
	protected TmaCommercePriceService getCommercePriceService()
	{
		return (TmaCommercePriceService) super.getCommercePriceService();
	}

	protected CommonI18NService getI18NService()
	{
		return i18NService;
	}

	protected Converter<PriceRowModel, PriceData> getPriceDataConverter()
	{
		return priceDataConverter;
	}
}
