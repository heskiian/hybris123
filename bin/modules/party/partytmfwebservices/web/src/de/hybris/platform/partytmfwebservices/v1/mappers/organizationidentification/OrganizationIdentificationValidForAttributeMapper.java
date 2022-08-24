/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.organizationidentification;

import de.hybris.platform.partyservices.model.PmIdentificationModel;
import de.hybris.platform.partytmfwebservices.v1.dto.OrganizationIdentification;
import de.hybris.platform.partytmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for valid for attribute between {@link PmIdentificationModel} and {@link OrganizationIdentification}
 *
 * @since 2108
 */
public class OrganizationIdentificationValidForAttributeMapper
		extends PmAttributeMapper<PmIdentificationModel, OrganizationIdentification>
{
	public OrganizationIdentificationValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmIdentificationModel source, final OrganizationIdentification target,
			final MappingContext context)
	{
		if (source.getStartDate() == null && source.getEndDate() == null)
		{
			return;
		}

		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getStartDate());
		timePeriod.setEndDateTime(source.getEndDate());

		target.setValidFor(timePeriod);
	}
}
