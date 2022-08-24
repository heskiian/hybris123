/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * Populates value of type attribute from {@link ProductData} to {@link ProductWsDTO}
 *
 * @since 2105
 */
public class TmaProductOfferingTypeAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{
	private Map<String, String> productOfferingType;

	public TmaProductOfferingTypeAttributeMapper(final Map<String, String> productOfferingType)
	{
		this.productOfferingType = productOfferingType;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (StringUtils.isNotEmpty(source.getItemType()))
		{
			target.setType(getProductOfferingType().get(source.getItemType()));
		}
	}

	protected Map<String, String> getProductOfferingType()
	{
		return productOfferingType;
	}
}

