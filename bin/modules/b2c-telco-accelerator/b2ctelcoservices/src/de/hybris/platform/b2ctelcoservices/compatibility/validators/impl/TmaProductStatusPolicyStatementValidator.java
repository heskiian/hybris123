/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.jalo.TmaProductStatusPolicyStatement;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductStatusPolicyStatementModel;
import de.hybris.platform.subscriptionservices.enums.SubscriptionStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Statement validator for {@link TmaProductStatusPolicyStatement} that checks if the status of the subscription is same
 * as status mentioned by policy statement.
 *
 * @since 2105
 */
public class TmaProductStatusPolicyStatementValidator extends AbstractTmaPolicyStatementValidator
{
	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaProductStatusPolicyStatementModel))
		{
			return contexts;
		}
		final List<TmaPolicyContext> subscribedProductPolicyContext = contexts.stream().filter(TmaPolicyContext::isSubscribed)
				.collect(Collectors.toList());
		if (CollectionUtils.isEmpty(subscribedProductPolicyContext))
		{
			return Collections.emptyList();
		}
		final List<TmaPolicyContext> validContexts = new ArrayList<>();
		final Set<SubscriptionStatus> statuses = ((TmaProductStatusPolicyStatementModel) statement).getStatuses();
		for (final TmaPolicyContext param : subscribedProductPolicyContext)
		{
			for (final SubscriptionStatus status : statuses)
			{
				if (StringUtils.equalsIgnoreCase(param.getSubscribedProduct().getSubscriptionStatus(), status.getCode()))
				{
					validContexts.add(param);
					break;
				}
			}
		}
		return validContexts;
	}
}
