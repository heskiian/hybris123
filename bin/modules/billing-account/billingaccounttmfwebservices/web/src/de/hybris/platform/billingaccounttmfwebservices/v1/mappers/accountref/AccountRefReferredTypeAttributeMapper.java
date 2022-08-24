/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.accountref;

import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.model.BaFinancialAccountModel;
import de.hybris.platform.billingaccountservices.model.BaPartyAccountModel;
import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for referred type attribute between {@link BaAccountModel} and {@link AccountRef}
 *
 * @since 2105
 */
public class AccountRefReferredTypeAttributeMapper extends BaAttributeMapper<BaAccountModel, AccountRef>
{
	public AccountRefReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaAccountModel source, final AccountRef target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}
		if (source instanceof BaPartyAccountModel)
		{
			target.setAtreferredType(Config.getParameter(BillingaccounttmfwebservicesConstants.PARTY_ACCOUNT_DEFAULT_REFERRED));
		}
		if (source instanceof BaBillingAccountModel)
		{
			target.setAtreferredType(Config.getParameter(BillingaccounttmfwebservicesConstants.BILLING_ACCOUNT_DEFAULT_REFERRED));
		}
		if (source instanceof BaFinancialAccountModel)
		{
			target.setAtreferredType(Config.getParameter(BillingaccounttmfwebservicesConstants.FINANCIAL_ACCOUNT_DEFAULT_REFERRED));
		}
	}
}
