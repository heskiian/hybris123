/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Money;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;
import de.hybris.platform.core.model.c2l.CurrencyModel;

import java.util.HashMap;
import java.util.Map;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for credit limit attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2105
 */
public class BillingAccountCreditLimitAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	private static final String CURRENCY_ISOCODE = "isocode";

	private BaGenericService baGenericService;

	public BillingAccountCreditLimitAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillingAccountModel source, final BillingAccount target,
			final MappingContext context)
	{
		final Money money = new Money();

		if (source.getCreditLimit() != null)
		{
			money.setValue(source.getCreditLimit().floatValue());
		}
		if (source.getCreditCurrency() != null && source.getCreditCurrency().getIsocode() != null)
		{
			money.setUnit(source.getCreditCurrency().getIsocode());
		}

		target.creditLimit(money);
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillingAccount target, final BaBillingAccountModel source,
			final MappingContext context)
	{
		if (target.getCreditLimit() == null)
		{
			return;
		}

		final Money money = target.getCreditLimit();
		if (money.getValue() != null)
		{
			source.setCreditLimit(money.getValue().doubleValue());
		}
		if (money.getUnit() != null)
		{
			final Map<String, String> keys = new HashMap<>();
			keys.put(CURRENCY_ISOCODE, money.getUnit());
			CurrencyModel currencyModel = (CurrencyModel) getBaGenericService().getItem(CurrencyModel._TYPECODE, keys);
			if (currencyModel == null)
			{
				currencyModel = (CurrencyModel) getBaGenericService().createItem(CurrencyModel.class);
				currencyModel.setIsocode(money.getUnit());
				currencyModel.setSymbol(money.getUnit());
			}
			source.setCreditCurrency(currencyModel);
		}
	}

	protected BaGenericService getBaGenericService()
	{
		return baGenericService;
	}
}
