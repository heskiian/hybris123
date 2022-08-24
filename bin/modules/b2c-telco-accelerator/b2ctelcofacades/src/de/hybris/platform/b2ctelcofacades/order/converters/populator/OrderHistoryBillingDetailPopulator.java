/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.converters.populator.OrderHistoryPopulator;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceDataType;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionfacades.data.OrderPriceData;
import de.hybris.platform.subscriptionfacades.order.impl.BillingTimeComparator;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Converter implementation for {@link de.hybris.platform.core.model.order.OrderModel} as source and
 * {@link de.hybris.platform.commercefacades.order.data.OrderHistoryData} as target type.
 */
public class OrderHistoryBillingDetailPopulator extends OrderHistoryPopulator
{
	private Converter<BillingTimeModel, BillingTimeData> billingTimeConverter;
	private Comparator billingTimeComparator;

	@Override
	public void populate(final OrderModel source, final OrderHistoryData target)
	{
		super.populate(source, target);

		if (source.getBillingTime() == null)
		{
			// compatibility mode: do not perform the subscription specific
			// populator tasks
			return;
		}

		target.setOrderHistoryPrices(buildOrderPrices(source, target));

	}

	protected PriceData createPrice(final AbstractOrderModel source, final Double val)
	{
		if (source == null)
		{
			throw new IllegalArgumentException("source order must not be null");
		}

		final CurrencyModel currency = source.getCurrency();
		if (currency == null)
		{
			throw new IllegalArgumentException("source order currency must not be null");
		}

		// Get double value, handle null as zero
		final double priceValue = val != null ? val.doubleValue() : 0d;

		return getPriceDataFactory().create(PriceDataType.BUY, BigDecimal.valueOf(priceValue), currency);
	}

	public List<OrderPriceData> buildOrderPrices(final AbstractOrderModel source, final OrderHistoryData target)
	{

		final Map<String, OrderPriceData> orderPriceContainer = new HashMap<>();

		final Collection<AbstractOrderModel> abstractOrders = new ArrayList<>();
		abstractOrders.add(source);
		abstractOrders.addAll(source.getChildren());

		for (final AbstractOrderModel abstractOrder : abstractOrders)
		{
			final OrderPriceData orderPrice = new OrderPriceData();
			orderPrice.setTotalPrice(createPrice(abstractOrder, abstractOrder.getTotalPrice()));
			orderPrice.setTotalTax(createPrice(abstractOrder, abstractOrder.getTotalTax()));
			orderPrice.setSubTotal(createPrice(abstractOrder, abstractOrder.getSubtotal()));
			orderPrice.setDeliveryCost(createPrice(abstractOrder, abstractOrder.getDeliveryCost()));

			final BillingTimeData billingTimeData = getBillingTimeConverter().convert(abstractOrder.getBillingTime());
			orderPrice.setBillingTime(billingTimeData);
			orderPriceContainer.put(abstractOrder.getBillingTime().getCode(), orderPrice);
		}

		return buildBillingTimes(source).stream().map(billingTime -> orderPriceContainer.get(billingTime.getCode()))
				.collect(Collectors.toList());
	}

	protected Converter<BillingTimeModel, BillingTimeData> getBillingTimeConverter()
	{
		return billingTimeConverter;
	}

	public void setBillingTimeConverter(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter)
	{
		this.billingTimeConverter = billingTimeConverter;
	}

	protected List<BillingTimeData> buildBillingTimes(final AbstractOrderModel source)
	{
		final List<AbstractOrderModel> children = new ArrayList<>(source.getChildren());
		final Stream<BillingTimeModel> sortedBillingTimes = children.stream().map(c -> c.getBillingTime())
				.sorted(getBillingTimeComparator());

		return Stream.concat(Stream.of(source.getBillingTime()), sortedBillingTimes)
				.map(billingTime -> getBillingTimeConverter().convert(billingTime)).collect(Collectors.toList());

	}

	protected Comparator getBillingTimeComparator()
	{
		return billingTimeComparator;
	}

	public void setBillingTimeComparator(final BillingTimeComparator billingTimeComparator)
	{
		this.billingTimeComparator = billingTimeComparator;
	}

}
