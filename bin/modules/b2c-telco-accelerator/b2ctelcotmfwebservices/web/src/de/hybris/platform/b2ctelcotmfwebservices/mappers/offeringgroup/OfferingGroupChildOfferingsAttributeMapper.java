/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.offeringgroup;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BundledProductOffering;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OfferingGroup;
import de.hybris.platform.b2ctelcofacades.data.TmaOfferingGroupData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for child product offerings attribute between {@link TmaOfferingGroupData} and
 * {@link OfferingGroup}
 *
 * @since 1903
 */
public class OfferingGroupChildOfferingsAttributeMapper extends TmaAttributeMapper<TmaOfferingGroupData, OfferingGroup>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(TmaOfferingGroupData source, OfferingGroup target, MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getChildProductOfferings()))
		{
			return;
		}

		final List<BundledProductOffering> childProductOfferings = new ArrayList<>();
		source.getChildProductOfferings().forEach(productData ->
		{
			final BundledProductOffering bundledProductOfferingWsDto = getMapperFacade()
					.map(productData, BundledProductOffering.class, context);
			childProductOfferings.add(bundledProductOfferingWsDto);
		});

		target.setChildProductOffering(childProductOfferings);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
