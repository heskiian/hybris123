/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingcyclespecification;

import de.hybris.platform.billingaccountservices.model.BaBillingCycleSpecificationModel;
import de.hybris.platform.billingaccounttmfwebservices.constants.BillingaccounttmfwebservicesConstants;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingCycleSpecificationRefOrValue;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link BaBillingCycleSpecificationModel} and
 * {@link BillingCycleSpecificationRefOrValue}
 *
 * @since 2105
 */
public class BillingCycleSpecReferredTypeAttributeMapper
		extends BaAttributeMapper<BaBillingCycleSpecificationModel, BillingCycleSpecificationRefOrValue>
{
	public BillingCycleSpecReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillingCycleSpecificationModel source,
			final BillingCycleSpecificationRefOrValue target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(
					Config.getParameter(BillingaccounttmfwebservicesConstants.BILLING_CYCLE_SPECIFICATION_REFERRED));
		}
	}
}
