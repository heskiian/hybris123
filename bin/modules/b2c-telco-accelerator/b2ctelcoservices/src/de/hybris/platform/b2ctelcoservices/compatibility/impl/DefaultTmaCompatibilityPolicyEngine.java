/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEngine;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaCompatibilityPolicyEvaluationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyActionResolver;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyEngine;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaPolicyStatementValidationStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.TmaValidationMessagesStrategy;
import de.hybris.platform.b2ctelcoservices.compatibility.data.RuleEvaluationResult;
import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.order.TmaAbstractOrderEntryService;
import de.hybris.platform.b2ctelcoservices.order.TmaEntryGroupService;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.order.EntryGroup;
import de.hybris.platform.order.EntryGroupService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of {@link TmaCompatibilityPolicyEngine}.
 *
 * @since 6.7
 */
public class DefaultTmaCompatibilityPolicyEngine implements TmaCompatibilityPolicyEngine
{
	private static final Logger LOG = Logger.getLogger(DefaultTmaCompatibilityPolicyEngine.class);

	private TmaCompatibilityPolicyEvaluationStrategy policyEvaluationStrategy;
	private Map<TmaCompatibilityPolicyActionType, TmaPolicyActionResolver> policyActionResolversMap;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private EntryGroupService entryGroupService;

	private TmaPoService poService;
	private ModelService modelService;
	private UserService userService;
	private TmaCustomerInventoryService customerInventoryService;
	private TmaPolicyEngine tmaPolicyEngine;
	private TmaPolicyStatementValidationStrategy tmaPolicyStatementValidationStrategy;
	private final TmaValidationMessagesStrategy tmaValidationMessagesStrategy;

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	private TmaEntryGroupService tmaEntryGroupService;

	private final TmaAbstractOrderEntryService entryService;

	public DefaultTmaCompatibilityPolicyEngine(final TmaAbstractOrderEntryService entryService,
			final TmaValidationMessagesStrategy tmaValidationMessagesStrategy)
	{
		this.entryService = entryService;
		this.tmaValidationMessagesStrategy = tmaValidationMessagesStrategy;
	}

	@Override
	public void verifyCompatibilityPolicies(final AbstractOrderModel orderModel, final EntryGroup entryGroup)
	{
		final List<TmaPolicyContext> contexts = new ArrayList<>();

		orderModel.getEntries().stream().filter(entry -> entry.getEntryGroupNumbers().contains(entryGroup.getGroupNumber()))
				.forEach((final AbstractOrderEntryModel entry) -> {
					if (!(entry.getProduct() instanceof TmaProductOfferingModel))
					{
						return;
					}
					final Optional<TmaProductOfferingGroupModel> groupModel = getPoService()
							.getOfferingGroupForPoAndBpo((TmaProductOfferingModel) entry.getProduct(), entry.getBpo());
					final TmaPolicyContextBuilder builder = TmaPolicyContextBuilder.newPolicyContextBuilder();
					builder.withProductOfferings(Arrays.asList((TmaProductOfferingModel) entry.getProduct()))
							.withQuantity(Math.toIntExact(entry.getQuantity()))
							.withProcessType(entry.getProcessType())
							.withOrderEntry(entry)
							.withEntryGroup(entryGroup);
					groupModel.ifPresent(builder::withGroup);
					final TmaPolicyContext context = builder.build();
					contexts.add(context);
				});

		contexts.addAll(getCompatibilityRequestsFromCpi((CustomerModel) orderModel.getUser()));

		final Set<RuleEvaluationResult> ruleEvaluationResults = getRuleEvaluationResults(contexts);

		getTmaPolicyEngine().applyActions(ruleEvaluationResults);
	}

