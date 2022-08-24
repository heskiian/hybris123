/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.cart.impl;

import de.hybris.platform.b2ctelcofacades.data.TmaCartStrategyType;
import de.hybris.platform.b2ctelcofacades.order.impl.DefaultTmaCartFacade;
import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.impl.DefaultCartFacade;
import de.hybris.platform.commerceservices.order.CommerceSaveCartTextGenerationStrategy;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import java.util.Map;


/**
 * Extension of {@link DefaultCartFacade} for Telco
 *
 * @since 2003
 * @deprecated since 2102. Use instead {@link DefaultTmaCartFacade}.
 *
 *
 */
@Deprecated(since = "2102")
public class DefaultTmaWebServicesCartFacade extends DefaultTmaCartFacade
{
	/**
	 * @param cartStrategyMap
	 * @param saveCartTextGenerationStrategy
	 * @param configurationService
	 * @param enumerationService
	 */
	public DefaultTmaWebServicesCartFacade(final Map<TmaCartStrategyType, TmaCartStrategy> cartStrategyMap,
			final CommerceSaveCartTextGenerationStrategy saveCartTextGenerationStrategy,
			final ConfigurationService configurationService, final EnumerationService enumerationService)
	{
		super(cartStrategyMap, saveCartTextGenerationStrategy, configurationService, enumerationService);

	}

	@Override
	public CartData getSessionCart()
	{
		final CartData cartData;
		final CartModel cart = getCartService().getSessionCart();
		cartData = getCartConverter().convert(cart);
		return cartData;
	}

}
