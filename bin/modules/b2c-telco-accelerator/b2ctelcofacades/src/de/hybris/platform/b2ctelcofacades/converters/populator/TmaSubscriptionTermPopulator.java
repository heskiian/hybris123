/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.subscriptionfacades.converters.populator.SubscriptionTermPopulator;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;


/**
 * Enhancing the default {@link SubscriptionTermPopulator} to populate the id on the {@link SubscriptionTermData}.
 *
 * @since 1805
 */
public class TmaSubscriptionTermPopulator<SOURCE extends SubscriptionTermModel, TARGET extends SubscriptionTermData>
		extends SubscriptionTermPopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		super.populate(source, target);
		target.setId(source.getId());
	}
}
