/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers;

import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaRelatedPartyWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaTimePeriodWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductWsDto;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.util.Config;
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * Mapper fills {@link TmaProductWsDto} from {@link CustomerData}
 *
 * @since 1810
 */
public class TmaRelatedPartyWsDtoMapper extends AbstractCustomMapper<CustomerData, TmaRelatedPartyWsDto>
{
	private static final String ID = "id";
	private static final String CUSTOMER_ID = "customerId";
	private static final String RELATED_PARTY_HREF = "href";
	private static final String DEACTIVATION_DATE = "deactivationDate";
	private static final String VALID_FOR = "validFor";

	@Override
	public void mapAtoB(final CustomerData a, final TmaRelatedPartyWsDto b, final MappingContext context)
	{
		mapDefaultAtoB(a, b, context);
		mapIdAtoB(a, b, context);
		mapValidForAtoB(a, b, context);
	}

	protected void mapIdAtoB(final CustomerData a, final TmaRelatedPartyWsDto b, final MappingContext context)
	{
		context.beginMappingField(CUSTOMER_ID, getAType(), a, ID, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getUid()))
			{
				b.setId(a.getUid());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	protected void mapDefaultAtoB(final CustomerData a, final TmaRelatedPartyWsDto b, final MappingContext context)
	{
		context.beginMappingField(CUSTOMER_ID, getAType(), a, RELATED_PARTY_HREF, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getUid()))
			{
				b.setHref(StringUtils.EMPTY);
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField(CUSTOMER_ID, getAType(), a, "referredType", getBType(), b);
		try
		{
			if (shouldMap(a, b, context))
			{
				b.setReferredType(Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_DEFAULT_REFERRED, StringUtils.EMPTY));
			}
		}
		finally
		{
			context.endMappingField();
		}

		context.beginMappingField(CUSTOMER_ID, getAType(), a, "role", getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && StringUtils.isNotEmpty(a.getCustomerId()))
			{
				b.setRole(Config.getString(B2ctelcotmfwebservicesConstants.TMA_API_DEFAULT_ROLE, StringUtils.EMPTY));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}


	protected void mapValidForAtoB(final CustomerData a, final TmaRelatedPartyWsDto b, final MappingContext context)
	{
		context.beginMappingField(DEACTIVATION_DATE, getAType(), a, VALID_FOR, getBType(), b);
		try
		{
			if (shouldMap(a, b, context) && a.getDeactivationDate() != null)
			{
				b.setValidFor(new TmaTimePeriodWsDto(new Date(), a.getDeactivationDate()));
			}
		}
		finally
		{
			context.endMappingField();
		}
	}

	@Override
	public void mapBtoA(final TmaRelatedPartyWsDto b, final CustomerData a, final MappingContext context)
	{
		// other fields are mapped automatically
		context.beginMappingField(VALID_FOR, getBType(), b, DEACTIVATION_DATE, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && b.getValidFor() != null)
			{
				a.setDeactivationDate(b.getValidFor().getEndDateTime());
			}
		}
		finally
		{
			context.endMappingField();
		}
		context.beginMappingField(ID, getBType(), b, CUSTOMER_ID, getAType(), a);
		try
		{
			if (shouldMap(b, a, context) && StringUtils.isNotEmpty(b.getId()))
			{
				a.setCustomerId(b.getId());
			}
		}
		finally
		{
			context.endMappingField();
		}
	}
}
