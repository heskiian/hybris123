/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * TmaPoHasParentBPOAttributeMapper populates value of HasParentBpo attribute from {@link ProductData} to
 * {@link ProductWsDTO}
 *
 * @since 1911
 */
public class TmaPoHasParentBPOAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (!CollectionUtils.isEmpty(source.getParents()))
		{
			target.setHasParentBpos(Boolean.TRUE);
		}
	}
}
