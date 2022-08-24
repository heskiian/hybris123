/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.partyrole;

import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.IndividualIdentification;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PartyRole;
import de.hybris.platform.commercefacades.user.data.PrincipalData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for individual identification attribute between {@link PrincipalData} and
 * {@link PartyRole}
 *
 * @since 1911
 */
public class PartyRoleIdentificationAttributeMapper extends TmaAttributeMapper<PrincipalData, PartyRole>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PrincipalData source, final PartyRole target, final MappingContext context)
	{
		final List<TmaIdentificationData> identificationDataList = source.getIdentifications();

		if (CollectionUtils.isNotEmpty(identificationDataList))
		{
			final List<IndividualIdentification> individualIdentifications = new ArrayList<>();
			identificationDataList.forEach(identificationData -> {
				final IndividualIdentification individualIdentification = getMapperFacade().map(identificationData,
						IndividualIdentification.class, context);
				individualIdentifications.add(individualIdentification);
			});
			target.setIndividualIdentification(individualIdentifications);
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
