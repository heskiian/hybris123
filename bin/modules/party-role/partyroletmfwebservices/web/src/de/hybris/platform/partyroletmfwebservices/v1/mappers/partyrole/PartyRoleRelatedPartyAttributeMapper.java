/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.partyrole;

import de.hybris.platform.partyroleservices.model.PrBusinessInteractionRoleModel;
import de.hybris.platform.partyroleservices.model.PrPartyRoleAssociationModel;
import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.PartyRole;
import de.hybris.platform.partyroletmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for related party attribute between {@link PrPartyRoleModel} and {@link PartyRole}
 *
 * @since 2108
 */
public class PartyRoleRelatedPartyAttributeMapper extends PrAttributeMapper<PrPartyRoleModel, PartyRole>
{
	private MapperFacade mapperFacade;

	public PartyRoleRelatedPartyAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PrPartyRoleModel source, final PartyRole target,
			final MappingContext context)
	{
		final List<PrPartyRoleAssociationModel> partyRoleAssociations = getPartRoleAssociations(source);
		final List<PrBusinessInteractionRoleModel> businessInteractionRoles = getBusinessInteractionRoles(source);

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
	 * Returns the party role association list for the provided party role.
	 *
	 * @param partyRole
	 * 		The party role
	 * @return List of {@link PrPartyRoleAssociationModel}
	 */
	protected List<PrPartyRoleAssociationModel> getPartRoleAssociations(final PrPartyRoleModel partyRole)
	{
		final List<PrPartyRoleAssociationModel> partyRoleAssociations = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(partyRole.getInvolvedWithAssociations()))
		{
			partyRoleAssociations.addAll(getPartRoleAssociations(partyRole, partyRole.getInvolvedWithAssociations()));
		}

		if (CollectionUtils.isNotEmpty(partyRole.getInvolvesAssociations()))
		{
			partyRoleAssociations.addAll(getPartRoleAssociations(partyRole, partyRole.getInvolvesAssociations()));
		}

		return partyRoleAssociations;
	}

	/**
	 * Returns the business interaction role list for the provided party role.
	 *
	 * @param partyRole
	 * 		The party role
	 * @return List of {@link PrBusinessInteractionRoleModel}
	 */
	protected List<PrBusinessInteractionRoleModel> getBusinessInteractionRoles(final PrPartyRoleModel partyRole)
	{
		if (CollectionUtils.isEmpty(partyRole.getInteractionRoles()))
		{
			return Collections.emptyList();
		}

		final List<PrBusinessInteractionRoleModel> businessInteractionRoles = new ArrayList<>();

		partyRole.getInteractionRoles().forEach((PrBusinessInteractionRoleModel businessInteractionRole) ->
			businessInteractionRoles.addAll(businessInteractionRole.getBusinessInteraction().getInteractionRoles().stream()
					.filter((PrBusinessInteractionRoleModel interactionRole) -> !interactionRole.equals(businessInteractionRole))
					.collect(Collectors.toList()))
		);


		return businessInteractionRoles;
	}

	private List<PrPartyRoleAssociationModel> getPartRoleAssociations(final PrPartyRoleModel partyRole,
			final Set<PrPartyRoleAssociationModel> partyRoleAssociations)
	{
		final List<PrPartyRoleAssociationModel> associations = new ArrayList<>();

		partyRoleAssociations.forEach((PrPartyRoleAssociationModel partyRoleAssociation) -> {
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
