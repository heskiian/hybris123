/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyActionStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaValidationMessagesStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Strategy to process compatibility actions
 *
 * @since 1911
 */
public class TmaCompatibilityPolicyActionStrategy implements TmaPolicyActionStrategy
{
	private TmaPolicyActionResolver policyActionResolver;
	private TmaValidationMessagesStrategy validationMessagesStrategy;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2011", forRemoval = true)
	private EntryGroupService entryGroupService;

	/**
	 * @deprecated since 2011 - saving the validation messages has been moved to TmaValidationMessageStrategy
	 */
	@Deprecated(since = "2011", forRemoval = true)
	private ModelService modelService;

	public TmaCompatibilityPolicyActionStrategy(final TmaPolicyActionResolver policyActionResolver,
			final ModelService modelService, final EntryGroupService entryGroupService,
			final TmaValidationMessagesStrategy validationMessagesStrategy)
	{
		this.policyActionResolver = policyActionResolver;
		this.modelService = modelService;
		this.entryGroupService = entryGroupService;
		this.validationMessagesStrategy = validationMessagesStrategy;
	}

	@Override
	public void processActions(final Set<RuleEvaluationResult> results)
	{
		final TmaPolicyContext context = getInitialContext(results);
		if (context == null)
		{
			return;
		}

		if (context.getOrderEntry() == null)
		{
			return;
		}

		final AbstractOrderModel order = context.getOrderEntry().getOrder();

		final AbstractOrderEntryModel parentEntry = getParentEntry(context.getOrderEntry());

		if (parentEntry == null)
		{
			getValidationMessagesStrategy().cleanupValidationMessagesOn((CartModel) order);
		}

		final Set<RuleEvaluationResult> applicableResults = results.stream()
				.filter(result -> CollectionUtils.isEmpty(result.getContexts())).collect(Collectors.toSet());

		final List<TmaPolicyActionModel> policyActions = applicableResults.stream().map(RuleEvaluationResult::getAction)
				.collect(Collectors.toList());

		getPolicyActionResolver().processPolicyActions(order, (CartEntryModel) parentEntry, policyActions);
	}

	protected TmaPolicyContext getInitialContext(Set<RuleEvaluationResult> results)
	{
		final RuleEvaluationResult firstResult = results.iterator().next();
		final List<TmaPolicyContext> initialContexts = firstResult.getInitialContexts();

		if (CollectionUtils.isNotEmpty(initialContexts))
		{
			return initialContexts.iterator().next();
		}

		return null;
	}

	/**
	 * Returns the master entry of the entry, if there is one. If not, it is returning null.
	 *
	 * @param entry
	 * 		The entry
	 * @return The parent entry
	 */
	protected AbstractOrderEntryModel getParentEntry(final AbstractOrderEntryModel entry)
	{
		if (!(entry.getProduct() instanceof TmaBundledProductOfferingModel) && entry.getMasterEntry() == null)
		{
			return null;
		}

		return entry.getMasterEntry() == null ? entry : entry.getMasterEntry();
	}

	protected TmaPolicyActionResolver getPolicyActionResolver()
	{
		return policyActionResolver;
	}

	/**
	 * @deprecated since 2011 - saving the validation messages has been moved to TmaValidationMessageStrategy
	 */
	@Deprecated(since = "2011", forRemoval = true)
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2011", forRemoval = true)
	protected EntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	protected TmaValidationMessagesStrategy getValidationMessagesStrategy()
	{
		return validationMessagesStrategy;
	}
}
