/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.porelationship;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PoRelationshipWsDTO;
import de.hybris.platform.commercefacades.product.data.ProductData;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for href attribute between {@link ProductData} and {@link PoRelationshipWsDTO}
 *
 * @since 1907
 */
public class TmaPoRelationshipRefHrefAttributeMapper extends TmaAttributeMapper<ProductData, PoRelationshipWsDTO>
{

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final PoRelationshipWsDTO target,
			final MappingContext context)
	{

		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (StringUtils.isNotEmpty(source.getCode()))
		{
			target.setHref(source.getUrl());
		}
	}
}
