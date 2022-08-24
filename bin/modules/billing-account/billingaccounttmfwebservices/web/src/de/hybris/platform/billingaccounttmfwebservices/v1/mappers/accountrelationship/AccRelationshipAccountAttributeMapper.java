/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.accountrelationship;

import de.hybris.platform.billingaccountservices.model.BaAccountModel;
import de.hybris.platform.billingaccountservices.model.BaAccountRelationshipModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountRef;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountRelationship;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for account attribute between {@link BaAccountRelationshipModel} and {@link AccountRelationship}
 *
 * @since 2105
 */
public class AccRelationshipAccountAttributeMapper
		extends BaAttributeMapper<BaAccountRelationshipModel, AccountRelationship>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public AccRelationshipAccountAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaAccountRelationshipModel source, final AccountRelationship target,
			final MappingContext context)
	{
		if (source.getTargetAccount() != null)
		{
			target.setAccount(getMapperFacade().map(source.getTargetAccount(), AccountRef.class, context));
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final AccountRelationship target, final BaAccountRelationshipModel source,
			final MappingContext context)
	{
		if (target.getAccount() == null)
		{
			return;
		}

		source.setTargetAccount(getAccount(target.getAccount(), context));
	}

	private BaAccountModel getAccount(final AccountRef accountRef, final MappingContext context)
	{
		BaAccountModel accountModel = (BaAccountModel) getBaGenericService()
				.getItem(BaAccountModel._TYPECODE, accountRef.getId());
		if (accountModel == null)
		{
			accountModel = (BaAccountModel) getBaGenericService().createItem(BaAccountModel.class);
		}
		getMapperFacade().map(accountRef, accountModel, context);
		getBaGenericService().saveItem(accountModel);
		return accountModel;
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
