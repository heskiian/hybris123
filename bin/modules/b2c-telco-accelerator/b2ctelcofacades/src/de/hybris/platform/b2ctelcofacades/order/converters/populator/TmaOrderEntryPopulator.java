/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaCartSubscriptionInfoData;
import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcoservices.model.TmaCartSubscriptionInfoModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populator implementation for {@link de.hybris.platform.core.model.order.AbstractOrderEntryModel} as source and
 * {@link de.hybris.platform.commercefacades.order.data.OrderEntryData} as target type.
 *
 * @since 1907
 */
public class TmaOrderEntryPopulator implements Populator<AbstractOrderEntryModel, OrderEntryData>
{

	/**
	 * Underline separator
	 */
	public static final String SEPARATOR = "_";

	/**
	 * SPO code for cart item
	 */
	public static final String SPO_CODE = "SPO";

	private Converter<TmaCartSubscriptionInfoModel, TmaCartSubscriptionInfoData> tmaSubscriptionInfoConverter;
	private Converter<ProductModel, ProductData> productConverter;
	private Converter<TmaCartValidationModel, TmaCartValidationData> cartValidationConverter;

	@Override
	public void populate(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		addId(source, target);
		addProcessType(source, target);
		addBPO(source, target);
		addSubscriptionInfo(source, target);
		addValidation(source, target);
		addContractStartDate(source, target);
	}

	private void addValidation(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		if (source instanceof CartEntryModel)
		{
			final Set<TmaCartValidationModel> cartEntryValidations = ((CartEntryModel) source).getCartEntryValidations();
			if (CollectionUtils.isNotEmpty(cartEntryValidations))
			{
				final Set<TmaCartValidationData> validationDataSet = cartEntryValidations.stream()
						.map(cartEntryValidation -> getCartValidationConverter().convert(cartEntryValidation))
						.collect(Collectors.toSet());
				target.setValidationMessages(validationDataSet);
			}
		}
	}

	protected void addId(final AbstractOrderEntryModel source, final OrderEntryData target)
	{
		if (source.getEntryNumber() != null)
		{
			target.setId(source.getEntryNumber().toString());
		}
	}

	protected void addBPO(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (orderEntry.getBpo() != null)
		{
			entry.setBpo(getProductConverter().convert(orderEntry.getBpo()));
		}
	}

	protected void addProcessType(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (orderEntry.getProcessType() != null)
		{
			entry.setProcessType(orderEntry.getProcessType());
		}
	}

	protected void addSubscriptionInfo(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (orderEntry.getSubscriptionInfo() != null)
		{
			entry.setSubscriptionInfo(
					getTmaSubscriptionInfoConverter().convert(orderEntry.getSubscriptionInfo()));
		}
	}

	private void addContractStartDate(final AbstractOrderEntryModel orderEntry, final OrderEntryData entry)
	{
		if (orderEntry.getContractStartDate() != null)
		{
			entry.setContractStartDate(orderEntry.getContractStartDate());
		}
	}

	protected Converter<TmaCartSubscriptionInfoModel, TmaCartSubscriptionInfoData> getTmaSubscriptionInfoConverter()
	{
		return tmaSubscriptionInfoConverter;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	protected Converter<TmaCartValidationModel, TmaCartValidationData> getCartValidationConverter()
	{
		return cartValidationConverter;
	}

	@Required
	public void setTmaSubscriptionInfoConverter(
			final Converter<TmaCartSubscriptionInfoModel, TmaCartSubscriptionInfoData> tmaSubscriptionInfoConverter)
	{
		this.tmaSubscriptionInfoConverter = tmaSubscriptionInfoConverter;
	}

	@Required
	public void setProductConverter(
			final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	public void setCartValidationConverter(
			final Converter<TmaCartValidationModel, TmaCartValidationData> cartValidationConverter)
	{
		this.cartValidationConverter = cartValidationConverter;
	}
}
