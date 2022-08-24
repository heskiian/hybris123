/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;

import ma.glasnost.orika.MappingContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * This attribute Mapper class maps data for ID attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1903
 */
public class PoPriceIdAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	/**
	 * @deprecated since 2007. Id is present on priceRow, no longer necessary to generate it.
	 */
	@Deprecated(since = "2007")
	private PoPriceIdGenerator poPriceIdGenerator;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setId(source.getCode());
		}
	}

	/**
	 * @deprecated since 2007.
	 */
	@Deprecated(since = "2007")
	@SuppressWarnings("WeakerAccess")
	protected PoPriceIdGenerator getPoPriceIdGenerator()
	{
		return poPriceIdGenerator;
	}

	/**
	 * @deprecated since 2007.
	 */
	@Deprecated(since = "2007")
	@Required
	public void setPoPriceIdGenerator(
			PoPriceIdGenerator poPriceIdGenerator)
	{
		this.poPriceIdGenerator = poPriceIdGenerator;
	}
}
