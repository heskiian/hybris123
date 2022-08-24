/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.TimePeriod;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 1903
 */
public class PoValidForAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getCode()))
		{
			return;
		}

		final TimePeriod timePeriod = new TimePeriod();
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
