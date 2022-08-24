/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.specification;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.constants.B2ctelcocommercewebservicescommonsConstants;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecificationWsDTO;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * TmaProdSpecHrefAttributeMapper populates value for href attribute between {@link TmaProductSpecificationData} and
 * {@link ProductSpecificationWsDTO}.
 *
 * @since 1907
 */
public class TmaProdSpecHrefAttributeMapper extends TmaAttributeMapper<TmaProductSpecificationData, ProductSpecificationWsDTO>
{

	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecificationData source, final ProductSpecificationWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		target.setHref(B2ctelcocommercewebservicescommonsConstants.PRODUCT_SPECIFICATION_API_URL + source.getId());
	}

}
