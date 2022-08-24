/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.geographicaddress;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.GeographicAddress;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.commercefacades.user.data.AddressData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps related party for shopping cart attribute between
 * {@link AddressData} and {@link GeographicAddress}
 *
 * @since 1907
 */
public class GeographicAddressRelatedPartyAttributeMapper extends TmaAttributeMapper<AddressData, GeographicAddress>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(AddressData source, GeographicAddress target, MappingContext context)
	{
		if (source.getUser() == null)
		{
			return;
		}
		final List<RelatedPartyRef> relatedPartyRefs = new ArrayList<>();
		final RelatedPartyRef addressRelatedParty = getMapperFacade().map(source.getUser(), RelatedPartyRef.class, context);
		relatedPartyRefs.add(addressRelatedParty);
		target.setRelatedParty(relatedPartyRefs);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
