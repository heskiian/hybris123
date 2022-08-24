/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao.impl.TmaSppEligibleProcessesAndTermsConditionBuilder;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.europe1.model.PriceRowModel;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;


/**
 * Search query condition builder for {@link PriceRowModel#ProcessType} based on received
 * {@link TmaPriceContext}.
 *
 * @since 1907
 * @deprecated 1911, Use {@link TmaSppEligibleProcessesAndTermsConditionBuilder} as it is updated with process type and
 *             eligibility context.
 */
@Deprecated(since = "1911", forRemoval= true)
public class TmaProcessTypeConditionBuilder extends AbstractSubscriptionPricePlanConditionBuilder<TmaPriceContext>
{

	private static final String SUBSCRIPTION_PRICE_PLAN_2_PROCESS_TYPE_RELATION_NAME = "SubscriptionPricePlan2TmaProcessTypeRelation";

	@Override
	protected boolean shouldApplyCondition(final TmaPriceContext parameter)
	{
		final Set<TmaProcessType> processTypes = parameter.getProcessTypes();
		return !CollectionUtils.isEmpty(processTypes);
	}

	@Override
	protected GenericConditionList createQueryConditions(final GenericQuery query, final TmaPriceContext priceContext)
	{
		final Set<TmaProcessType> processTypes = priceContext.getProcessTypes();
		final GenericSearchField processTypeTargetPk = addRelationJoinToQuery(query,
				SUBSCRIPTION_PRICE_PLAN_2_PROCESS_TYPE_RELATION_NAME,
				SOURCE_RELATION_NAME, TmaProcessType._TYPECODE, TARGET_RELATION_NAME);
		final GenericCondition subscriptionPricePlanMatchProcessTypeCondition = GenericCondition
				.createConditionForValueComparison(processTypeTargetPk, Operator.IN, processTypes);
		final GenericCondition subscriptionPricePlanEmptyProcessTypeCondition = GenericCondition
				.createIsNullCondition(processTypeTargetPk);
		return GenericConditionList.or(subscriptionPricePlanMatchProcessTypeCondition,
				subscriptionPricePlanEmptyProcessTypeCondition);
	}
}
