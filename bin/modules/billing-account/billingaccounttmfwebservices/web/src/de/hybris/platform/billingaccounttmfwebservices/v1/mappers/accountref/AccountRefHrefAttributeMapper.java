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

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link BaAccountModel} and {@link AccountRef}
 *
 * @since 2105
 */
public class AccountRefHrefAttributeMapper extends BaAttributeMapper<BaAccountModel, AccountRef>
{
	public AccountRefHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
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
			target.setHref(BillingaccounttmfwebservicesConstants.PARTY_ACCOUNT_API_URL + source.getId());
		}
		if (source instanceof BaBillingAccountModel)
		{
			target.setHref(BillingaccounttmfwebservicesConstants.BILLING_ACCOUNT_API_URL + source.getId());
		}
		if (source instanceof BaFinancialAccountModel)
		{
			target.setHref(BillingaccounttmfwebservicesConstants.FINANCIAL_ACCOUNT_API_URL + source.getId());
		}
	}
}
