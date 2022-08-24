/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.contact;

import de.hybris.platform.billingaccountservices.enums.BaPartyType;
import de.hybris.platform.billingaccountservices.model.BaContactModel;
import de.hybris.platform.billingaccountservices.model.BaPartyModel;
import de.hybris.platform.billingaccountservices.model.BaPartyRoleModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Contact;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for relatedParty attribute between {@link BaContactModel} and {@link Contact}
 *
 * @since 2111
 */
public class ContactRelatedPartyAttributeMapper extends BaAttributeMapper<BaContactModel, Contact>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public ContactRelatedPartyAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaContactModel source, final Contact target,
			final MappingContext context)
	{
		if (source.getRelatedParty() != null)
		{
			getMapperFacade().map(source.getRelatedParty(), RelatedParty.class, context);
		}
	}

	@Override
	public void populateSourceAttributeFromTarget(final Contact target, final BaContactModel source,
			final MappingContext context)
	{
		if (target.getRelatedParty() == null)
		{
			return;
		}

		BaPartyModel baPartyModel = (BaPartyModel) getBaGenericService()
				.getItem(BaPartyModel._TYPECODE, target.getRelatedParty().getId());
		if (baPartyModel == null)
		{
			baPartyModel = (BaPartyModel) getBaGenericService().createItem(BaPartyModel.class);
			baPartyModel.setType(BaPartyType.valueOf(target.getRelatedParty().getAtreferredType()));
		}
		getMapperFacade().map(target.getRelatedParty(), baPartyModel, context);
		getBaGenericService().saveItem(baPartyModel);

		final BaPartyRoleModel baPartyRoleModel = (BaPartyRoleModel) getBaGenericService().createItem(BaPartyRoleModel.class);
		baPartyRoleModel.setParty(baPartyModel);
		if (StringUtils.isNotEmpty(target.getRelatedParty().getRole()))
		{
			baPartyRoleModel.setRole(target.getRelatedParty().getRole());
		}

		source.setRelatedParty(baPartyRoleModel);
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
