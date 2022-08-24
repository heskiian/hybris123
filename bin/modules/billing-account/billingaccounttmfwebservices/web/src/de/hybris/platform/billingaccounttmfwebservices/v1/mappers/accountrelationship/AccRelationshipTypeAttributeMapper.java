/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.accountrelationship;

import de.hybris.platform.billingaccountservices.model.BaAccountBalanceModel;
import de.hybris.platform.billingaccountservices.model.BaAccountRelationshipModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountBalance;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountRelationship;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for type attribute between {@link BaAccountBalanceModel} and {@link AccountBalance}
 *
 * @since 2105
 */
public class AccRelationshipTypeAttributeMapper
		extends BaAttributeMapper<BaAccountRelationshipModel, AccountRelationship>
{
	public AccRelationshipTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaAccountRelationshipModel source, final AccountRelationship target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
