/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.PromotionOrderEntryConsumedData;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Cart populator with TMA specific details.
 *
 * @since 1810
 */
public class TmaCartPopulator extends TmaAbstractOrderPopulator<CartModel, CartData>
{
	private Converter<PaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter;
	private Converter<AddressModel, AddressData> addressConverter;
	private Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter;
	private Converter<ZoneDeliveryModeModel, ZoneDeliveryModeData> zoneDeliveryModeConverter;

	/**
	 * Constructor responsible for injecting collaborators
	 *
	 * @param creditCardPaymentInfoConverter
	 * 		converter used for converting credit card payment information from model to data
	 * @param addressConverter
	 * 		converter used for converting addresses from model to data
	 * @param deliveryModeConverter
	 * 		converter used for converting delivery modes information from model to data
	 * @param zoneDeliveryModeConverter
	 * 		converter used for converting zone delivery modes from model to data
	 * @deprecated since 2102. Use instead the setter methods from base class.
	 */
	@Deprecated(since = "2102")
	public TmaCartPopulator(
			Converter<PaymentInfoModel, CCPaymentInfoData> creditCardPaymentInfoConverter,
			Converter<AddressModel, AddressData> addressConverter,
			Converter<DeliveryModeModel, DeliveryModeData> deliveryModeConverter,
			Converter<ZoneDeliveryModeModel, ZoneDeliveryModeData> zoneDeliveryModeConverter)
	{
		this.creditCardPaymentInfoConverter = creditCardPaymentInfoConverter;
		this.addressConverter = addressConverter;
		this.deliveryModeConverter = deliveryModeConverter;
		this.zoneDeliveryModeConverter = zoneDeliveryModeConverter;
	}

	@Override
	public void populate(final CartModel cartModel, final CartData cartData)
	{
		super.populate(cartModel, cartData);

		final OrderStatus status = cartModel.getStatus();
		cartData.setStatus(status);
		setPromotionOnEntries(cartData.getEntries(), cartData.getAppliedProductPromotions(),
				cartData.getPotentialProductPromotions());
	}

	/**
	 * @deprecated since 2102. Use instead {@link #setPromotionOnEntries(List, List, List)}}
	 */
	@Deprecated(since = "2102")
	protected void setPromotionOnEntries(final CartData cartData)
	{
		cartData.getEntries().forEach((final OrderEntryData entry) ->
		{
			entry.setAppliedPromotionResults(
					cartData.getAppliedProductPromotions().stream().filter((final PromotionResultData promotionResult) ->
							promotionResult.getConsumedEntries().stream()
									.anyMatch((PromotionOrderEntryConsumedData consumedEntry) -> consumedEntry.getOrderEntryNumber()
											.equals(entry.getEntryNumber()))
					).collect(Collectors.toSet()));

			entry.setPotentialPromotionResults(
					cartData.getPotentialOrderPromotions().stream().filter((final PromotionResultData promotionResult) ->
							promotionResult.getConsumedEntries().stream()
									.anyMatch(
											(PromotionOrderEntryConsumedData consumedEntry) -> consumedEntry.getOrderEntryNumber()
													.equals(entry.getEntryNumber()))
					).collect(Collectors.toSet()));
		});
	}

	protected void setPromotionOnEntries(final List<OrderEntryData> entries, final List<PromotionResultData> appliedPromotions,
			final List<PromotionResultData> potentialPromotions)
	{
		entries.forEach((final OrderEntryData entry) ->
		{
			entry.setAppliedPromotionResults(
					appliedPromotions.stream().filter((final PromotionResultData promotionResult) ->
							promotionResult.getConsumedEntries().stream()
									.anyMatch((PromotionOrderEntryConsumedData consumedEntry) -> consumedEntry.getOrderEntryNumber()
											.equals(entry.getEntryNumber()))
					).collect(Collectors.toSet()));

			entry.setPotentialPromotionResults(
					potentialPromotions.stream().filter((final PromotionResultData promotionResult) ->
							promotionResult.getConsumedEntries().stream()
									.anyMatch(
											(PromotionOrderEntryConsumedData consumedEntry) -> consumedEntry.getOrderEntryNumber()
													.equals(entry.getEntryNumber()))
					).collect(Collectors.toSet()));


			if (CollectionUtils.isNotEmpty(entry.getEntries()))
			{
				setPromotionOnEntries(entry.getEntries(), appliedPromotions, potentialPromotions);
			}
		});
	}

}
