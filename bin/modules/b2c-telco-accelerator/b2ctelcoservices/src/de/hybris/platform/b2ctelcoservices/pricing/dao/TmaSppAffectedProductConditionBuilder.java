/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.europe1.model.PriceRowModel;


/**
 * Search query condition builder for {@link PriceRowModel#AFFECTEDPRODUCTOFFERING}
 * based on received {@link TmaPriceContext}.
 *
 * @since 1810
 */
public class TmaSppAffectedProductConditionBuilder extends AbstractSubscriptionPricePlanConditionBuilder<TmaPriceContext>
{
	@Override
	protected boolean shouldApplyCondition(final TmaPriceContext priceContext)
	{
		return priceContext != null && (productIsBpo(priceContext.getProduct()) && priceContext.getAffectedProduct() != null);
	}

	@Override
	protected GenericConditionList createQueryConditions(final GenericQuery query, final TmaPriceContext priceContext)
	{
		final GenericSearchField affectedProductField = new GenericSearchField(null,
				PriceRowModel.AFFECTEDPRODUCTOFFERING);
		final GenericCondition affectedProductCondition = GenericCondition
				.createConditionForValueComparison(affectedProductField, Operator.EQUAL, priceContext.getAffectedProduct(),
						PriceRowModel.AFFECTEDPRODUCTOFFERING);

		return GenericConditionList.createConditionList(affectedProductCondition);
	}
}
