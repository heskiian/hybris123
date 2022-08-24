/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.accounttaxexemption;

import de.hybris.platform.billingaccountservices.model.BaAccountTaxExemptionModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.AccountTaxExemption;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for type attribute between {@link BaAccountTaxExemptionModel} and {@link AccountTaxExemption}
 *
 * @since 2105
 */
public class AccTaxExemptionAtTypeAttributeMapper extends BaAttributeMapper<BaAccountTaxExemptionModel, AccountTaxExemption>
{
	public AccTaxExemptionAtTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaAccountTaxExemptionModel source, final AccountTaxExemption target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(target.getClass().getSimpleName());
		}
	}
}
