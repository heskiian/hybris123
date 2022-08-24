/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.appliedpayment;

import de.hybris.platform.billmanagementservices.model.BmPartyPaymentItemModel;
import de.hybris.platform.billmanagementtmfwebservices.constants.BillmanagementtmfwebservicesConstants;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.AppliedPayment;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atSchemaLocation attribute between {@link BmPartyPaymentItemModel} and {@link AppliedPayment}
 *
 * @since 2108
 */
public class AppliedPaymentSchemaLocationAttributeMapper extends BmAttributeMapper<BmPartyPaymentItemModel, AppliedPayment>
{
	public AppliedPaymentSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyPaymentItemModel source, final AppliedPayment target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(BillmanagementtmfwebservicesConstants.BM_API_SCHEMA);
		}
	}
}
