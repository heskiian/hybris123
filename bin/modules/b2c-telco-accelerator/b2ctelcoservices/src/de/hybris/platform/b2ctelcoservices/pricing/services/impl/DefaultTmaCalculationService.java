/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.services.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaFixedBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.pricing.services.TmaCalculationService;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.TmaFindAbstractOrderEntryPriceStrategy;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.TmaAbstractOrderDiscountValuePriceBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.TmaAbstractOrderPriceBuilder;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.FindDeliveryCostStrategy;
import de.hybris.platform.order.strategies.calculation.FindPaymentCostStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Custom implementation of the {@link DefaultCalculationService} providing enhancements for Telco specific operations.
 *
 * @since 6.7
 */
public class DefaultTmaCalculationService extends DefaultCalculationService implements TmaCalculationService
{
	private TmaFindAbstractOrderEntryPriceStrategy findAbstractOrderEntryPriceStrategy;//NOSONAR
	private FindDeliveryCostStrategy deliveryCostStrategy;//NOSONAR
	private FindPaymentCostStrategy paymentCostStrategy;//NOSONAR
	private KeyGenerator keyGenerator;//NOSONAR
	private TmaAbstractOrderPriceBuilder orderPriceBuilder;//NOSONAR
	private CommonI18NService defaultCommonI18NService; //NOSONAR

	@Override
	public void calculateEntries(final AbstractOrderModel order, final boolean forceRecalculate) throws CalculationException
	{
		double subtotal = 0.0;
		for (final AbstractOrderEntryModel entry : order.getEntries())
		{
			if (isCalculationRequired(entry))
			{
				recalculateOrderEntryIfNeeded(entry, forceRecalculate);
				subtotal += entry.getTotalPrice().doubleValue();
			}
			else
			{
				entry.setCalculated(Boolean.TRUE);
				entry.setPrice(null);
				entry.setTotalPrice((double) 0);
				entry.setBasePrice((double) 0);
				getModelService().save(entry);
			}
		}
		order.setTotalPrice(Double.valueOf(subtotal));
	}

	/**
	 * @deprecated since 2007. Use instead {@link TmaAbstractOrderDiscountValuePriceBuilder#buildPrice(DiscountValue, AbstractOrderModel, List)}
	 */
	@Deprecated(since = "2007")
	protected TmaAbstractOrderChargePriceModel createCartPriceForDiscountValue(AbstractOrderEntryModel entry,
			DiscountValue discountValue)
	{
		// create cart price of type One time price charge , with label promotion
		double appliedValue = discountValue.getAppliedValue();

		if (appliedValue > 0.0)
		{
			appliedValue *= -1;
		}

		final TmaAbstractOrderOneTimeChargePriceModel promotionPrice = (TmaAbstractOrderOneTimeChargePriceModel) getOrderPriceBuilder()
				.buildPrice(appliedValue, entry.getOrder(), null, TmaAbstractOrderPriceType.DISCOUNT);
		promotionPrice.setDiscountValueCode(discountValue.getCode());
		getModelService().save(promotionPrice);

		return promotionPrice;
	}

	/**
	 * @deprecated since 2007. Use instead {@link TmaAbstractOrderDiscountValuePriceBuilder#buildPrice(DiscountValue, AbstractOrderModel, List)}
	 */
	@Deprecated(since = "2007")
	protected TmaAbstractOrderChargePriceModel createCartPriceForDiscountValue(AbstractOrderModel order,
			DiscountValue discountValue)
	{
		// create cart price of type One time price charge , with label promotion
		final TmaAbstractOrderOneTimeChargePriceModel promotionPrice = (TmaAbstractOrderOneTimeChargePriceModel) getOrderPriceBuilder()
				.buildPrice(discountValue.getAppliedValue() * (-1), order, null, TmaAbstractOrderPriceType.DISCOUNT);
		promotionPrice.setDiscountValueCode(discountValue.getCode());
		getModelService().save(promotionPrice);

		return promotionPrice;
	}

	@Override
	public TmaAbstractOrderPriceModel createCartPrice()
	{
		final TmaAbstractOrderCompositePriceModel orderPrice =
				getModelService().create(TmaAbstractOrderCompositePriceModel.class);
		getModelService().save(orderPrice);
		return orderPrice;
	}

