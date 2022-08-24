/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.paymentref;

import de.hybris.platform.billmanagementservices.model.BmPartyPaymentModel;
import de.hybris.platform.billmanagementtmfwebservices.constants.BillmanagementtmfwebservicesConstants;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.PaymentRef;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atSchemaLocation attribute between {@link BmPartyPaymentModel} and {@link PaymentRef}
 *
 * @since 2108
 */
public class PaymentRefAtSchemaLocationAttributeMapper extends BmAttributeMapper<BmPartyPaymentModel, PaymentRef>
{
	public PaymentRefAtSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyPaymentModel source, final PaymentRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(BillmanagementtmfwebservicesConstants.BM_API_SCHEMA);
		}
	}
}
