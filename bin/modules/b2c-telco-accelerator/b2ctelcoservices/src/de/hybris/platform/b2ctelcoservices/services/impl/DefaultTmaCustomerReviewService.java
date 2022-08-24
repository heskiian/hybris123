/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.services.impl;

import de.hybris.platform.b2ctelcoservices.daos.TmaCustomerReviewDao;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerReviewService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.customerreview.impl.DefaultCustomerReviewService;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

import java.util.List;


/**
 * Default implementation for {@link TmaCustomerReviewService}.
 *
 * @since 1907
 */
public class DefaultTmaCustomerReviewService extends DefaultCustomerReviewService implements TmaCustomerReviewService
{
	@Override
	public List<CustomerReviewModel> getReviewsForProductByLimitOffsetAndLanguage(ProductModel product, int offset, int limit,
			LanguageModel language)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("product", product);
		ServicesUtil.validateParameterNotNullStandardMessage("language", language);
		return this.getCustomerReviewDao().getReviewsForProductByLimitOffsetAndLanguage(product, offset, limit, language);
	}

	@Override
	public Integer getNumberOfReviewsForProductAndLanguage(ProductModel product, LanguageModel language)
	{
		ServicesUtil.validateParameterNotNullStandardMessage("product", product);
		ServicesUtil.validateParameterNotNullStandardMessage("language", language);
		return this.getCustomerReviewDao().getNumberOfReviewsForProductAndLanguage(product, language);
	}

	@Override
	protected TmaCustomerReviewDao getCustomerReviewDao()
	{
		return (TmaCustomerReviewDao) super.getCustomerReviewDao();
	}
}
