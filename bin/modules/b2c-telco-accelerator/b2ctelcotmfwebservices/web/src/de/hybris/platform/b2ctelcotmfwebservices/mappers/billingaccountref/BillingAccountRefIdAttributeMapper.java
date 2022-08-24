/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.billingaccountref;

import de.hybris.platform.b2ctelcofacades.data.TmaBillingAccountData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BillingAccountRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for id attribute between {@link TmaBillingAccountData} and {@link BillingAccountRef}
 *
 * @since 1907
 */
public class BillingAccountRefIdAttributeMapper extends TmaAttributeMapper<TmaBillingAccountData, BillingAccountRef>
{
	@Override
	public void populateTargetAttributeFromSource(TmaBillingAccountData source, BillingAccountRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getBillingAccountId()))
		{
			target.setId(source.getBillingAccountId());
		}
	}
}
