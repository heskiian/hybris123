/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderTierUsageCompositePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for usage charge type attribute between
 * {@link TmaAbstractOrderTierUsageCompositePriceData} and {@link CartCostWsDTO}
 *
 * @since 2007
 */
public class TmaCartCostUsageChargeTypeAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderTierUsageCompositePriceData,
		CartCostWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(
			TmaAbstractOrderTierUsageCompositePriceData source, CartCostWsDTO target, MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (!ObjectUtils.isEmpty(source.getUsageChargeType()) && StringUtils.isNotEmpty(source.getUsageChargeType().getCode()))
		{
			target.setUsageChargeType(source.getUsageChargeType().getCode());
		}
	}
}
