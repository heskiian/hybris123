/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.offeringgroup;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.OfferingGroupWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferingGroupData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for child product offerings attribute between {@link TmaOfferingGroupData} and
 * {@link OfferingGroupWsDTO}
 *
 * @since 2011
 */
public class TmaOfferingGroupChildOfferingsAttributeMapper extends TmaAttributeMapper<TmaOfferingGroupData, OfferingGroupWsDTO>
{
	/**
	 * Mapper facade
	 */
	private final MapperFacade mapperFacade;

	public TmaOfferingGroupChildOfferingsAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(TmaOfferingGroupData source, OfferingGroupWsDTO target, MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getChildProductOfferings()))
		{
			return;
		}

		final List<ProductWsDTO> childProductOfferings = new ArrayList<>();
		source.getChildProductOfferings().forEach(productData ->
		{
			final ProductWsDTO childProduct = getMapperFacade()
					.map(productData, ProductWsDTO.class, context);
			childProductOfferings.add(childProduct);
		});

		target.setChildProductOfferings(childProductOfferings);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}