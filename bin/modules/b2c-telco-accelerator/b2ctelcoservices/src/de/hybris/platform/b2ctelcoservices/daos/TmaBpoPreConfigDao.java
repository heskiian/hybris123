/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;


/**
 * Data Access Objects related to {@link TmaBpoPreConfigModel}
 *
 * @since 6.7
 */
public interface TmaBpoPreConfigDao
{

	/**
	 * Retrieves {@link TmaBpoPreConfigModel} by the given code
	 *
	 * @param code
	 *           Unique identifier for the {@link TmaBpoPreConfigModel}
	 * @param catalogVersion
	 *           The Catalog version of the instance to be fetched
	 * @return {@link TmaBpoPreConfigModel}
	 */
	TmaBpoPreConfigModel findBpoPreconfigByCode(String code, CatalogVersionModel catalogVersion);

}
