/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.pricing.dao;

import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Search query condition builder for {@link PriceRowModel#REGION} based on received
 * {@link TmaPriceContext}.
 *
 * @since 1907
 */
public class TmaPriceRegionConditionBuilder extends AbstractSubscriptionPricePlanConditionBuilder<TmaPriceContext>
{

	@Override
	protected boolean shouldApplyCondition(final TmaPriceContext parameter)
	{
		final Set<RegionModel> regions = parameter.getRegions();
		return CollectionUtils.isNotEmpty(regions);
	}

	@Override
	protected GenericConditionList createQueryConditions(final GenericQuery query, final TmaPriceContext priceContext)
	{
		final Set<RegionModel> regions = priceContext.getRegions();
		final GenericSearchField regionTargetPk = addRelationJoinToQuery(query, RegionModel._PDTROW2REGION,
				SOURCE_RELATION_NAME, RegionModel._TYPECODE, TARGET_RELATION_NAME);
		final GenericCondition subscriptionPricePlanMatchRegionCondition = GenericCondition
				.createConditionForValueComparison(regionTargetPk, Operator.IN, regions);
		final GenericCondition subscriptionPricePlanEmptyRegionCondition = GenericCondition.createIsNullCondition(regionTargetPk);
		return GenericConditionList.or(subscriptionPricePlanMatchRegionCondition, subscriptionPricePlanEmptyRegionCondition);
	}
}
