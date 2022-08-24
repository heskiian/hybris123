/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billpresentationmedia;

import de.hybris.platform.billingaccountservices.model.BaBillPresentationMediaModel;
import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillPresentationMediaRefOrValue;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for schema location attribute between {@link BaBillPresentationMediaModel} and
 * {@link BillPresentationMediaRefOrValue}
 *
 * @since 2105
 */
public class BillPresentationMediaSchemaLocationAttributeMapper
		extends BaAttributeMapper<BaBillPresentationMediaModel, BillPresentationMediaRefOrValue>
{
	public BillPresentationMediaSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillPresentationMediaModel source,
			final BillPresentationMediaRefOrValue target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(BillingaccounttmfwebservicesConstants.BA_API_SCHEMA);
		}
	}
}
