/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billformat;

import de.hybris.platform.billingaccountservices.model.BaBillFormatModel;
import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillFormatRefOrValue;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link BaBillFormatModel} and {@link BillFormatRefOrValue}
 *
 * @since 2105
 */
public class BillFormatHrefAttributeMapper extends BaAttributeMapper<BaBillFormatModel, BillFormatRefOrValue>
{
	public BillFormatHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillFormatModel source, final BillFormatRefOrValue target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(BillingaccounttmfwebservicesConstants.BILL_FORMAT_API_URL + source.getId());
		}
	}
}
