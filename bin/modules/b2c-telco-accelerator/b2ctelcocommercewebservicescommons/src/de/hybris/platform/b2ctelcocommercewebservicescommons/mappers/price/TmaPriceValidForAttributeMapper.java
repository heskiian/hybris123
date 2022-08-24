/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TimePeriodWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * TmaPriceValidForAttributeMapper populates value of ValidFor attribute from {@link PriceData} to
 * {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPriceValidForAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (StringUtils.isEmpty(source.getCode()))
		{
			return;
		}

		final TimePeriodWsDTO timePeriod = new TimePeriodWsDTO();
		if (source.getStartTime() != null)
		{
			timePeriod.setStartDateTime(source.getStartTime());
		}

		if (source.getEndTime() != null)
		{
			timePeriod.setEndDateTime(source.getEndTime());
		}

		target.setValidFor(timePeriod);
	}

}
