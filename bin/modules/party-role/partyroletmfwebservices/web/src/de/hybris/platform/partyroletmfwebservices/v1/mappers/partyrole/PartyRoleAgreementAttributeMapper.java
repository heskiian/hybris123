/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.partyrole;

import de.hybris.platform.partyroleservices.model.PrBusinessInteractionModel;
import de.hybris.platform.partyroleservices.model.PrBusinessInteractionRoleModel;
import de.hybris.platform.partyroleservices.model.PrPartyRoleModel;
import de.hybris.platform.partyroletmfwebservices.v1.dto.AgreementRef;
import de.hybris.platform.partyroletmfwebservices.v1.dto.PartyRole;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for agreement attribute between {@link PrPartyRoleModel} and {@link PartyRole}
 *
 * @since 2108
 */
public class PartyRoleAgreementAttributeMapper extends PrAttributeMapper<PrPartyRoleModel, PartyRole>
{
	private MapperFacade mapperFacade;

	public PartyRoleAgreementAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PrPartyRoleModel source, final PartyRole target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getInteractionRoles()))
		{
			return;
		}

		final List<PrBusinessInteractionModel> businessInteractions = source.getInteractionRoles().stream()
				.filter((PrBusinessInteractionRoleModel businessInteractionRole) ->
						businessInteractionRole.getBusinessInteraction() != null)
				.map(PrBusinessInteractionRoleModel::getBusinessInteraction).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(businessInteractions))
		{
			return;
		}

		target.setAgreement(getMapperFacade().mapAsList(businessInteractions, AgreementRef.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}

