/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos;

import de.hybris.platform.b2ctelcoservices.data.TmaProductOfferingSearchContext;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.jalo.SearchResult;

import java.util.Collection;


/**
 * Data Access Object for operations related to the @{TmaProductOfferingModel} type
 *
 * @since 1907
 *
 * @deprecated since 1911 - use ExportProductService instead
 */
@Deprecated(since = "1911", forRemoval= true)
public interface TmaProductOfferingDao
{
	/**
	 * Finds the {@link TmaProductOfferingModel} for a given collection {@link CatalogVersionModel} and
	 * {@link ProductOfferingSearchContext}
	 *
	 * @param catalogVersions
	 *           catalog versions used to query for products
	 * @param productOfferingSearchContext
	 *           product search context object stores all attributes for search
	 * @return page of {@link TmaProductOfferingModel} wrapped on {@link SearchResult}
	 */
	public SearchPageData<TmaProductOfferingModel> getProductOfferings(final Collection<CatalogVersionModel> catalogVersions,
			final TmaProductOfferingSearchContext productOfferingSearchContext);
}
