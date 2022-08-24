/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.subscription;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaBillingAccountRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductWsDto;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This Mapper class maps data in between {@link TmaSubscriptionBaseData} and {@link TmaProductWsDto}
 *
 * @since 1810
 */
public class TmaBillingAccountRefWsDtoMapper extends AbstractCustomMapper<TmaSubscriptionBaseData, TmaBillingAccountRefWsDto>
{

	private static final String BILLINGACCOUNTDATA = "tmaBillingAccountData";
	private static final String BILLINGACCOUNTID = "id";

	@Override
	public void mapAtoB(final TmaSubscriptionBaseData a, final TmaBillingAccountRefWsDto b, final MappingContext context)
	{
		context.beginMappingField(BILLINGACCOUNTDATA, getAType(), a, BILLINGACCOUNTID, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getTmaBillingAccountData() != null)
			{
				b.setId(a.getTmaBillingAccountData().getBillingAccountId());
				b.setName(a.getTmaBillingAccountData().getBillingAccountId());
				b.setReferredType(Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_DEFAULT_REFERRED, StringUtils.EMPTY));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaBillingAccountRefWsDto b, final TmaSubscriptionBaseData a, final MappingContext context)
	{
		// other fields are mapped automatically
		context.beginMappingField(BILLINGACCOUNTID, getBType(), b, BILLINGACCOUNTDATA, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && a.getTmaBillingAccountData() != null)
			{
				a.getTmaBillingAccountData().setBillingAccountId(b.getId());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}
}