/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.billmanagementservices.enums.BmPartyType;
import de.hybris.platform.billmanagementservices.model.BmPartyRoleModel;
import de.hybris.platform.billmanagementtmfwebservices.constants.BillmanagementtmfwebservicesConstants;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.RelatedPartyRef;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link BmPartyRoleModel} and {@link RelatedPartyRef}
 *
 * @since 2108
 */
public class RelatedPartyReferredTypeAttributeMapper extends BmAttributeMapper<BmPartyRoleModel, RelatedPartyRef>
{
	public RelatedPartyReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyRoleModel source, final RelatedPartyRef target,
			final MappingContext context)
	{
		if (source.getParty() == null || StringUtils.isEmpty(source.getParty().getId()) || source.getParty().getType() == null)
		{
			return;
		}

		if (source.getParty().getType().equals(BmPartyType.INDIVIDUAL))
		{
			target.setAtreferredType(
					Config.getParameter(BillmanagementtmfwebservicesConstants.RELATED_PARTY_REFERRED_TYPE_INDIVIDUAL));
		}
		else
		{
			target.setAtreferredType(
					Config.getParameter(BillmanagementtmfwebservicesConstants.RELATED_PARTY_REFERRED_TYPE_ORGANIZATION));
		}
	}
}
