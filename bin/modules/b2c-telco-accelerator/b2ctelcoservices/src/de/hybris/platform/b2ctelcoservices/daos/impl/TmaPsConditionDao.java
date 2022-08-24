/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.daos.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.compatibility.data.TmaPolicyContext;
import de.hybris.platform.b2ctelcoservices.daos.TmaPolicyConditionDao;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPsPolicyStatementModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Dao for CompatibilityPolicy conditions based on
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel}.
 *
 * @since 1907
 */
public class TmaPsConditionDao extends DefaultGenericDao<TmaPolicyConditionModel> implements TmaPolicyConditionDao
{
	private static final String PS_VALUE_KEY = "psvalue";
	private static final String FIND_POLICY_FOR_PRODUCT_SPEC = "SELECT {pc:" + TmaPolicyConditionModel.PK + "} " + "FROM {"
			+ TmaPolicyConditionModel._TYPECODE + " AS pc " + "  JOIN " + TmaPsPolicyStatementModel._TYPECODE + " AS ps "
			+ "  ON {pc:" + TmaPolicyConditionModel.STATEMENT + "}={ps:" + TmaPsPolicyStatementModel.PK + "}} " + "WHERE {ps:"
			+ TmaPsPolicyStatementModel.PRODUCTSPECIFICATION + "} " + " IN (?" + PS_VALUE_KEY + ")";

	public TmaPsConditionDao()
	{
		super(TmaPolicyConditionModel._TYPECODE);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<TmaPolicyConditionModel> findPolicyConditions(final TmaPolicyContext requestParam)
	{
		validateParameterNotNullStandardMessage("productOffering", requestParam.getProductOfferings());

		final List<TmaProductSpecificationModel> productSpecifications = new ArrayList<>();


		requestParam.getProductOfferings().forEach(productoffering -> {
			if (productoffering.getProductSpecification() != null)
			{
				productSpecifications.add(productoffering.getProductSpecification());
			}
		});

		if (CollectionUtils.isEmpty(productSpecifications))
		{
			return Collections.emptyList();
		}

		final FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_POLICY_FOR_PRODUCT_SPEC);
		query.addQueryParameter(PS_VALUE_KEY, productSpecifications);
		return getFlexibleSearchService().<TmaPolicyConditionModel> search(query).getResult();
	}
}
