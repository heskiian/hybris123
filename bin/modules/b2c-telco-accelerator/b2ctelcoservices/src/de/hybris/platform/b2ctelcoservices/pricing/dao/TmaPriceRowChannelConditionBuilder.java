/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.pricing.dao;

import de.hybris.platform.b2ctelcoservices.daos.TmaSearchQueryException;
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
 * Search query condition builder for {@link PriceRowModel#CHANNEL} based on received {@link TmaPriceContext}.
 *
 * @since 1903
 * @deprecated since 2007. Use instead {@link TmaSppChannelConditionBuilder}.
 */
@Deprecated(since = "2007")
public class TmaPriceRowChannelConditionBuilder extends AbstractSubscriptionPricePlanConditionBuilder<TmaPriceContext>
{

	private RetrieveChannelStrategy retrieveChannelStrategy;

	@Override
	protected boolean shouldApplyCondition(final TmaPriceContext priceContext)
	{
		final PriceRowChannel sessionChannel = getSessionChannel();
		return sessionChannel != null;
	}

	@Override
	protected GenericConditionList createQueryConditions(GenericQuery query, TmaPriceContext priceContext)
			throws TmaSearchQueryException
	{
		final GenericSearchField channelSearchField = new GenericSearchField(null, PriceRowModel.CHANNEL);
		final GenericCondition channelSearchCondition = GenericCondition
				.createConditionForValueComparison(channelSearchField, Operator.EQUAL, getSessionChannel());
		final GenericCondition channelIsNullCondition = GenericCondition.createIsNullCondition(channelSearchField);
		return GenericConditionList.or(channelSearchCondition, channelIsNullCondition);
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
