/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.data.TmaServiceTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionAccessData;
import de.hybris.platform.b2ctelcoservices.model.TmaBillingAccountModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionAccessModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;


/**
 * Populates a {@link TmaSubscribedProductData} having as source a {@link TmaSubscribedProductModel}.
 *
 * @since 6.6
 */
public class TmaSubscribedProductPopulator implements Populator<TmaSubscribedProductModel, TmaSubscribedProductData>
{
	private final TmaPoService tmaPoService;
	private final Converter<ProductModel, ProductData> productConverter;
	private final Converter<TmaBillingAccountModel, TmaBillingAccountData> tmaBillingAccountDataConverter;
	private final Converter<TmaSubscribedProductModel, ProductData> tmaProductSubscribedProductConverter;
	private final Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessRefConverter;


	public TmaSubscribedProductPopulator(final TmaPoService tmaPoService,
			final Converter<ProductModel, ProductData> productConverter,
			final Converter<TmaBillingAccountModel, TmaBillingAccountData> tmaBillingAccountDataConverter,
			final Converter<TmaSubscribedProductModel, ProductData> tmaProductSubscribedProductConverter,
			final Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> tmaSubscriptionAccessRefConverter)
	{
		this.tmaBillingAccountDataConverter = tmaBillingAccountDataConverter;
		this.tmaPoService = tmaPoService;
		this.productConverter = productConverter;
		this.tmaProductSubscribedProductConverter = tmaProductSubscribedProductConverter;
		this.tmaSubscriptionAccessRefConverter = tmaSubscriptionAccessRefConverter;
	}

	@Override
	public void populate(final TmaSubscribedProductModel source, final TmaSubscribedProductData target)
	{
		target.setId(source.getId());
		target.setDescription(source.getDescription());
		target.setName(source.getName());
		target.setProductCode(source.getProductCode());
		target.setServiceType(TmaServiceTypeData.valueOf(source.getServiceType().name()));
		setSubscriptionBase(source, target);
		target.setBillingsystemId(source.getBillingsystemId());
		target.setBillingSubscriptionId(source.getBillingSubscriptionId());
		target.setSubscriptionStatus(source.getSubscriptionStatus());
		target.setStartDate(source.getStartDate());
		target.setEndDate(source.getEndDate());
		target.setCancelledDate(source.getCancelledDate());
		target.setRenewalType(source.getRenewalType());
		target.setCancellable(source.getCancellable());
		target.setBillingFrequency(source.getBillingFrequency());
		target.setContractDuration(source.getContractDuration());
		target.setContractFrequency(source.getContractFrequency());
		target.setOrderEntryNumber(source.getOrderEntryNumber());
		target.setOrderNumber(source.getOrderNumber());
		target.setCustomerId(source.getCustomerId());
		target.setPlacedOn(source.getPlacedOn());
		target.setParentPOCode(source.getBundledProductCode());
		target.setPaymentMethodId(source.getPaymentMethodId());
		setProductOfferingRef(source, target);
		setBillingAccount(source, target);
		setSubscriptionAccessData(source, target);
	}

	private void setSubscriptionBase(final TmaSubscribedProductModel source, final TmaSubscribedProductData target)
	{
		final TmaSubscriptionBaseModel subscriptionBase = source.getSubscriptionBase();
		if (subscriptionBase == null)
		{
			return;
		}
		target.setSubscriptionBaseId(subscriptionBase.getSubscriberIdentity());
	}

	private void setProductOfferingRef(final TmaSubscribedProductModel source, final TmaSubscribedProductData target)
	{
		if (StringUtils.isBlank(source.getProductCode()))
		{
			return;
		}
		try
		{
			final TmaProductOfferingModel productModel = getTmaPoService().getPoForCode(source.getProductCode());
			target.setProductOfferingRef(getProductConverter().convert(productModel));
		}
		catch (final UnknownIdentifierException e)
		{
			target.setProductOfferingRef(getTmaProductSubscribedProductConverter().convert(source));
		}
	}

	private void setBillingAccount(final TmaSubscribedProductModel source, final TmaSubscribedProductData target)
	{
		final TmaSubscriptionBaseModel subscriptionBase = source.getSubscriptionBase();
		if (ObjectUtils.isEmpty(subscriptionBase) || ObjectUtils.isEmpty(subscriptionBase.getBillingAccount()))
		{
			return;
		}
		target.setBillingAccountRef(getTmaBillingAccountDataConverter().convert(subscriptionBase.getBillingAccount()));
	}

	protected void setSubscriptionAccessData(final TmaSubscribedProductModel source, final TmaSubscribedProductData target)
	{
		final TmaSubscriptionBaseModel subscriptionBase = source.getSubscriptionBase();
		if (ObjectUtils.isEmpty(subscriptionBase) || CollectionUtils.isEmpty(subscriptionBase.getSubscriptionAccesses()))
		{
			return;
		}
		target.setSubscriptionAccesses(
				getTmaSubscriptionAccessRefConverter().convertAll(subscriptionBase.getSubscriptionAccesses()));

	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	protected Converter<TmaBillingAccountModel, TmaBillingAccountData> getTmaBillingAccountDataConverter()
	{
		return tmaBillingAccountDataConverter;
	}

	protected Converter<TmaSubscribedProductModel, ProductData> getTmaProductSubscribedProductConverter()
	{
		return tmaProductSubscribedProductConverter;
	}

	protected Converter<TmaSubscriptionAccessModel, TmaSubscriptionAccessData> getTmaSubscriptionAccessRefConverter()
	{
		return tmaSubscriptionAccessRefConverter;
	}

}
