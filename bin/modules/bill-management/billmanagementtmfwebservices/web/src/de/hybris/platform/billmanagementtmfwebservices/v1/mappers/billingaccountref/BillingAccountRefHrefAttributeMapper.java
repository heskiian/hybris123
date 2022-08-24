/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.billingaccountref;

import de.hybris.platform.billmanagementservices.model.BmBillingAccountModel;
import de.hybris.platform.billmanagementtmfwebservices.constants.BillmanagementtmfwebservicesConstants;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.BillingAccountRef;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;

/**
 * This attribute Mapper class maps data for href attribute between {@link BmBillingAccountModel} and
 * {@link BillingAccountRef}
 *
 * @since 2108
 */
public class BillingAccountRefHrefAttributeMapper extends BmAttributeMapper<BmBillingAccountModel, BillingAccountRef>
{
	public BillingAccountRefHrefAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmBillingAccountModel source, final BillingAccountRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setHref(BillmanagementtmfwebservicesConstants.BILLING_ACCOUNT_API_URL + source.getId());
		}
	}
}
