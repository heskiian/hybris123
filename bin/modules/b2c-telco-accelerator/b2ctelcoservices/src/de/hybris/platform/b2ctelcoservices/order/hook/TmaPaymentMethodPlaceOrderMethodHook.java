/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.hook;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderCompositePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.services.TmaBillingTimeService;
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import org.apache.commons.collections.CollectionUtils;


/**
 * Cart validation hook to verify if payment method is required
 *
 * @since 1911
 */
public class TmaPaymentMethodPlaceOrderMethodHook implements CommercePlaceOrderMethodHook
{
	private TmaBillingTimeService billingTimeService;

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	private ConfigurationService configurationService;

	public TmaPaymentMethodPlaceOrderMethodHook(final TmaBillingTimeService billingTimeService,
			final ConfigurationService configurationService)
	{
		this.billingTimeService = billingTimeService;
		this.configurationService = configurationService;
	}

	@Override
	public void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult orderModel) throws InvalidCartException
	{
		// no implementation needed
	}

	@Override
	public void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException
	{
		if (parameter.getCart() != null && isPaymentMethodRequired(parameter.getCart())
				&& parameter.getCart().getPaymentInfo() == null)
		{
			throw new InvalidCartException(
					"Cannot place order because cart '" + parameter.getCart().getCode() + "' does not have payment method.");
		}
	}

	@Override
	public void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
	{
		// no implementation needed
	}

	protected boolean isPaymentMethodRequired(final CartModel cartModel)
	{
		boolean isPaymentMethodRequired;

		isPaymentMethodRequired = (CollectionUtils.isNotEmpty(cartModel.getEntries()) && cartModel.getEntries().stream().anyMatch(
				(AbstractOrderEntryModel entry) -> entry.getPrice() != null && CollectionUtils
						.isNotEmpty(((TmaAbstractOrderCompositePriceModel) entry.getPrice()).getChildPrices())
						&& (((TmaAbstractOrderCompositePriceModel) entry.getPrice()).getChildPrices().stream().anyMatch(
						(TmaAbstractOrderPriceModel price) -> (price instanceof TmaAbstractOrderOneTimeChargePriceModel
								&& ((TmaAbstractOrderOneTimeChargePriceModel) price).getBillingTime().getCode()
								.equals(getBillingTimeForPayNow().getCode())
								&& ((TmaAbstractOrderOneTimeChargePriceModel) price).getDutyFreeAmount() != 0)))))
				|| (cartModel.getPrice() != null && CollectionUtils
				.isNotEmpty(((TmaAbstractOrderCompositePriceModel) cartModel.getPrice()).getChildPrices())
				&& ((TmaAbstractOrderCompositePriceModel) cartModel.getPrice()).getChildPrices().stream().anyMatch(
				(TmaAbstractOrderPriceModel price) -> (price instanceof TmaAbstractOrderOneTimeChargePriceModel
						&& ((TmaAbstractOrderOneTimeChargePriceModel) price).getBillingTime().getCode()
						.equals(getBillingTimeForPayNow().getCode())
						&& ((TmaAbstractOrderOneTimeChargePriceModel) price).getDutyFreeAmount() != 0)));

		return isPaymentMethodRequired;
	}

	private BillingTimeModel getBillingTimeForPayNow()
	{
		return getBillingTimeService().getDefaultBillingTime();
	}

	protected TmaBillingTimeService getBillingTimeService()
	{
		return billingTimeService;
	}

	/**
	 * Returns the configuration service.
	 *
	 * @return the configuration service
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
