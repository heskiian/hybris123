/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaProductOfferingDao;
import de.hybris.platform.b2ctelcoservices.data.TmaProductOfferingSearchContext;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.servicelayer.data.PaginationData;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchParameter;
import de.hybris.platform.servicelayer.search.paginated.PaginatedFlexibleSearchService;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link TmaProductOfferingDao}.
 * 
 * @since 1907
 *
 * @deprecated since 1911 - use DefaultExportProductService instead
 */
@Deprecated(since = "1911", forRemoval= true)
public class DefaultTmaProductOfferingDao implements TmaProductOfferingDao
{
	protected static final String PRODUCT_OFFERING_QUERY = "SELECT {" + TmaProductOfferingModel.PK + "} FROM {"
			+ TmaProductOfferingModel._TYPECODE + "} WHERE {" + TmaProductOfferingModel.CATALOGVERSION + "} IN (?catalogVersions)";
	protected static final String MODFIEDTIME_QUERY = PRODUCT_OFFERING_QUERY + "AND {" + TmaProductOfferingModel.MODIFIEDTIME
			+ "} > ?modifiedTime";
	private PaginatedFlexibleSearchService paginatedFlexibleSearchService;

	@Override
	public SearchPageData<TmaProductOfferingModel> getProductOfferings(final Collection<CatalogVersionModel> catalogVersions,
			final TmaProductOfferingSearchContext productOfferingSearchContext)
	{
		final PaginatedFlexibleSearchParameter parameter = new PaginatedFlexibleSearchParameter();
		final SearchPageData searchPageData = new SearchPageData();
		final PaginationData pagination = new PaginationData();
		FlexibleSearchQuery query = null;
		pagination.setCurrentPage(productOfferingSearchContext.getCurrentPage());
		pagination.setPageSize(productOfferingSearchContext.getPageSize());
		pagination.setNeedsTotal(true);
		searchPageData.setPagination(pagination);
		parameter.setSearchPageData(searchPageData);

		if (productOfferingSearchContext.getTimestamp() == null)
		{
			query = new FlexibleSearchQuery(PRODUCT_OFFERING_QUERY);
		}
		else
		{
			query = new FlexibleSearchQuery(MODFIEDTIME_QUERY);
			query.addQueryParameter("modifiedTime", productOfferingSearchContext.getTimestamp());

		}
		query.addQueryParameter("catalogVersions", catalogVersions);
		parameter.setFlexibleSearchQuery(query);
		return getPaginatedFlexibleSearchService().search(parameter);
	}

	protected PaginatedFlexibleSearchService getPaginatedFlexibleSearchService()
	{
		return paginatedFlexibleSearchService;
	}

	@Required
	public void setPaginatedFlexibleSearchService(final PaginatedFlexibleSearchService paginatedFlexibleSearchService)
	{
		this.paginatedFlexibleSearchService = paginatedFlexibleSearchService;
	}
}
