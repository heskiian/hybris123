/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.List;


/**
 * Data Access object for operations related to {@link SubscriptionTermModel}.
 *
 * @since 1810
 */
public interface TmaSubscriptionTermDao extends GenericDao<SubscriptionTermModel>
{
	/**
	 * Returns all the {@link SubscriptionTermModel}s.
	 *
	 * @return configured subscription terms.
	 */
	List<SubscriptionTermModel> findAllSubscriptionTerms();
}
