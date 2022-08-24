/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cartcost;

import de.hybris.platform.b2ctelcofacades.data.TmaAbstractOrderChargePriceData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.CartCostWsDTO;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for name attribute between {@link TmaAbstractOrderChargePriceData} and
 * {@link CartCostWsDTO}
 *
 * @since 1911
 * @deprecated since 2011.1
 */
@Deprecated(since = "2011.1", forRemoval = true)
public class TmaCartCostNameAttributeMapper extends TmaAttributeMapper<TmaAbstractOrderChargePriceData, CartCostWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaAbstractOrderChargePriceData source, final CartCostWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (!ObjectUtils.isEmpty(source.getBillingTime()) && StringUtils.isNotEmpty(source.getBillingTime().getName()))
		{
			target.setName(source.getBillingTime().getName());
		}
	}
}
