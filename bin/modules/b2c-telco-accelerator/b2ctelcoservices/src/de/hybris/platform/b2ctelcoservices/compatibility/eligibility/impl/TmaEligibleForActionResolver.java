/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.eligibility.TmaEligibilityActionResolver;
import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.model.TmaCompatibilityPolicyModel;
import de.hybris.platform.b2ctelcoservices.model.TmaContractTermsPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPoListPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyActionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProcessTypesPolicyStatementModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Action resolver for eligibility policies having
 * {@link de.hybris.platform.b2ctelcoservices.enums.TmaCompatibilityPolicyActionType#ALLOW} action type.
 *
 * @since 1810
 */
public class TmaEligibleForActionResolver implements TmaEligibilityActionResolver
{
	@Override
	public List<TmaEligibilityContext> processEligibilityPolicyActions(final Set<TmaCompatibilityPolicyModel> policies,
			final String subscriptionBaseId, final String billingSystemId)
	{
		return policies.stream().map(policy -> processPolicyActions(policy, subscriptionBaseId, billingSystemId))
				.collect(Collectors.toList());
	}

	private TmaEligibilityContext processPolicyActions(final TmaCompatibilityPolicyModel policyModel,
			final String subscriptionBaseId, final String billingSystemId)
	{
		final TmaEligibilityContext eligibilityContext = new TmaEligibilityContext();
		eligibilityContext.setSubscriptionBaseId(subscriptionBaseId);
		eligibilityContext.setBillingSystemId(billingSystemId);

		List<String> baseSites = new ArrayList<>();
		policyModel.getCatalogVersion().getCatalog().getBaseStores().stream().forEach(baseStore ->
		{
			baseStore.getCmsSites().stream().forEach(baseSite ->
			{
				baseSites.add(baseSite.getUid());
			});
		});
		eligibilityContext.setBaseSiteCodes(baseSites);

		for (final TmaPolicyActionModel tmaPolicyActionModel : policyModel.getActions())
		{
			final TmaPolicyStatementModel policyStatement = tmaPolicyActionModel.getStatement();
			processListOfProcessTypeStatement(eligibilityContext, policyStatement);
			processListOfContractTermsStatement(eligibilityContext, policyStatement);
			processPoListStatement(eligibilityContext, policyStatement);
		}
		return eligibilityContext;
	}

	private void processListOfProcessTypeStatement(final TmaEligibilityContext eligibilityContext,
			final TmaPolicyStatementModel policyStatement)
	{
		if (!(policyStatement instanceof TmaProcessTypesPolicyStatementModel))
		{
			return;
		}
		final List<String> processTypeCodes = new ArrayList<>();
		if (eligibilityContext.getProcessesCodes() != null)
		{
			processTypeCodes.addAll(eligibilityContext.getProcessesCodes());
		}
		for (final TmaProcessType processType : ((TmaProcessTypesPolicyStatementModel) policyStatement).getProcesses())
		{
			if (processTypeCodes.contains(processType.getCode()))
			{
				continue;
			}
			processTypeCodes.add(processType.getCode());
		}
		eligibilityContext.setProcessesCodes(processTypeCodes);
	}

	private void processListOfContractTermsStatement(final TmaEligibilityContext eligibilityContext,
			final TmaPolicyStatementModel policyStatement)
	{
		if (!(policyStatement instanceof TmaContractTermsPolicyStatementModel))
		{
			return;
		}
		final List<String> contractTerms = new ArrayList<>();
		if (eligibilityContext.getTermCodes() != null)
		{
			contractTerms.addAll(eligibilityContext.getTermCodes());
		}
		for (final SubscriptionTermModel subscriptionTermModel : ((TmaContractTermsPolicyStatementModel) policyStatement)
				.getContractTerms())
		{
			if (contractTerms.contains(subscriptionTermModel.getId()))
			{
				continue;
			}
			contractTerms.add(subscriptionTermModel.getId());
		}
		eligibilityContext.setTermCodes(contractTerms);
	}

	private void processPoListStatement(final TmaEligibilityContext eligibilityContext,
			final TmaPolicyStatementModel policyStatement)
	{
		if (!(policyStatement instanceof TmaPoListPolicyStatementModel))
		{
			return;
		}
		final List<String> poIds = new ArrayList<>();
		if (eligibilityContext.getRequiredPoCodes() != null)
		{
			poIds.addAll(eligibilityContext.getRequiredPoCodes());
		}
		((TmaPoListPolicyStatementModel) policyStatement).getOfferings().stream().filter(po -> !poIds.contains(po.getCode()))
				.forEach(po -> poIds.add(po.getCode()));

		eligibilityContext.setRequiredPoCodes(poIds);
	}

}
