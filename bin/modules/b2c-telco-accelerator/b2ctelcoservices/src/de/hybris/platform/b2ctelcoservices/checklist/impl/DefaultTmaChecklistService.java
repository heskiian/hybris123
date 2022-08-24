/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.checklist.impl;


import de.hybris.platform.b2ctelcoservices.checklist.TmaChecklistService;
import de.hybris.platform.b2ctelcoservices.checklist.context.TmaChecklistContext;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyEngine;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.impl.TmaPolicyContextBuilder;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaCartValidationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaChecklistService}
 *
 * @since 1907
 */
public class DefaultTmaChecklistService implements TmaChecklistService
{
	private TmaPolicyEngine policyEngine;
	private ModelService modelService;

	@Override
	public Set<RuleEvaluationResult> findActions(final TmaChecklistContext checklistContext)
	{
		return getPolicies(getRequestParamsFromContext(checklistContext));
	}

	@Override
	public boolean areActionsFulfilled(final AbstractOrderModel order)
	{
		cleanUpCartEntriesValidation(order);

		final List<TmaPolicyContext> contexts = createContexts(order.getEntries());
		final Set<RuleEvaluationResult> results = getPolicyEngine().findPolicies(contexts,
				Collections.singleton(TmaCompatibilityPolicyActionType.CHECKLIST));
		getPolicyEngine().applyActions(results);

		return order.getEntries().stream()
				.allMatch(entry -> CollectionUtils.isEmpty(((CartEntryModel) entry).getCartEntryValidations()));
	}

	protected List<TmaPolicyContext> createContexts(final List<AbstractOrderEntryModel> orderEntries)
	{
		final List<AbstractOrderEntryModel> orderEntriesWithPo = orderEntries.stream()
				.filter(orderEntry -> orderEntry.getProduct() instanceof TmaProductOfferingModel).collect(Collectors.toList());

		return orderEntriesWithPo.stream()
				.map(entry -> TmaPolicyContextBuilder.newPolicyContextBuilder()
						.withProductOfferings(Arrays.asList((TmaProductOfferingModel) entry.getProduct()))
						.withProcessType(entry.getProcessType())
						.withQuantity(Math.toIntExact(entry.getQuantity()))
						.withOrderEntry(entry).build()).collect(Collectors.toList());
	}

	protected void cleanUpCartEntriesValidation(final AbstractOrderModel order)
	{
		order.getEntries().stream().map(entry -> (CartEntryModel) entry).forEach(cartEntry -> {
			final Set<TmaCartValidationModel> cartEntryValidations = cartEntry.getCartEntryValidations();
			if (CollectionUtils.isNotEmpty(cartEntryValidations))
			{
				cartEntry.setCartEntryValidations(Collections.emptySet());
				getModelService().save(cartEntry);
				getModelService().removeAll(cartEntryValidations);
			}
		});
	}

	/**
	 * This method creates a list of request params from a checklist context.
	 *
	 * @param checklistContext
	 *           the context containing the data
	 * @return list of {@link TmaPolicyContext}
	 */
	protected List<TmaPolicyContext> getRequestParamsFromContext(final TmaChecklistContext checklistContext)
	{
		final List<TmaPolicyContext> requestParams = new ArrayList<>();
		if (CollectionUtils.isEmpty(checklistContext.getProductOfferings()))
		{
			return requestParams;
		}

		final List<ProductModel> contextProductOfferings = checklistContext.getProductOfferings();
		contextProductOfferings.forEach(product -> {
			final TmaPolicyContext requestParam = TmaPolicyContextBuilder.newPolicyContextBuilder()
					.withProductOfferings(Arrays.asList((TmaProductOfferingModel) product))
					.withQuantity(1)
					.withProcessType(checklistContext.getProcessType()).build();

			requestParams.add(requestParam);
		});

		return requestParams;
	}

	/**
	 * Finds policies compatible with the request parameters and Checklist policy action type.
	 *
	 * @param compatibilityRequestParams
	 *           request parameters
	 * @return list of {@link RuleEvaluationResult}
	 */
	protected Set<RuleEvaluationResult> getPolicies(final List<TmaPolicyContext> compatibilityRequestParams)
	{
		final Set<RuleEvaluationResult> results = getPolicyEngine().findPolicies(compatibilityRequestParams,
				new HashSet<>(Collections.singletonList(TmaCompatibilityPolicyActionType.CHECKLIST)));
		return results.stream().filter(result -> CollectionUtils.isNotEmpty(result.getContexts())).collect(Collectors.toSet());
	}

	protected TmaPolicyEngine getPolicyEngine()
	{
		return policyEngine;
	}

	@Required
	public void setPolicyEngine(final TmaPolicyEngine policyEngine)
	{
		this.policyEngine = policyEngine;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}
}

