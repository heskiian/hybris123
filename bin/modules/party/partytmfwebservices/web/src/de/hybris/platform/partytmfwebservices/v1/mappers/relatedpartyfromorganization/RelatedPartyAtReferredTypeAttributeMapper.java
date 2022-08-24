/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.relatedpartyfromorganization;

import de.hybris.platform.partyservices.model.PmOrganizationModel;
import de.hybris.platform.partytmfwebservices.constants.PartytmfwebservicesConstants;
import de.hybris.platform.partytmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at referred type attribute between {@link PmOrganizationModel} and {@link RelatedParty}
 *
 * @since 2108
 */
public class RelatedPartyAtReferredTypeAttributeMapper extends PmAttributeMapper<PmOrganizationModel, RelatedParty>
{
	public RelatedPartyAtReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PmOrganizationModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getId()))
		{
			return;
		}

		target.setAtreferredType(PartytmfwebservicesConstants.ORGANIZATION_DEFAULT_REFERRED_TYPE);
	}
}
