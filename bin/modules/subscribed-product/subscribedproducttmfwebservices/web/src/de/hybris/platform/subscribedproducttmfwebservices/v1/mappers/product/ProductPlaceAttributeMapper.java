/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiRelatedPlaceModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.RelatedPlaceRefOrValue;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for place attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductPlaceAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductPlaceAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getRelatedPlaces()))
		{
			return;
		}

		final List<RelatedPlaceRefOrValue> relatedPlaceRefOrValues = new ArrayList<>();
		source.getRelatedPlaces().forEach(spiRelatedPlaceModel -> {
			final RelatedPlaceRefOrValue relatedPlaceRefOrValue = getMapperFacade()
					.map(spiRelatedPlaceModel, RelatedPlaceRefOrValue.class, context);
			relatedPlaceRefOrValues.add(relatedPlaceRefOrValue);
		});

		target.setPlace(relatedPlaceRefOrValues);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getPlace()))
		{
			return;
		}

		source.setRelatedPlaces(getAllRelatedPlaces(target.getPlace(), context));
	}

	private Set<SpiRelatedPlaceModel> getAllRelatedPlaces(final List<RelatedPlaceRefOrValue> relatedPlaceRefOrValues,
			final MappingContext context)
	{
		final Set<SpiRelatedPlaceModel> result = new HashSet<>();
		relatedPlaceRefOrValues.forEach(relatedPlaceRefOrValue -> {
			SpiRelatedPlaceModel relatedPlaceModel = (SpiRelatedPlaceModel) getSpiGenericService()
					.getItem(SpiRelatedPlaceModel._TYPECODE, relatedPlaceRefOrValue.getId());
			if (relatedPlaceModel == null)
			{
				relatedPlaceModel = (SpiRelatedPlaceModel) getSpiGenericService().createItem(SpiRelatedPlaceModel.class);
			}
			getMapperFacade().map(relatedPlaceRefOrValue, relatedPlaceModel, context);
			getSpiGenericService().saveItem(relatedPlaceModel);
			result.add(relatedPlaceModel);
		});
		return result;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}
}
