/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.europe1.model.PriceRowModel;

import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * This attribute Mapper class maps data for href attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1903
 * @deprecated since 2007.
 */
@Deprecated(since = "2007")
public class PoPriceHrefAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	/**
	 * @deprecated since 2007. Populate the id from {@link PriceRowModel} instead.
	 */
	@Deprecated(since = "2007")
	private PoPriceIdGenerator poPriceIdGenerator;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(B2ctelcotmfwebservicesConstants.PRODUCT_OFFERING_PRICE_API_URL + source.getCode());
		}
	}

	@SuppressWarnings("WeakerAccess")
	protected PoPriceIdGenerator getPoPriceIdGenerator()
	{
		return poPriceIdGenerator;
	}

	@Required
	public void setPoPriceIdGenerator(
			PoPriceIdGenerator poPriceIdGenerator)
	{
		this.poPriceIdGenerator = poPriceIdGenerator;
	}
}
