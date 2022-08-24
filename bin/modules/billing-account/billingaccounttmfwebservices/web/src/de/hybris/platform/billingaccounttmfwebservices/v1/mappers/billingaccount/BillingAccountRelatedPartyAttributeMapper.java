/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.enums.BaPartyType;
import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.model.BaPartyModel;
import de.hybris.platform.billingaccountservices.model.BaPartyRoleModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for relatedParties attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2105
 */
public class BillingAccountRelatedPartyAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillingAccountRelatedPartyAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (CollectionUtils.isEmpty(source.getPartyRoles()))
		{
			return;
		}

		final List<RelatedParty> relatedPartyList = source.getPartyRoles().stream()
				.map(partyRoleModel -> getMapperFacade().map(partyRoleModel, RelatedParty.class, context))
				.collect(Collectors.toList());

		target.setRelatedParty(relatedPartyList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillingAccount target, final BaBillingAccountModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getRelatedParty()))
		{
			return;
		}

		source.setPartyRoles(getAllPartyRoles(target.getRelatedParty(), context));
	}

	private Set<BaPartyRoleModel> getAllPartyRoles(final List<RelatedParty> relatedParties, final MappingContext context)
	{
		final Set<BaPartyRoleModel> result = new HashSet<>();
		relatedParties.forEach(relatedParty -> {
			BaPartyModel baPartyModel = (BaPartyModel) getBaGenericService().getItem(BaPartyModel._TYPECODE, relatedParty.getId());
			if (baPartyModel == null)
			{
				baPartyModel = (BaPartyModel) getBaGenericService().createItem(BaPartyModel.class);
				baPartyModel.setType(BaPartyType.valueOf(relatedParty.getAtreferredType()));
			}
			getMapperFacade().map(relatedParty, baPartyModel, context);
			getBaGenericService().saveItem(baPartyModel);

			final BaPartyRoleModel baPartyRoleModel = (BaPartyRoleModel) getBaGenericService()
					.createItem(BaPartyRoleModel.class);
			baPartyRoleModel.setParty(baPartyModel);
			if (StringUtils.isNotEmpty(relatedParty.getRole()))
			{
				baPartyRoleModel.setRole(relatedParty.getRole());
			}

			result.add(baPartyRoleModel);
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
