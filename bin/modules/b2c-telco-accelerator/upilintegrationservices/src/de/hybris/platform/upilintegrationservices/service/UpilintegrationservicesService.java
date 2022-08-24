/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.upilintegrationservices.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;


/**
 * UpilintegrationservicesService interface to perform Upil related operations
 *
 * @since 1911
 */
public interface UpilintegrationservicesService
{
	/**
	 * Sync ISU configurations with Hybris
	 *
	 * @param catalogVersionModel
	 *
	 * @return Boolean
	 *         Returns true if sync is success otherwise it will return false.
	 */
	Boolean syncIsuConfigWithTua(CatalogVersionModel catalogVersionModel);
}
