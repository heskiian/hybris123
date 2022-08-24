/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.party;

import de.hybris.platform.partyservices.model.PmPartyModel;
import de.hybris.platform.partytmfwebservices.v1.dto.ExternalReference;
import de.hybris.platform.partytmfwebservices.v1.dto.Party;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for external reference attribute between {@link PmPartyModel} and {@link Party}
 *
 * @since 2108
 */
public class PartyExternalReferenceAttributeMapper extends PmAttributeMapper<PmPartyModel, Party>
{
	private MapperFacade mapperFacade;

	public PartyExternalReferenceAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PmPartyModel source, final Party target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getExternalReferences()))
		{
			return;
		}

		target.setExternalReference(getMapperFacade().mapAsList(source.getExternalReferences(), ExternalReference.class, context));
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
