/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.creditprofile;

import de.hybris.platform.partyroleservices.model.PrCreditProfileModel;
import de.hybris.platform.partyroletmfwebservices.constants.PartyroletmfwebservicesConstants;
import de.hybris.platform.partyroletmfwebservices.v1.dto.CreditProfile;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at schema location attribute between {@link PrCreditProfileModel} and {@link CreditProfile}
 *
 * @since 2108
 */
public class CreditProfileAtSchemaLocationAttributeMapper extends PrAttributeMapper<PrCreditProfileModel, CreditProfile>
{
	public CreditProfileAtSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrCreditProfileModel source, final CreditProfile target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAtschemaLocation(PartyroletmfwebservicesConstants.PARTY_ROLE_API_SCHEMA);
	}
}
