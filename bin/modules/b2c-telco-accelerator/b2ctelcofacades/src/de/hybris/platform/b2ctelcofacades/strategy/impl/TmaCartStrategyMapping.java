/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.strategy.impl;

import de.hybris.platform.b2ctelcoservices.order.TmaCartStrategy;

import java.util.Map;

import org.springframework.beans.factory.annotation.Required;


/**
 * Handles mapping between cart update action and its strategy implementation.
 *
 * @since 1907
 * @deprecated since 1911; use the tmaCartStrategyMap bean instead.
 */
@Deprecated(since = "1911", forRemoval= true)
public class TmaCartStrategyMapping
{
	 private Map<String, TmaCartStrategy> cartStrategyMap;

	 public Map<String, TmaCartStrategy> getCartStrategyMap()
	 {
		  return cartStrategyMap;
	 }

	 @Required
	 public void setCartStrategyMap(Map<String, TmaCartStrategy> cartStrategyMap)
	 {
		  this.cartStrategyMap = cartStrategyMap;
	 }
}
