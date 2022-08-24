/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.model.BaAccountRelationshipModel;
import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountRelationship;
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
 * This attribute Mapper class maps data for account relationship attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2105
 */
public class BillingAccountAccRelationshipAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillingAccountAccRelationshipAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (CollectionUtils.isEmpty(source.getAccountRelationshipReferences()))
		{
			return;
		}

		final List<AccountRelationship> accountRelationshipList = source.getAccountRelationshipReferences().stream()
				.map(relationshipModel -> getMapperFacade().map(relationshipModel, AccountRelationship.class, context))
				.collect(Collectors.toList());

		target.setAccountRelationship(accountRelationshipList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillingAccount target, final BaBillingAccountModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getAccountRelationship()))
		{
			return;
		}

		source.setAccountRelationshipReferences(getAllAccountRelationships(target.getAccountRelationship(), context));
	}

	private Set<BaAccountRelationshipModel> getAllAccountRelationships(final List<AccountRelationship> accountRelationships,
			final MappingContext context)
	{
		final Set<BaAccountRelationshipModel> result = new HashSet<>();
		accountRelationships.forEach(accountRelationship -> {
			final BaAccountRelationshipModel baAccountRelationshipModel = (BaAccountRelationshipModel) getBaGenericService()
					.createItem(BaAccountRelationshipModel.class);
			getMapperFacade().map(accountRelationship, baAccountRelationshipModel, context);
			result.add(baAccountRelationshipModel);
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
