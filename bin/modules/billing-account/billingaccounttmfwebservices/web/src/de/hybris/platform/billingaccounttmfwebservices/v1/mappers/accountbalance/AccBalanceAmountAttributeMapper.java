/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.accountbalance;

import de.hybris.platform.billingaccountservices.model.BaAccountBalanceModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountBalance;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Money;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for amount attribute between {@link BaAccountBalanceModel} and {@link AccountBalance}
 *
 * @since 2105
 */
public class AccBalanceAmountAttributeMapper
		extends BaAttributeMapper<BaAccountBalanceModel, AccountBalance>
{
	private CommonI18NService commonI18NService;

	public AccBalanceAmountAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final CommonI18NService commonI18NService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.commonI18NService = commonI18NService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaAccountBalanceModel source, final AccountBalance target,
			final MappingContext context)
	{
		final Money money = new Money();

		if (source.getAmount() != null)
		{
			money.setValue(source.getAmount().floatValue());
		}
		if (source.getCurrency() != null && source.getCurrency().getIsocode() != null)
		{
			money.setUnit(source.getCurrency().getIsocode());
		}

		target.setAmount(money);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AccountBalance target, final BaAccountBalanceModel source,
			final MappingContext context)
	{

		if (target.getAmount().getValue() != null)
		{
			source.setAmount(Double.valueOf(target.getAmount().getValue()));
		}
		if (target.getAmount().getUnit() != null)
		{
			source.setCurrency(getCommonI18NService().getCurrency(target.getAmount().getUnit()));
		}
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}
}
