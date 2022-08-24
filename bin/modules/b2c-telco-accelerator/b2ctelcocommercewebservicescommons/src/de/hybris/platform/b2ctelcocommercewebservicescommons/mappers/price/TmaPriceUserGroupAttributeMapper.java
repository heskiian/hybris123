/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for user attribute between {@link PriceData} and
 * {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPriceUserGroupAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		if (StringUtils.isEmpty(source.getUserPriceGroupID()))
		{
			return;
		}
		target.setUserPriceGroupID(source.getUserPriceGroupID());
	}
}
