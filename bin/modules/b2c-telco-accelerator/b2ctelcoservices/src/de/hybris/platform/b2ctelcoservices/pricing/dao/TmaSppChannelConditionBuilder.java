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
import de.hybris.platform.europe1.channel.strategies.RetrieveChannelStrategy;
import de.hybris.platform.europe1.enums.PriceRowChannel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.jalo.JaloSession;

import org.springframework.beans.factory.annotation.Required;


/**
 * Search query condition builder for {@link PriceRowModel#DISTRIBUTIONCHANNELS} based on received {@link TmaPriceContext}.
 *
 * @since 1810
 */
public class TmaSppChannelConditionBuilder extends AbstractSubscriptionPricePlanConditionBuilder<TmaPriceContext>
{
	private static final String SUBSCRIPTION_PRICE_PLAN_2_PRICE_ROW_CHANNEL_RELATION_NAME = "SubscriptionPricePlan2PriceRowChannelRelation";

	private RetrieveChannelStrategy retrieveChannelStrategy;

	@Override
	protected boolean shouldApplyCondition(final TmaPriceContext parameter)
	{
		final PriceRowChannel sessionChannel = getSessionChannel();
		return sessionChannel != null;
	}

	@Override
	protected GenericConditionList createQueryConditions(final GenericQuery query, final TmaPriceContext priceContext)
	{
		final PriceRowChannel sessionChannel = getSessionChannel();

		final GenericSearchField channelTargetPk = addRelationJoinToQuery(query,
				SUBSCRIPTION_PRICE_PLAN_2_PRICE_ROW_CHANNEL_RELATION_NAME, SOURCE_RELATION_NAME, PriceRowChannel._TYPECODE,
				TARGET_RELATION_NAME);

		final GenericCondition subscriptionPricePlanMatchChannelCondition = GenericCondition
				.createConditionForValueComparison(channelTargetPk, Operator.EQUAL, sessionChannel);
		final GenericCondition subscriptionPricePlanEmptyChannel = GenericCondition.createIsNullCondition(channelTargetPk);

		return GenericConditionList.or(subscriptionPricePlanMatchChannelCondition, subscriptionPricePlanEmptyChannel);
	}

	/**
	 * Returns the session price row channel.
	 *
	 * @return current price row channel from the session
	 */
	protected PriceRowChannel getSessionChannel()
	{
		return getRetrieveChannelStrategy().getChannel(JaloSession.getCurrentSession().getSessionContext());
	}

	protected RetrieveChannelStrategy getRetrieveChannelStrategy()
	{
		return retrieveChannelStrategy;
	}

	@Required
	public void setRetrieveChannelStrategy(RetrieveChannelStrategy retrieveChannelStrategy)
	{
		this.retrieveChannelStrategy = retrieveChannelStrategy;
	}

}
