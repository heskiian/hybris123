/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.party;

import de.hybris.platform.partyservices.model.PmBusinessInteractionRoleModel;
import de.hybris.platform.partyservices.model.PmPartyModel;
import de.hybris.platform.partyservices.model.PmPartyRoleAssociationModel;
import de.hybris.platform.partyservices.model.PmPartyRoleModel;
import de.hybris.platform.partytmfwebservices.v1.dto.Party;
import de.hybris.platform.partytmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for related party attribute between {@link PmPartyModel} and {@link Party}
 *
 * @since 2108
 */
public class PartyRelatedPartyAttributeMapper extends PmAttributeMapper<PmPartyModel, Party>
{
	private MapperFacade mapperFacade;

	public PartyRelatedPartyAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmPartyModel source, final Party target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getPartyRoles()))
		{
			return;
		}

		final List<PmPartyRoleAssociationModel> partyRoleAssociations = getPartRoleAssociations(source);
		final List<PmBusinessInteractionRoleModel> businessInteractionRoles = getBusinessInteractionRoles(source);

		final List<RelatedParty> relatedParties = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(partyRoleAssociations))
		{
			relatedParties.addAll(getMapperFacade().mapAsList(partyRoleAssociations, RelatedParty.class, context));
		}

		if (CollectionUtils.isNotEmpty(businessInteractionRoles))
		{
			relatedParties.addAll(getMapperFacade().mapAsList(businessInteractionRoles, RelatedParty.class, context));
		}

		if (CollectionUtils.isEmpty(relatedParties))
		{
			return;
		}

		target.setRelatedParty(relatedParties);
	}

	/**
	 * Returns the party role association list for the provided party.
	 *
	 * @param party
	 * 		The party
	 * @return List of {@link PmPartyRoleAssociationModel}
	 */
	protected List<PmPartyRoleAssociationModel> getPartRoleAssociations(final PmPartyModel party)
	{
		final List<PmPartyRoleAssociationModel> partyRoleAssociations = new ArrayList<>();

		party.getPartyRoles().forEach((PmPartyRoleModel partyRole) -> {
			if (CollectionUtils.isNotEmpty(partyRole.getPartyRoleAssociationInvolvedWith()))
			{
				partyRoleAssociations.addAll(getPartRoleAssociations(partyRole, partyRole.getPartyRoleAssociationInvolvedWith()));
			}

			if (CollectionUtils.isNotEmpty(partyRole.getPartyRoleAssociationInvolves()))
			{
				partyRoleAssociations.addAll(getPartRoleAssociations(partyRole, partyRole.getPartyRoleAssociationInvolves()));
			}
		});

		return partyRoleAssociations;
	}

	/**
	 * Returns the business interaction role list for the provided party.
	 *
	 * @param party
	 * 		The party
	 * @return List of {@link PmBusinessInteractionRoleModel}
	 */
	protected List<PmBusinessInteractionRoleModel> getBusinessInteractionRoles(final PmPartyModel party)
	{
		final List<PmBusinessInteractionRoleModel> businessInteractionRoles = new ArrayList<>();

		party.getPartyRoles().forEach((PmPartyRoleModel partyRole) -> {
			if (CollectionUtils.isEmpty(partyRole.getInteractionRoles()))
			{
				return;
			}

			partyRole.getInteractionRoles().forEach((PmBusinessInteractionRoleModel businessInteractionRole) -> {
				if (businessInteractionRole.getBusinessInteraction() == null ||
						CollectionUtils.isEmpty(businessInteractionRole.getBusinessInteraction().getInteractionRoles()))
				{
					return;
				}

				businessInteractionRoles.addAll(businessInteractionRole.getBusinessInteraction().getInteractionRoles().stream()
						.filter((PmBusinessInteractionRoleModel interactionRole) -> !interactionRole.equals(businessInteractionRole))
						.collect(Collectors.toList()));
			});
		});

		return businessInteractionRoles;
	}

	private List<PmPartyRoleAssociationModel> getPartRoleAssociations(final PmPartyRoleModel partyRole,
			final Set<PmPartyRoleAssociationModel> partyRoleAssociations)
	{
		final List<PmPartyRoleAssociationModel> associations = new ArrayList<>();

		partyRoleAssociations.forEach((PmPartyRoleAssociationModel partyRoleAssociation) -> {
			if (partyRole.equals(partyRoleAssociation.getPartyRoleInvolvedWith()))
			{
				partyRoleAssociation.setPartyRoleInvolvedWith(null);
				associations.add(partyRoleAssociation);
			}

			if (partyRole.equals(partyRoleAssociation.getPartyRoleInvolves()))
			{
				partyRoleAssociation.setPartyRoleInvolves(null);
				associations.add(partyRoleAssociation);
			}
		});

		return associations;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
