/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.financialaccountref;

import de.hybris.platform.billingaccountservices.model.BaFinancialAccountModel;
import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.FinancialAccountRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link BaFinancialAccountModel} and {@link FinancialAccountRef}
 *
 * @since 2105
 */
public class FinancialAccountRefSchemaLocationAttributeMapper
		extends BaAttributeMapper<BaFinancialAccountModel, FinancialAccountRef>
{
	public FinancialAccountRefSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaFinancialAccountModel source, final FinancialAccountRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(BillingaccounttmfwebservicesConstants.BA_API_SCHEMA);
		}
	}
}
