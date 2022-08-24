/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.validators.impl;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.compatibility.validators.AbstractTmaPolicyStatementValidator;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedPoPolicyStatementModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Implementation of {@link AbstractTmaPolicyStatementValidator} responsible for validating
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaSubscribedPoPolicyStatementModel}
 *
 * @since 1810
 */
public class TmaSubscribedPoStatementValidator extends AbstractTmaPolicyStatementValidator
{

	@Override
	public List<TmaPolicyContext> retrieveApplicableContexts(final TmaPolicyStatementModel statement,
			final List<TmaPolicyContext> contexts)
	{
		if (!(statement instanceof TmaSubscribedPoPolicyStatementModel))
		{
			return contexts;
		}

		final TmaSubscribedPoPolicyStatementModel subscribedPoPolicyStatement = (TmaSubscribedPoPolicyStatementModel) statement;
		final Set<TmaPolicyContext> poRequestParam = contexts.stream()
				.filter(param -> param.isSubscribed() && CollectionUtils.isNotEmpty(param.getProductOfferings())
						&& param.getProductOfferings().contains(subscribedPoPolicyStatement.getProductOffering())
						&& hasSameCatalogVersion(param, statement))
				.collect(Collectors.toSet());

		if (poRequestParam.isEmpty())
		{
			return Collections.emptyList();
		}
		final int quantity = poRequestParam.stream().mapToInt(TmaPolicyContext::getQuantity).sum();

		if (checkQuantity(quantity, statement.getMin(), statement.getMax()))
		{
			return new ArrayList<>(poRequestParam);
		}
		return Collections.emptyList();
	}

	/**
	 * Verifies if catalog version of {@link TmaPolicyStatementModel} is same as catalog version of
	 * subscribed products of {@link TmaPolicyContext} .
	 *
	 * @param policy
	 *           the {@link TmaPolicyContext}
	 * @param statement
	 *           the {@link TmaPolicyStatementModel}
	 * @return boolean between the provided prices
	 */
	protected boolean hasSameCatalogVersion(TmaPolicyContext policy, TmaPolicyStatementModel statement)
	{
		if (!CollectionUtils.isEmpty(policy.getProductOfferings()))
		{
			Set<CatalogVersionModel> prodcutOfferingCatalog = new HashSet<>();
			policy.getProductOfferings().stream().forEach(product ->
			{
				prodcutOfferingCatalog.add(product.getCatalogVersion());
			});
			if (prodcutOfferingCatalog.contains(statement.getCatalogVersion()))
			{
				return true;
			}
		}

		return false;
	}
}
