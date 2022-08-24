/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.relatedparty;

import de.hybris.platform.billingaccountservices.enums.BaPartyType;
import de.hybris.platform.billingaccountservices.model.BaPartyRoleModel;
import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.RelatedParty;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link BaPartyRoleModel} and {@link RelatedParty}
 *
 * @since 2105
 */
public class RelatedPartyHrefAttributeMapper extends BaAttributeMapper<BaPartyRoleModel, RelatedParty>
{
	public RelatedPartyHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaPartyRoleModel source, final RelatedParty target,
			final MappingContext context)
	{
		if (source.getParty() == null || StringUtils.isEmpty(source.getParty().getId()) || source.getParty().getType() == null)
		{
			return;
		}

		if (source.getParty().getType().equals(BaPartyType.INDIVIDUAL))
		{
			target.setHref(BillingaccounttmfwebservicesConstants.INDIVIDUAL_RELATED_PARTY_API_URL + source.getParty().getId());
		}
		else
		{
			target.setHref(BillingaccounttmfwebservicesConstants.ORGANIZATION_RELATED_PARTY_API_URL + source.getParty().getId());
		}
	}
}
