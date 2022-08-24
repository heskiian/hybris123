/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.impl;

import de.hybris.platform.b2ctelcoservices.enums.TmaAbstractOrderPriceType;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderOneTimeChargePriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.DefaultTmaAbstractOrderCartPriceBuilder;
import de.hybris.platform.b2ctelcoservices.pricing.strategy.builder.order.TmaAbstractOrderPriceBuilder;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.TaxValue;

import java.util.List;

import org.springframework.beans.factory.annotation.Required;


/**
 * Builder that creates a list of {@link TmaAbstractOrderOneTimeChargePriceModel} to be set on the order
 *
 * @since 1907
 */
public class DefaultTmaAbstractOrderPriceBuilder extends DefaultTmaAbstractOrderCartPriceBuilder
		implements TmaAbstractOrderPriceBuilder
{
	private ModelService modelService;

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	private ConfigurationService configurationService;

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	private KeyGenerator keyGenerator;

	@Override
	public TmaAbstractOrderPriceModel buildPrice(final Double price, final AbstractOrderModel order,
			final List<TaxValue> taxes, final TmaAbstractOrderPriceType priceType)
	{
		final TmaAbstractOrderOneTimeChargePriceModel orderPrice =
				getModelService().create(TmaAbstractOrderOneTimeChargePriceModel.class);

		setPriceValues(price, order, orderPrice, retrieveTaxRate(price, order, taxes));
		orderPrice.setBillingTime(getBillingTimeService().getDefaultBillingTime());
		orderPrice.setPriceType(priceType);
		orderPrice.setCurrency(order.getCurrency());
		getModelService().save(orderPrice);
		return orderPrice;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	protected KeyGenerator getKeyGenerator()
	{
		return keyGenerator;
	}

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	@Required
	public void setKeyGenerator(KeyGenerator keyGenerator)
	{
		this.keyGenerator = keyGenerator;
	}

	/**
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	/**
	 * @deprecated since 2007
	 */
	@Required
	@Deprecated(since = "2007")
	public void setConfigurationService(ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}


}