	@Override
	public void verifyCompatibilityPolicies(final AbstractOrderEntryModel orderEntryModel, final UserModel userModel)
	{
		final List<TmaPolicyContext> contexts = new ArrayList<>();

		final List<AbstractOrderEntryModel> entries = new ArrayList<>(getEntryService().getSpoChildEntries(orderEntryModel));
		entries.add(orderEntryModel);

		clearEntryCompatibilityValidationMessages((CartEntryModel) orderEntryModel);

		entries.forEach((final AbstractOrderEntryModel entry) -> {
			if (!(entry.getProduct() instanceof TmaProductOfferingModel))
			{
				return;
			}

			final Optional<TmaProductOfferingGroupModel> groupModel = entry.getMasterEntry() != null ?
					getPoService().getOfferingGroupForPoAndBpo((TmaProductOfferingModel) entry.getProduct(),
							(TmaBundledProductOfferingModel) entry.getMasterEntry().getProduct()) : Optional.empty();
			final TmaPolicyContextBuilder builder = TmaPolicyContextBuilder.newPolicyContextBuilder();
			builder.withProductOfferings(Collections.singletonList((TmaProductOfferingModel) entry.getProduct()))
					.withQuantity(Math.toIntExact(entry.getQuantity()))
					.withProcessType(entry.getProcessType())
					.withOrderEntry(entry);
			groupModel.ifPresent(builder::withGroup);
			final TmaPolicyContext context = builder.build();
			contexts.add(context);
		});

		contexts.addAll(getCompatibilityRequestsFromCpi((CustomerModel) userModel));

		final Set<RuleEvaluationResult> ruleEvaluationResults = getRuleEvaluationResults(contexts);

		getTmaPolicyEngine().applyActions(ruleEvaluationResults);
	}

	@Override
	public void verifyCompatibilityPoliciesForStandaloneProducts(final AbstractOrderModel orderModel)
	{
		getTmaValidationMessagesStrategy().cleanupValidationMessagesOn((CartModel) orderModel);

		final List<TmaPolicyContext> contexts = new ArrayList<>();

		orderModel.getEntries().stream()
				.filter(entry -> entry.getMasterEntry() == null && CollectionUtils.isEmpty(entry.getChildEntries()))
				.forEach((final AbstractOrderEntryModel entry) -> {
					if (!(entry.getProduct() instanceof TmaProductOfferingModel))
					{
						return;
					}
					final TmaPolicyContext context = TmaPolicyContextBuilder.newPolicyContextBuilder()
							.withProductOfferings(Collections.singletonList((TmaProductOfferingModel) entry.getProduct()))
							.withQuantity(Math.toIntExact(entry.getQuantity()))
							.withProcessType(entry.getProcessType())
							.withOrderEntry(entry).build();
					contexts.add(context);
				});

		contexts.addAll(getCompatibilityRequestsFromCpi((CustomerModel) getUserService().getCurrentUser()));

		final Set<RuleEvaluationResult> ruleEvaluationResults = getRuleEvaluationResults(contexts);

		getTmaPolicyEngine().applyActions(ruleEvaluationResults);
	}

	protected List<TmaPolicyContext> getCompatibilityRequestsFromCpi(final CustomerModel customer)
	{
		if (getUserService().isAnonymousUser(customer))
		{
			return Collections.emptyList();
		}

		final List<TmaPolicyContext> compatibilityRequestParamsForSubscribedProducts = new ArrayList<>();

		for (final TmaSubscribedProductModel subscribedProduct : getCustomerInventoryService().getAllSubscribedProducts(customer))
		{
			try
			{
				final TmaPolicyContextBuilder builder = TmaPolicyContextBuilder.newPolicyContextBuilder()
						.withProductOfferings(
								Collections.singletonList(getPoService().getPoForCode(subscribedProduct.getProductCode())))
						.withSubscribed(true)
						.withQuantity(1);

				if (subscribedProduct.getAgreementItem() != null
						&& subscribedProduct.getAgreementItem().getAgreement().getTerm() != null)
				{
					builder.withSubscriptionEndDate(subscribedProduct.getAgreementItem().getAgreement().getTerm().getEnd());
				}

				compatibilityRequestParamsForSubscribedProducts.add(builder.build());
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.error("Removing the Unknown Product from context" + subscribedProduct.getProductCode(), e);
			}
		}

		return compatibilityRequestParamsForSubscribedProducts;
	}

	/**
	 * @deprecated since 1911
	 */
	@Deprecated(since = "1911", forRemoval = true)
	protected List<TmaPolicyActionModel> getInvalidActions(final List<TmaPolicyContext> contexts)
	{
		final Set<RuleEvaluationResult> results = getRuleEvaluationResults(contexts);

		return results.stream().filter(result -> CollectionUtils.isEmpty(result.getContexts()))
				.map(RuleEvaluationResult::getAction)
				.collect(Collectors.toList());
	}

	protected Set<RuleEvaluationResult> getRuleEvaluationResults(final List<TmaPolicyContext> contexts)
	{
		return getTmaPolicyEngine().findPolicies(contexts,
				Stream.of(TmaCompatibilityPolicyActionType.SELECT, TmaCompatibilityPolicyActionType.AUTOPICK)
						.collect(Collectors.toCollection(HashSet::new)));
	}

