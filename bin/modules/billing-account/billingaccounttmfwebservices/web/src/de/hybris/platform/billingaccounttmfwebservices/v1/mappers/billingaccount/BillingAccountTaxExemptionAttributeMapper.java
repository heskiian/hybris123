/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.model.BaAccountTaxExemptionModel;
import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountTaxExemption;
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
 * This attribute Mapper class maps data for tax exemption attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2105
 */
public class BillingAccountTaxExemptionAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillingAccountTaxExemptionAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (CollectionUtils.isEmpty(source.getTaxExemptions()))
		{
			return;
		}

		final List<AccountTaxExemption> taxExemptionList = source.getTaxExemptions().stream()
				.map(taxExemptionModel -> getMapperFacade().map(taxExemptionModel, AccountTaxExemption.class, context))
				.collect(Collectors.toList());

		target.setTaxExemption(taxExemptionList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillingAccount target, final BaBillingAccountModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getTaxExemption()))
		{
			return;
		}

		source.setTaxExemptions(getAllTaxExceptions(target.getTaxExemption()));
	}

	private Set<BaAccountTaxExemptionModel> getAllTaxExceptions(final List<AccountTaxExemption> accountTaxExemptions)
	{
		final Set<BaAccountTaxExemptionModel> result = new HashSet<>();
		accountTaxExemptions.forEach(accountTaxExemption -> {
			final BaAccountTaxExemptionModel baAccountTaxExemptionModel = (BaAccountTaxExemptionModel) getBaGenericService()
					.createItem(BaAccountTaxExemptionModel.class);
			getMapperFacade().map(accountTaxExemption, baAccountTaxExemptionModel);
			result.add(baAccountTaxExemptionModel);
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
