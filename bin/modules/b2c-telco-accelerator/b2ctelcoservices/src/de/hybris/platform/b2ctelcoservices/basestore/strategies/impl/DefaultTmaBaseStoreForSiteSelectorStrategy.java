/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.basestore.strategies.impl;

import de.hybris.platform.b2ctelcoservices.basestore.strategies.TmaBaseStoreForSiteSelectorStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.store.BaseStoreModel;


/**
 * Default implementation of {@link TmaBaseStoreForSiteSelectorStrategy} which returns first base store from collection.
 *
 * @since 2003
 *
 */
public class DefaultTmaBaseStoreForSiteSelectorStrategy implements TmaBaseStoreForSiteSelectorStrategy
{

	@Override
	public BaseStoreModel getBaseStore(final BaseSiteModel baseSiteModel)
	{
		return baseSiteModel.getStores().get(0);
	}
}
