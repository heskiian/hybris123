/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.financialaccountref;

import de.hybris.platform.billingaccountservices.model.BaFinancialAccountModel;
import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.FinancialAccountRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referred type attribute between {@link BaFinancialAccountModel} and {@link FinancialAccountRef}
 *
 * @since 2105
 */
public class FinancialAccountRefReferredTypeAttributeMapper
		extends BaAttributeMapper<BaFinancialAccountModel, FinancialAccountRef>
{
	public FinancialAccountRefReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaFinancialAccountModel source, final FinancialAccountRef target,
			MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(Config.getParameter(BillingaccounttmfwebservicesConstants.FINANCIAL_ACCOUNT_DEFAULT_REFERRED));
		}
	}
}
