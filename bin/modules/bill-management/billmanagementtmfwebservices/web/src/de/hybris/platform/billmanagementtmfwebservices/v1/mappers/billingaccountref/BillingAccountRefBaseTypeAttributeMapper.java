/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.billingaccountref;

import de.hybris.platform.billmanagementservices.model.BmBillingAccountModel;
import de.hybris.platform.billmanagementtmfwebservices.constants.BillmanagementtmfwebservicesConstants;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.BillingAccountRef;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atBaseType attribute between {@link BmBillingAccountModel} and
 * {@link BillingAccountRef}
 *
 * @since 2108
 */
public class BillingAccountRefBaseTypeAttributeMapper extends BmAttributeMapper<BmBillingAccountModel, BillingAccountRef>
{
	public BillingAccountRefBaseTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmBillingAccountModel source, final BillingAccountRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtbaseType(Config.getParameter(BillmanagementtmfwebservicesConstants.BILLING_ACCOUNT_BASE_TYPE));
		}
	}
}
