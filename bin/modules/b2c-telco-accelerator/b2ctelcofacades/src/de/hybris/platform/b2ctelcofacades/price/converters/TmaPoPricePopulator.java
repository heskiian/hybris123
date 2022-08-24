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
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Custom implementation of the {@link ProductPricePopulator} with pricing enhancements for telco.
 *
 * @param <SOURCE>
 * 		source to retrieve the information from
 * @param <TARGET>
 * 		target to be populated with information
 * @since 1805
 * @deprecated since 2007. Use instead {@link TmaProductPricePopulator} due to supporting new pricing determination logic as
 * best applicable price not minimum
 */
@Deprecated(since = "2007")
public class TmaPoPricePopulator<SOURCE extends ProductModel, TARGET extends ProductData>
		extends ProductPricePopulator<SOURCE, TARGET>
{
	private CommonI18NService i18NService;

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
					.build();
			final Double minimumPriceValue = getCommercePriceService().getMinimumPriceValue(priceContext);
			if (minimumPriceValue == null)
			{
				target.setPurchasable(false);
				return;
			}

			final PriceDataType priceType = CollectionUtils.isEmpty(sourceProductOffering.getVariants()) ? PriceDataType.BUY
					: PriceDataType.FROM;
			final PriceData priceData = getPriceDataFactory()
					.create(priceType, BigDecimal.valueOf(minimumPriceValue), getI18NService().getCurrentCurrency().getIsocode());
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

	@Required
	public void setI18NService(final CommonI18NService i18NService)
	{
		this.i18NService = i18NService;
	}

}
