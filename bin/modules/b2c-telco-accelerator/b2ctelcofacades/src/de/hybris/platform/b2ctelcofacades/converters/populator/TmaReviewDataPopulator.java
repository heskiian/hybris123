/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.commercefacades.product.converters.populator.CustomerReviewPopulator;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.customerreview.model.CustomerReviewModel;


/**
 * Populates {@link ReviewData} based on {@link CustomerReviewModel}.
 *
 * @since 1907
 */
public class TmaReviewDataPopulator extends CustomerReviewPopulator
{
	@Override
	public void populate(final CustomerReviewModel reviewModel, final ReviewData reviewData)
	{
		super.populate(reviewModel, reviewData);

		if (reviewModel.getLanguage() != null)
		{
			reviewData.setLanguage(reviewModel.getLanguage().getName());
		}

		if (reviewModel.getApprovalStatus() != null)
		{
			reviewData.setLifecycleStatus(reviewModel.getApprovalStatus().getCode());
		}
	}
}