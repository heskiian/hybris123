/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.accountbalance;

import de.hybris.platform.billingaccountservices.enums.BaBalanceType;
import de.hybris.platform.billingaccountservices.model.BaAccountBalanceModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountBalance;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for balanceType attribute between {@link BaAccountBalanceModel} and {@link AccountBalance}
 *
 * @since 2111
 */
public class AccBalanceTypeAttributeMapper extends BaAttributeMapper<BaAccountBalanceModel, AccountBalance>
{
	public AccBalanceTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaAccountBalanceModel accountBalanceModel,
			final AccountBalance accountBalance, final MappingContext context)
	{
		// no implementation needed
	}

	@Override
	public void populateSourceAttributeFromTarget(final AccountBalance target, final BaAccountBalanceModel source,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(target.getBalanceType()))
		{
			source.setBalanceType(BaBalanceType.valueOf(target.getBalanceType()));
		}
	}
}
