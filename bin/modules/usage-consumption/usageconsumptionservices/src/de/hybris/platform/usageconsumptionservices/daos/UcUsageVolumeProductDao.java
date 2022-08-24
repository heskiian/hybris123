/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.usageconsumptionservices.daos;

import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import de.hybris.platform.usageconsumptionservices.data.UcUsageVolumeProductContext;
import de.hybris.platform.usageconsumptionservices.model.UcUsageVolumeProductModel;

import java.util.List;


/**
 * Data access object for {@link UcUsageVolumeProductModel}s.
 *
 * @since 2108
 */
public interface UcUsageVolumeProductDao extends GenericDao<UcUsageVolumeProductModel>
{
	/**
	 * Retrieves a list of {@link UcUsageVolumeProductModel} for a given context.
	 *
	 * @param ucUsageVolumeProductContext
	 * 		the context.
	 * @param offset
	 * 		the offset.
	 * @param limit
	 * 		the maximum number of returned usage volume products.
	 * @return the list of usage volume products found for the given context.
	 */
	List<UcUsageVolumeProductModel> getUsageVolumeProducts(final UcUsageVolumeProductContext ucUsageVolumeProductContext,
			final Integer offset, final Integer limit);

	/**
	 * Retrieves the total number of usage volume products found for a given context.
	 *
	 * @param ucUsageVolumeProductContext
	 * 		the context.
	 * @return the number of usage volume products found.
	 */
	Integer getNumberOfUsageVolumeProductFor(final UcUsageVolumeProductContext ucUsageVolumeProductContext);
}
