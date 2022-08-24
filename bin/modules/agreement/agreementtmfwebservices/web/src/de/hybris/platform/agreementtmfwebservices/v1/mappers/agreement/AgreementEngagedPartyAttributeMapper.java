/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreement;

import de.hybris.platform.agreementservices.enums.AgrPartyType;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.model.AgrPartyModel;
import de.hybris.platform.agreementservices.model.AgrPartyRoleModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for engaged party attribute between {@link AgrAgreementModel} and {@link Agreement}
 *
 * @since 2108
 */
public class AgreementEngagedPartyAttributeMapper extends AgrAttributeMapper<AgrAgreementModel, Agreement>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementEngagedPartyAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementModel source, final Agreement target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getPartyRoles()))
		{
			return;
		}

		final List<RelatedParty> relatedPartyList = new ArrayList<>();
		source.getPartyRoles().forEach(partyRoleModel -> {
			final RelatedParty relatedPartyDto = getMapperFacade().map(partyRoleModel.getParty(), RelatedParty.class, context);
			relatedPartyDto.setRole(partyRoleModel.getRole());
			relatedPartyList.add(relatedPartyDto);
		});

		target.setEngagedParty(relatedPartyList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Agreement target, final AgrAgreementModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getEngagedParty()))
		{
			return;
		}

		source.setPartyRoles(getAllPartyRoles(target.getEngagedParty(), context));
	}

	private Set<AgrPartyRoleModel> getAllPartyRoles(final List<RelatedParty> relatedParties, final MappingContext context)
	{
		final Set<AgrPartyRoleModel> result = new HashSet<>();
		relatedParties.forEach(relatedParty -> {

			final AgrPartyModel agrPartyModel = getParty(relatedParty.getId(), relatedParty.getAtreferredType());

			getMapperFacade().map(relatedParty, agrPartyModel, context);
			getAgrGenericService().saveItem(agrPartyModel);
			final AgrPartyRoleModel agrPartyRoleModel = (AgrPartyRoleModel) getAgrGenericService()
					.createItem(AgrPartyRoleModel.class);
			if (StringUtils.isNotEmpty(relatedParty.getRole()))
			{
				agrPartyRoleModel.setRole(relatedParty.getRole());
			}
			agrPartyRoleModel.setParty(agrPartyModel);

			getAgrGenericService().saveItem(agrPartyRoleModel);
			result.add(agrPartyRoleModel);
		});
		return result;
	}

	private AgrPartyModel getParty(final String id, final String type)
	{
		AgrPartyModel agrPartyModel = (AgrPartyModel) getAgrGenericService()
				.getItem(AgrPartyModel._TYPECODE, id);
		if (agrPartyModel == null)
		{
			agrPartyModel = (AgrPartyModel) getAgrGenericService().createItem(AgrPartyModel.class);
			agrPartyModel.setType(AgrPartyType.valueOf(type));
		}

		return agrPartyModel;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}
}
