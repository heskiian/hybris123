/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaRemainingCommittedPeriodPolicyStatementModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;


/**
 * Statement validator for {@link TmaRemainingCommittedPeriodPolicyStatementModel} that checks if the
 * remaining committed period of the customer subscription is less than the period mentioned by policy statement.
 *
 * @since 1810
 */
public class TmaRemainingCommittedPeriodStatementValidator extends AbstractTmaPolicyStatementValidator
{

	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaRemainingCommittedPeriodPolicyStatementModel))
		{
			return contexts;
		}

		final List<TmaPolicyContext> subscribedPoRequestParams = contexts.stream()
				.filter(TmaPolicyContext::isSubscribed).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(subscribedPoRequestParams))
		{
			// no subscribed products, statement is not valid
			return Collections.emptyList();
		}

		final List<TmaPolicyContext> validSubscribedPoRequestParams
				= subscribedPoRequestParams.stream().filter(param -> param.getSubscriptionEndDate() != null)
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(validSubscribedPoRequestParams))
		{
			//no end date found
			return Collections.emptyList();
		}

		final List<TmaPolicyContext> validContexts = new ArrayList<>();
		final int remainingDays = ((TmaRemainingCommittedPeriodPolicyStatementModel) statement).getDays();
		for (final TmaPolicyContext param : subscribedPoRequestParams)
		{
			final Date subscriptionEndDate = param.getSubscriptionEndDate();
			final Date currentDate = new Date();
			boolean isValid;

			if (remainingDays == 0)
			{
				isValid = DateUtils.isSameDay(currentDate,subscriptionEndDate) && currentDate.compareTo(subscriptionEndDate) < 1;
			}
			else
			{
				final Date intervalStartDate = DateUtils.addDays(subscriptionEndDate, -remainingDays);
				isValid = currentDate.after(intervalStartDate) && currentDate.before(subscriptionEndDate);
			}

			if (isValid)
			{
				validContexts.add(param);
			}
		}
		return validContexts;
	}
}
