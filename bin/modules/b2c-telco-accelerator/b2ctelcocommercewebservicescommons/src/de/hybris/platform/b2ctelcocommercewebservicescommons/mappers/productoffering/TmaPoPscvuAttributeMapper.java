/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProdSpecCharValueUseWsDTO;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;

/**
 * This class populates value of productSpecCharValueUses attribute from
 * {@link ProductData} to {@link ProductWsDTO}
 *
 * @since 2105
 */
public class TmaPoPscvuAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO> {

	private MapperFacade mapperFacade;

	public TmaPoPscvuAttributeMapper(final MapperFacade mapperFacade) {
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context) {
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (CollectionUtils.isEmpty(source.getProductSpecCharValueUses())) {
			return;
		}
		target.setPoSpecCharValueUses(getMapperFacade().mapAsList(source.getProductSpecCharValueUses(), ProdSpecCharValueUseWsDTO.class, context));
						
	}

	protected MapperFacade getMapperFacade() {
		return mapperFacade;
	}
}
