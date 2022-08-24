/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.context.impl.TmaPriceContextBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCommercePriceService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.RecurringChargeEntryData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionBillingData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates a {@link List} of {@link SubscriptionBillingData} from a {@link TmaSubscribedProductData}.
 *
 * @since 6.6
 */
public class SubscriptionBillingDataListPopulator implements Populator<TmaSubscribedProductData, List<SubscriptionBillingData>>
{
	private static final String PAID = "Paid";
	private static final String TO_PAY = "to Pay";
	private static final String FROM = "from ";
	private static final String TO = " to ";
	private static final String UNDER_SCORE = "_";
	private static final List<String> monthFrequency = Arrays.asList("monthly", "month", "month(s)", "months");
	private static final List<String> quarterFrequency = Arrays.asList("quarterly", "quarter", "quarter(s)", "quarters");
	private static final List<String> yearFrequency = Arrays.asList("yearly", "year", "year(s)", "years");
	private static final int DAYS_MONTH = 30;
	private static final int DAYS_QUARTER = 90;
	private static final int DAYS_YEAR = 365;

	private ProductService productService;
	private TmaCommercePriceService commercePriceService;
	private Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> subscriptionPricePlanRecurringChargeConverter;

	@Override
	public void populate(final TmaSubscribedProductData source, final List<SubscriptionBillingData> target)
	{
		validateParameterNotNullStandardMessage("source", source);

		final int billingFrequency = calculateDay(source.getBillingFrequency());
		final int contractFrequency = calculateDay(source.getContractFrequency());
		final double totalBill = ((double) contractFrequency / billingFrequency) * source.getContractDuration();
		final Calendar calendar = new GregorianCalendar();
		calendar.setTime(source.getStartDate());
		LocalDate billStartDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH));
		final ProductModel productModel = getProductService().getProductForCode(source.getProductCode());

		final TmaPriceContext priceContext = TmaPriceContextBuilder.newTmaPriceContextBuilder()
				.withProduct(productModel)
				.build();

		final PriceRowModel pricePlanModel = getCommercePriceService().getMinimumPrice(priceContext);

		if (pricePlanModel == null || !(pricePlanModel instanceof SubscriptionPricePlanModel))
		{
			return;
		}

		final SubscriptionPricePlanData subscriptionPricePlanData = new SubscriptionPricePlanData();
		getSubscriptionPricePlanRecurringChargeConverter().convert((SubscriptionPricePlanModel) pricePlanModel,
				subscriptionPricePlanData);

		for (int i = 1; i <= totalBill; i++)
		{
			final SubscriptionBillingData subscriptionBillingData = new SubscriptionBillingData();
			final LocalDate billEndDate = billStartDate.plusDays(billingFrequency);
			final LocalDate billingDate = billEndDate.plusDays(1);
			subscriptionBillingData.setBillingDate(billingDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
			subscriptionBillingData.setBillingPeriod(FROM + billStartDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + TO
					+ billEndDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
			subscriptionBillingData.setBillingId(source.getId() + UNDER_SCORE + i);
			if (billingDate.isBefore(LocalDate.now()))
			{
				subscriptionBillingData.setPaymentStatus(PAID);
			}
			else
			{
				subscriptionBillingData.setPaymentStatus(TO_PAY);
			}
			subscriptionBillingData.setPaymentAmount(calculatePrice(subscriptionPricePlanData, i));
			target.add(subscriptionBillingData);
			billStartDate = billEndDate;
		}
	}

	private int calculateDay(final String srcFrequency)
	{
		if (monthFrequency.contains(srcFrequency.toLowerCase())) //NOSONAR
		{
			return DAYS_MONTH;
		}
		else if (quarterFrequency.contains(srcFrequency.toLowerCase())) //NOSONAR
		{
			return DAYS_QUARTER;
		}
		else if (yearFrequency.contains(srcFrequency.toLowerCase())) //NOSONAR
		{
			return DAYS_YEAR;
		}
		else
		{
			return DAYS_MONTH;
		}
	}

	@Nonnull
	private String calculatePrice(@Nonnull final SubscriptionPricePlanData subscriptionPricePlanData,
			@Nonnull final int currentCycle)
	{
		if (CollectionUtils.isNotEmpty(subscriptionPricePlanData.getRecurringChargeEntries()))
		{
			for (final RecurringChargeEntryData recurringCharge : subscriptionPricePlanData.getRecurringChargeEntries())
			{
				if (null != recurringCharge.getPrice() && isCurrentCycleInRange(recurringCharge, currentCycle))
				{
					return recurringCharge.getPrice().getFormattedValue();
				}
			}
		}
		return "$0.00";
	}

	private boolean isCurrentCycleInRange(final RecurringChargeEntryData recurringCharge, final int currentCycle)
	{
		return recurringCharge.getCycleStart() <= currentCycle
				&& (recurringCharge.getCycleEnd() >= currentCycle || recurringCharge.getCycleEnd() == -1);
	}

	public ProductService getProductService()
	{
		return productService;
	}

	@Required
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	protected TmaCommercePriceService getCommercePriceService()
	{
		return commercePriceService;
	}

	@Required
	public void setCommercePriceService(final TmaCommercePriceService commercePriceService)
	{
		this.commercePriceService = commercePriceService;
	}

	public Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> getSubscriptionPricePlanRecurringChargeConverter()
	{
		return subscriptionPricePlanRecurringChargeConverter;
	}

	@Required
	public void setSubscriptionPricePlanRecurringChargeConverter(
			final Converter<SubscriptionPricePlanModel, SubscriptionPricePlanData> subscriptionPricePlanRecurringChargeConverter)
	{
		this.subscriptionPricePlanRecurringChargeConverter = subscriptionPricePlanRecurringChargeConverter;
	}

}
