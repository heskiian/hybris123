/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.model.BaAccountBalanceModel;
import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountBalance;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for account balance attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2105
 */
public class BillingAccountAccBalanceAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillingAccountAccBalanceAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillingAccountModel source, final BillingAccount target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getAccountBalances()))
		{
			return;
		}

		final List<AccountBalance> accountBalanceList = source.getAccountBalances().stream()
				.map(accountBalanceModel -> getMapperFacade().map(accountBalanceModel, AccountBalance.class, context))
				.collect(Collectors.toList());

		target.setAccountBalance(accountBalanceList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillingAccount target, final BaBillingAccountModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getAccountBalance()))
		{
			return;
		}

		source.setAccountBalances(getAllAccountBalances(target.getAccountBalance(), context));
	}

	private Set<BaAccountBalanceModel> getAllAccountBalances(final List<AccountBalance> accountBalances,
			final MappingContext context)
	{
		final Set<BaAccountBalanceModel> result = new HashSet<>();
		accountBalances.forEach(accountBalance -> {
			final BaAccountBalanceModel baAccountBalanceModel = (BaAccountBalanceModel) getBaGenericService()
					.createItem(BaAccountBalanceModel.class);
			getMapperFacade().map(accountBalance, baAccountBalanceModel, context);
			result.add(baAccountBalanceModel);
		});
		return result;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected BaGenericService getBaGenericService()
	{
		return baGenericService;
	}
}
