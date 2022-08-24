/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.services;

import de.hybris.platform.b2ctelcoservices.model.TmaAbstractOrderPriceModel;
import de.hybris.platform.order.CalculationService;


/**
 * Service handling cart calculation operations.
 *
 * @since 2003
 * @deprecated since 2007
 */
@Deprecated(since = "2007")
public interface TmaCalculationService extends CalculationService
{
	/**
	 * Creates a composite cart price for storing prices.
	 *
	 * @return The composite price created
	 * @deprecated since 2007
	 */
	@Deprecated(since = "2007")
	TmaAbstractOrderPriceModel createCartPrice();
}
