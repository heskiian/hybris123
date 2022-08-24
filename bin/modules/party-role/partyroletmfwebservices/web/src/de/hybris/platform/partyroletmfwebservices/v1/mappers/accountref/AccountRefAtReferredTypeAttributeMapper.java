/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partyroletmfwebservices.v1.mappers.accountref;

import de.hybris.platform.partyroleservices.model.PrAccountModel;
import de.hybris.platform.partyroletmfwebservices.constants.PartyroletmfwebservicesConstants;
import de.hybris.platform.partyroletmfwebservices.v1.dto.AccountRef;
import de.hybris.platform.partyroletmfwebservices.v1.mappers.PrAttributeMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for at referred type attribute between {@link PrAccountModel} and {@link AccountRef}
 *
 * @since 2108
 */
public class AccountRefAtReferredTypeAttributeMapper extends PrAttributeMapper<PrAccountModel, AccountRef>
{
	public AccountRefAtReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final PrAccountModel source, final AccountRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(PartyroletmfwebservicesConstants.ACCOUNT_DEFAULT_REFERRED_TYPE);
		}
	}
}