	/**
	 * @deprecated since 1911
	 */
	@Deprecated(since = "1911", forRemoval = true)
	protected void invalidateErrorMessages(final AbstractOrderModel orderModel, final EntryGroup entryGroup)
	{
		if (entryGroup == null)
		{
			clearCartInvalidErrors((CartModel) orderModel);
			return;
		}
		invalidateEntryGroupErrors(orderModel, entryGroup);
	}

	private void invalidateEntryGroupErrors(final AbstractOrderModel orderModel, final EntryGroup entryGroup)
	{
		if (!entryGroup.getErroneous())
		{
			return;
		}
		entryGroup.setErroneous(false);
		entryGroup.setErrorMessages(Collections.emptyList());

		getTmaEntryGroupService().updateEntryGroup(orderModel, entryGroup);
	}

	private void clearCartInvalidErrors(final CartModel cart)
	{
		cart.setInvalidMessages(Collections.emptyList());
		getModelService().save(cart);
	}

	private void clearEntryCompatibilityValidationMessages(final CartEntryModel entry)
	{
		if (entry == null)
		{
			return;
		}

		getTmaValidationMessagesStrategy().cleanupValidationMessagesOn(entry);

		if (CollectionUtils.isEmpty(entry.getChildEntries()))
		{
			return;
		}

		entry.getChildEntries().forEach((AbstractOrderEntryModel childEntry) ->
				clearEntryCompatibilityValidationMessages((CartEntryModel) childEntry));
	}

	protected TmaCompatibilityPolicyEvaluationStrategy getPolicyEvaluationStrategy()
	{
		return policyEvaluationStrategy;
	}

	@Required
	public void setPolicyEvaluationStrategy(final TmaCompatibilityPolicyEvaluationStrategy policyEvaluationStrategy)
	{
		this.policyEvaluationStrategy = policyEvaluationStrategy;
	}

	protected Map<TmaCompatibilityPolicyActionType, TmaPolicyActionResolver> getPolicyActionResolversMap()
	{
		return policyActionResolversMap;
	}

	@Required
	public void setPolicyActionResolversMap(
			final Map<TmaCompatibilityPolicyActionType, TmaPolicyActionResolver> policyActionResolversMap)
	{
		this.policyActionResolversMap = policyActionResolversMap;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	protected EntryGroupService getEntryGroupService()
	{
		return entryGroupService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	@Required
	public void setEntryGroupService(final EntryGroupService entryGroupService)
	{
		this.entryGroupService = entryGroupService;
	}

	protected TmaPoService getPoService()
	{
		return poService;
	}

	@Required
	public void setPoService(final TmaPoService poService)
	{
		this.poService = poService;
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected TmaCustomerInventoryService getCustomerInventoryService()
	{
		return customerInventoryService;
	}

	@Required
	public void setCustomerInventoryService(final TmaCustomerInventoryService customerInventoryService)
	{
		this.customerInventoryService = customerInventoryService;
	}

	public TmaPolicyEngine getTmaPolicyEngine()
	{
		return tmaPolicyEngine;
	}

	@Required
	public void setTmaPolicyEngine(final TmaPolicyEngine tmaPolicyEngine)
	{
		this.tmaPolicyEngine = tmaPolicyEngine;
	}

	public TmaPolicyStatementValidationStrategy getTmaPolicyStatementValidationStrategy()
	{
		return tmaPolicyStatementValidationStrategy;
	}

	@Required
	public void setTmaPolicyStatementValidationStrategy(
			final TmaPolicyStatementValidationStrategy tmaPolicyStatementValidationStrategy)
	{
		this.tmaPolicyStatementValidationStrategy = tmaPolicyStatementValidationStrategy;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	public TmaEntryGroupService getTmaEntryGroupService()
	{
		return tmaEntryGroupService;
	}

	/**
	 * @deprecated since 2102
	 */
	@Deprecated(since = "2102")
	public void setTmaEntryGroupService(final TmaEntryGroupService tmaEntryGroupService)
	{
		this.tmaEntryGroupService = tmaEntryGroupService;
	}

	protected TmaAbstractOrderEntryService getEntryService()
	{
		return entryService;
	}

	protected TmaValidationMessagesStrategy getTmaValidationMessagesStrategy()
	{
		return tmaValidationMessagesStrategy;
	}
}



