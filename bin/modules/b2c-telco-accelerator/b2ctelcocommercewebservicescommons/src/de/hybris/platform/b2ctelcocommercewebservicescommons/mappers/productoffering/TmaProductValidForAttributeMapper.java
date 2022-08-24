/*
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TimePeriodWsDTO;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * TmaProductValidForAttributeMapper populates value of validFor attribute from {@link ProductData} to
 * {@link ProductWsDTO}
 *
 * @since 1907
 */
public class TmaProductValidForAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		final TimePeriodWsDTO timePeriod = new TimePeriodWsDTO();
		if (source.getOnlineDate() != null)
		{
			timePeriod.setStartDateTime(source.getOnlineDate());
		}

		if (source.getOfflineDate() != null)
		{
			timePeriod.setEndDateTime(source.getOfflineDate());
		}

		target.setValidFor(timePeriod);
	}
}
