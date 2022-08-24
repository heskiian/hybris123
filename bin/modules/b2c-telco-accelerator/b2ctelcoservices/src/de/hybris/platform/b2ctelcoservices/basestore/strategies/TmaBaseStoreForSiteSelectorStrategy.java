/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.basestore.strategies;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.store.BaseStoreModel;


/**
 * Strategy for retrieving base store from the base site.
 *
 * @since 2003
 *
 */
public interface TmaBaseStoreForSiteSelectorStrategy
{

	/**
	 * Retrieves the base store for given BaseSite
	 *
	 * @param baseSiteModel
	 * @return the base store
	 */
	BaseStoreModel getBaseStore(BaseSiteModel baseSiteModel);

}
