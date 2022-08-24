/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaCustomerReviewDao;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.customerreview.dao.impl.DefaultCustomerReviewDao;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.Collections;
import java.util.List;


/**
 * Default implementation for {@link TmaCustomerReviewDao}.
 *
 * @since 1907
 */
public class DefaultTmaCustomerReviewDao extends DefaultCustomerReviewDao implements TmaCustomerReviewDao
{
	final String QUERY_BY_PRODUCT_OFFSET_LIMIT_LANGUAGE =
		"SELECT {" + Item.PK + "} FROM {" + "CustomerReview" + "} WHERE {" + "product" + "}=?product AND {" + "language" +
				"}=?language ORDER BY {" +
				"creationtime" + "} DESC LIMIT " + "?limit" + " OFFSET " + "?offset ;";

	@Override
	public List<CustomerReviewModel> getReviewsForProductByLimitOffsetAndLanguage(ProductModel product, int offset,
			int limit, LanguageModel language)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("product", product);

		FlexibleSearchQuery fsQuery = new FlexibleSearchQuery(QUERY_BY_PRODUCT_OFFSET_LIMIT_LANGUAGE);
		fsQuery.addQueryParameter("product", product);
		fsQuery.addQueryParameter("language", language);
		fsQuery.addQueryParameter("limit", limit);
		fsQuery.addQueryParameter("offset", offset);
		fsQuery.setResultClassList(Collections.singletonList(CustomerReviewModel.class));
		SearchResult<CustomerReviewModel> searchResult = this.getFlexibleSearchService().search(fsQuery);
		return searchResult.getResult();
	}

	@Override
	public Integer getNumberOfReviewsForProductAndLanguage(ProductModel product, LanguageModel language) {
		ServicesUtil.validateParameterNotNullStandardMessage("product", product);
		FlexibleSearchQuery fsQuery = new FlexibleSearchQuery("SELECT count(*) FROM {CustomerReview} WHERE {product} = ?product AND {"
				+ "language" + "}=?language");
		fsQuery.addQueryParameter("product", product);
		fsQuery.addQueryParameter("language", language);
		fsQuery.setResultClassList(Collections.singletonList(Integer.class));
		SearchResult<Integer> searchResult = this.getFlexibleSearchService().search(fsQuery);
		return (Integer)searchResult.getResult().iterator().next();
	}
}
