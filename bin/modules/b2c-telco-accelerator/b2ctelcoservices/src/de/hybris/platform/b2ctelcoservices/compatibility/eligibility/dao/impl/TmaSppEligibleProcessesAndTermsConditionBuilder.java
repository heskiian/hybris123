/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityContextService;
import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.dao.TmaEligibilitySearchQueryException;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaEligibilityContextModel;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.pricing.dao.AbstractSubscriptionPricePlanConditionBuilder;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerInventoryService;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Search query condition builder for {@link PriceRowModel#PROCESSTYPES} and
 * {@link PriceRowModel#SUBSCRIPTIONTERMS} based on received {@link TmaPriceContext}.
 *
 * @since 1810
 */
public class TmaSppEligibleProcessesAndTermsConditionBuilder
		extends AbstractSubscriptionPricePlanConditionBuilder<TmaPriceContext>
{
	private static final String SUBSCRIPTION_PRICE_PLAN_2_PROCESS_TYPE_RELATION_NAME = "SubscriptionPricePlan2TmaProcessTypeRelation";
	private TmaCustomerInventoryService customerInventoryService;
	private TmaEligibilityContextService tmaEligibilityContextService;
	private EnumerationService enumerationService;
	private UserService userService;

	@Override
	protected boolean shouldApplyCondition(final TmaPriceContext parameter)
	{
		return true;
	}

	@Override
	protected GenericConditionList createQueryConditions(final GenericQuery query, final TmaPriceContext priceContext)
			throws TmaEligibilitySearchQueryException
	{
		final Map<TmaProcessType, Set<String>> eligibleProcessesAndTerms;
		if (getTmaEligibilityContextService().shouldApplyEligibility())
		{
			eligibleProcessesAndTerms = filterEligibleProcessesAndTerms(priceContext);
		}
		else
		{
			eligibleProcessesAndTerms = createProcessAndTermsMap(priceContext);
		}

		if (CollectionUtils.isEmpty(eligibleProcessesAndTerms.keySet()))
		{
			throw new TmaEligibilitySearchQueryException(
					"The given price context does not contain any eligible combination of processes & terms!");
		}

		return GenericConditionList.createConditionList(createCondition(query, eligibleProcessesAndTerms));
	}

	/**
	 * Filters the eligible {@link TmaProcessType} and associated subscription terms ids from the given
	 * {@link TmaPriceContext}.
	 *
	 * @param priceContext
	 * 		the {@link TmaPriceContext} from which eligible processes and terms will be retrieved
	 * @return eligible terms with associated subscription term ids
	 */
	protected Map<TmaProcessType, Set<String>> filterEligibleProcessesAndTerms(final TmaPriceContext priceContext)
	{
		if (priceContext.getUser() != null)
		{
			getUserService().setCurrentUser(priceContext.getUser());
		}
		final Set<TmaProcessType> eligibleProcesses = retrieveEligibleProcesses(priceContext);
		final Map<TmaProcessType, Set<String>> eligibleProcessesAndTerms = new HashMap<>();
		if (CollectionUtils.isNotEmpty(eligibleProcesses))
		{
			eligibleProcesses.forEach(processType ->
			{
				final Set<SubscriptionTermModel> subscriptionTerms = priceContext.getSubscriptionTerms();
				final Set<TmaEligibilityContextModel> eligibilityContexts = retrieveExistingContextsForProcess(processType);

				if (areContextTermsAvailable(eligibilityContexts))
				{
					eligibleProcessesAndTerms.put(processType, retrieveCodesFor(subscriptionTerms));
				}
				else
				{
					updateEligibleProcessesAndTermsWithSubscriptionTerms(subscriptionTerms, eligibleProcessesAndTerms, processType,
							eligibilityContexts);
				}
			});
		}
		return eligibleProcessesAndTerms;
	}

	protected void updateEligibleProcessesAndTermsWithSubscriptionTerms(
			final Set<SubscriptionTermModel> subscriptionTerms, final Map<TmaProcessType, Set<String>> eligibleProcessesAndTerms,
			final TmaProcessType processType, final Set<TmaEligibilityContextModel> eligibilityContexts)
	{
		final Set<String> eligibleTermCodes = new HashSet<>();
		eligibilityContexts.forEach(eligibilityContext -> eligibleTermCodes.addAll(eligibilityContext.getTermCodes()));

		if (CollectionUtils.isNotEmpty(subscriptionTerms))
		{
			final Set<String> termCodes = subscriptionTerms.stream()
					.filter(term -> eligibleTermCodes.contains(term.getId()))
					.map(SubscriptionTermModel::getId).collect(Collectors.toSet());
			if (CollectionUtils.isNotEmpty(termCodes))
			{
				eligibleProcessesAndTerms.put(processType, termCodes);
			}
		}
		else
		{
			eligibleProcessesAndTerms.put(processType, eligibleTermCodes);
		}
	}

	protected Set<TmaProcessType> retrieveEligibleProcesses(final TmaPriceContext priceContext)
	{
		final Set<TmaProcessType> eligibleProcessTypes = getCustomerInventoryService().retrieveProcesses();
		if (priceContext == null || CollectionUtils.isEmpty(priceContext.getProcessTypes()))
		{
			return eligibleProcessTypes;
		}
		return priceContext.getProcessTypes().stream().filter(processType -> eligibleProcessTypes.contains(processType))
				.collect(Collectors.toSet());
	}

	private Set<String> retrieveCodesFor(final Set<SubscriptionTermModel> subscriptionTerms)
	{
		if (CollectionUtils.isEmpty(subscriptionTerms))
		{
			return Collections.emptySet();
		}

		return subscriptionTerms.stream().map(SubscriptionTermModel::getId).collect(Collectors.toSet());
	}

	protected Set<TmaEligibilityContextModel> retrieveExistingContextsForProcess(final TmaProcessType processType)
	{
		final Set<TmaEligibilityContextModel> customerEligibilityContext = getTmaEligibilityContextService()
				.extractEligibilityContext();

		if (CollectionUtils.isEmpty(customerEligibilityContext))
		{
			return Collections.emptySet();
		}

		return customerEligibilityContext.stream().filter(
				eligibilityContext -> CollectionUtils.isEmpty(eligibilityContext.getProcessesCodes()) || eligibilityContext
						.getProcessesCodes().contains(processType.getCode()))
				.collect(Collectors.toSet());
	}

	protected boolean areContextTermsAvailable(final Set<TmaEligibilityContextModel> eligibilityContexts)
	{
		if (CollectionUtils.isEmpty(eligibilityContexts))
		{
			return false;
		}
		final Optional<TmaEligibilityContextModel> eligibilityContextForAllTerms = eligibilityContexts.stream()
				.filter(eligibilityContext -> CollectionUtils.isEmpty(eligibilityContext.getTermCodes())).findFirst();
		return eligibilityContextForAllTerms.isPresent();
	}

	protected Map<TmaProcessType, Set<String>> createProcessAndTermsMap(final TmaPriceContext priceContext)
	{
		final Map<TmaProcessType, Set<String>> eligibleProcessesAndTerms = new HashMap<>();
		if (CollectionUtils.isEmpty(priceContext.getProcessTypes()))
		{
			final Set<TmaProcessType> allProcessTypes = new HashSet(
					getEnumerationService().getEnumerationValues(TmaProcessType._TYPECODE));
			priceContext.setProcessTypes(allProcessTypes);
		}
		priceContext.getProcessTypes().forEach(processType ->
		{
			if (CollectionUtils.isEmpty(priceContext.getSubscriptionTerms()))
			{
				eligibleProcessesAndTerms.put(processType, new HashSet<>());
			}
			else
			{
				eligibleProcessesAndTerms
						.put(processType,
								priceContext.getSubscriptionTerms().stream().map(SubscriptionTermModel::getId)
										.collect(Collectors.toSet()));
			}
		});
		return eligibleProcessesAndTerms;
	}

	/**
	 * @deprecated since 1911, use {@link #createCondition(GenericQuery, Map)}
	 */
	@Deprecated(since = "1911", forRemoval = true)
	protected GenericCondition createConditionFor(final GenericQuery query, final TmaProcessType processType,
			final Set<String> termCodes)
	{
		final GenericCondition processTypeConditions = createConditionsForProcess(query, processType);
		if (CollectionUtils.isEmpty(termCodes))
		{
			return processTypeConditions;
		}

		final GenericCondition subscriptionTermsConditions = createConditionsForTerms(query, termCodes);
		return GenericCondition.and(processTypeConditions, subscriptionTermsConditions);
	}

	protected GenericCondition createCondition(final GenericQuery query,
			final Map<TmaProcessType, Set<String>> eligibleProcessesAndTerms)
	{
		final GenericCondition processTypeConditions = createConditionsForProcesses(query, eligibleProcessesAndTerms.keySet());
		final List<GenericCondition> subscriptionTermsConditions = new ArrayList<>();
		eligibleProcessesAndTerms.keySet().forEach(processType ->
		{
			final Set<String> terms = eligibleProcessesAndTerms.get(processType);
			if (CollectionUtils.isNotEmpty(terms))
			{
				subscriptionTermsConditions.add(createConditionsForTerms(query, terms));
			}
		});

		return CollectionUtils.isEmpty(subscriptionTermsConditions)
				? processTypeConditions
				: GenericConditionList.and(processTypeConditions, GenericCondition.or(subscriptionTermsConditions));
	}

	/**
	 * @deprecated since 1911, use {@link #createConditionsForProcesses(GenericQuery, Set)}
	 */
	@Deprecated(since = "1911", forRemoval = true)
	protected GenericCondition createConditionsForProcess(final GenericQuery query, final TmaProcessType processType)
	{
		final GenericSearchField processTypeTargetPk = addRelationJoinToQuery(query,
				SUBSCRIPTION_PRICE_PLAN_2_PROCESS_TYPE_RELATION_NAME, SOURCE_RELATION_NAME, TmaProcessType._TYPECODE,
				TARGET_RELATION_NAME);

		final GenericCondition subscriptionPricePlanMatchProcessCondition = GenericCondition
				.createConditionForValueComparison(processTypeTargetPk, Operator.EQUAL, processType);

		final GenericCondition subscriptionPricePlanEmptyProcessCondition = GenericCondition
				.createIsNullCondition(processTypeTargetPk);
		return GenericCondition.or(subscriptionPricePlanMatchProcessCondition, subscriptionPricePlanEmptyProcessCondition);
	}

	protected GenericCondition createConditionsForProcesses(final GenericQuery query, final Set<TmaProcessType> processTypes)
	{
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

	protected GenericCondition createConditionsForTerms(final GenericQuery query, final Set<String> subscriptionTermCodes)
	{
		final GenericSearchField subscriptionTermTargetPk = addRelationJoinToQuery(query,
				PriceRowModel._SUBSCRIPTIONTERM2SUBSCRIPTIONPRICEPLANRELATION, TARGET_RELATION_NAME,
				SubscriptionTermModel._TYPECODE, SOURCE_RELATION_NAME);

		final GenericSearchField subscriptionTermCode = new GenericSearchField(SubscriptionTermModel._TYPECODE,
				SubscriptionTermModel.ID);
		final GenericCondition subscriptionPricePlanEmptyTermCondition = GenericCondition
				.createIsNullCondition(subscriptionTermTargetPk);
		final GenericCondition subscriptionPricePlanMatchTermsCondition = GenericCondition
				.createConditionForValueComparison(subscriptionTermCode, Operator.IN, subscriptionTermCodes);
		return GenericCondition.or(subscriptionPricePlanMatchTermsCondition, subscriptionPricePlanEmptyTermCondition);
	}

	public TmaCustomerInventoryService getCustomerInventoryService()
	{
		return customerInventoryService;
	}

	@Required
	public void setCustomerInventoryService(final TmaCustomerInventoryService customerInventoryService)
	{
		this.customerInventoryService = customerInventoryService;
	}

	public TmaEligibilityContextService getTmaEligibilityContextService()
	{
		return tmaEligibilityContextService;
	}

	@Required
	public void setTmaEligibilityContextService(
			final TmaEligibilityContextService tmaEligibilityContextService)
	{
		this.tmaEligibilityContextService = tmaEligibilityContextService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

	public UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}


}