	@Override
	protected void resetAllValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		super.resetAllValues(entry);
		removeOldPrice(entry.getPrice());
		final List<TaxValue> orderEntryTaxes = new ArrayList<>(entry.getTaxValues());
		final TmaAbstractOrderPriceModel orderEntryPrice = getFindAbstractOrderEntryPriceStrategy()
				.findBasePrice(entry, orderEntryTaxes);
		entry.setPrice(orderEntryPrice);
	}

	@Override
	protected void resetAdditionalCosts(final AbstractOrderModel order, final Collection<TaxValue> relativeTaxValues)
	{
		removeOldPrice(order.getPrice());
		final TmaAbstractOrderCompositePriceModel orderPrice = getModelService().create(TmaAbstractOrderCompositePriceModel.class);
		final Set<TmaAbstractOrderPriceModel> orderChildPrices = new HashSet<>();

		final TmaAbstractOrderPriceModel deliveryCostPrice = getDeliveryPrice(order, relativeTaxValues);
		orderChildPrices.add(deliveryCostPrice);
		final TmaAbstractOrderPriceModel paymentCostPrice = getPaymentPrice(order, relativeTaxValues);
		orderChildPrices.add(paymentCostPrice);

		orderPrice.setChildPrices(orderChildPrices);
		order.setPrice(orderPrice);
	}

	protected TmaAbstractOrderPriceModel getPaymentPrice(final AbstractOrderModel order,
			final Collection<TaxValue> relativeTaxValues)
	{
		final PriceValue paymentCostValue = getPaymentCostStrategy().getPaymentCost(order);
		double convertedPrice = convertFromNetToGrossPriceIfNeeded(paymentCostValue, order.getNet(), order.getCurrency());
		order.setPaymentCost(convertedPrice);
		return getOrderPriceBuilder()
				.buildPrice(convertedPrice, order, new ArrayList<>(relativeTaxValues), TmaAbstractOrderPriceType.PAYMENT_COST);
	}

	protected TmaAbstractOrderPriceModel getDeliveryPrice(AbstractOrderModel order, Collection<TaxValue> relativeTaxValues)
	{
		final PriceValue deliveryCostValue = getDeliveryCostStrategy().getDeliveryCost(order);
		double convertedPrice = convertFromNetToGrossPriceIfNeeded(deliveryCostValue, order.getNet(), order.getCurrency());
		order.setDeliveryCost(convertedPrice);
		return getOrderPriceBuilder()
				.buildPrice(convertedPrice, order, new ArrayList<>(relativeTaxValues), TmaAbstractOrderPriceType.DELIVERY_COST);
	}

	protected double convertFromNetToGrossPriceIfNeeded(final PriceValue priceValue, final boolean toNet,
			final CurrencyModel orderCurrency)
	{
		double convertedPrice = priceValue.getValue();
		if (priceValue.isNet() != toNet)
		{
			convertedPrice = getDefaultCommonI18NService().roundCurrency(convertedPrice, orderCurrency.getDigits());
		}
		return convertedPrice;
	}

	private void removeOldPrice(final TmaAbstractOrderPriceModel price)
	{
		if (price != null)
		{
			getModelService().remove(price);
		}
	}

	/**
	 * Calculation is required for entries representing:
	 * <ul>
	 * <li>SPOs or Fixed BPOs which are not part of a Fixed BPO structure</li></>
	 * </ul>
	 *
	 * @param entry
	 * 		the entry for which calculation is checked
	 * @return <true>in case calculation is required for the given entry</true>, <code>false</code> otherwise
	 */
	private boolean isCalculationRequired(final AbstractOrderEntryModel entry)
	{
		if (entry.getMasterEntry() != null && entry.getMasterEntry().getProduct() instanceof TmaFixedBundledProductOfferingModel)
		{
			return false;
		}
		return !StringUtils.equalsIgnoreCase(TmaBundledProductOfferingModel._TYPECODE, entry.getProduct().getItemtype());
	}

	protected TmaFindAbstractOrderEntryPriceStrategy getFindAbstractOrderEntryPriceStrategy()
	{
		return findAbstractOrderEntryPriceStrategy;
	}

	@Required
	public void setFindAbstractOrderEntryPriceStrategy(TmaFindAbstractOrderEntryPriceStrategy findAbstractOrderEntryPriceStrategy)
	{
		this.findAbstractOrderEntryPriceStrategy = findAbstractOrderEntryPriceStrategy;
	}

	protected FindDeliveryCostStrategy getDeliveryCostStrategy()
	{
		return deliveryCostStrategy;
	}

	@Required
	public void setDeliveryCostStrategy(FindDeliveryCostStrategy deliveryCostStrategy)
	{
		this.deliveryCostStrategy = deliveryCostStrategy;
	}

	protected FindPaymentCostStrategy getPaymentCostStrategy()
	{
		return paymentCostStrategy;
	}

	@Required
	public void setPaymentCostStrategy(FindPaymentCostStrategy paymentCostStrategy)
	{
		this.paymentCostStrategy = paymentCostStrategy;
	}

	/**
	 * @deprecated since 2007.
	 */
	@Deprecated(since = "2007")
	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	/**
	 * @deprecated since 2007.
	 */
	@Required
	@Deprecated(since = "2007")
	public void setKeyGenerator(KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	protected TmaAbstractOrderPriceBuilder getOrderPriceBuilder()
	{
		return orderPriceBuilder;
	}

	@Required
	public void setOrderPriceBuilder(TmaAbstractOrderPriceBuilder orderPriceBuilder)
	{
		this.orderPriceBuilder = orderPriceBuilder;
	}

	protected CommonI18NService getDefaultCommonI18NService()
	{
		return defaultCommonI18NService;
	}

	@Required
	public void setDefaultCommonI18NService(final CommonI18NService defaultCommonI18NService)
	{
		this.defaultCommonI18NService = defaultCommonI18NService;
	}
}
