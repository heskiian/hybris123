/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.order.converters.populator;

import de.hybris.platform.b2ctelcofacades.order.TmaOrderEntryFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.core.model.order.CartModel;


/**
 * Extended Cart populator with TMA specific details.
 *
 * @since 2102
 */
public class TmaExtendedHierarchicalCartPopulator extends TmaHierarchicalCartPopulator
{
	public TmaExtendedHierarchicalCartPopulator(final TmaOrderEntryFacade orderEntryFacade)
	{
		super(orderEntryFacade);
	}

	@Override
	public void populate(final CartModel source, final CartData target)
	{
		super.populate(source, target);
		addDeliveryAddress(source, target);
		addPaymentInformation(source, target);
		addDeliveryMethod(source, target);
		addPrincipalInformation(source, target);
	}
}
