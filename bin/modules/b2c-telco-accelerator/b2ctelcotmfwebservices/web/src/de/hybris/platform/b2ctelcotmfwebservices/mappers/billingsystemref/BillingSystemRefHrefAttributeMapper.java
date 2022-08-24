/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.billingsystemref;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BillingSystemRef;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link String} and {@link BillingSystemRef}
 *
 * @since 1907
 */
public class BillingSystemRefHrefAttributeMapper extends TmaAttributeMapper<String, BillingSystemRef>
{
	@Override
	public void populateTargetAttributeFromSource(String source, BillingSystemRef target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.BILLING_SYSTEM_REF_API_URL + source);
		}
	}
}
