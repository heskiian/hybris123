/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessType;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for processType attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1903
 */
public class PoPriceProcessTypeAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	private static final String ALL_PROCESS_TYPES = " ";
	private MapperFacade mapperFacade;

	public PoPriceProcessTypeAttributeMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProcessTypes()))
		{
			return;
		}

		final List<ProcessType> processTypes = source.getProcessTypes().stream()
				.filter(processType -> !processType.getCode().equals(ALL_PROCESS_TYPES))
				.map(processType -> getMapperFacade().map(processType, ProcessType.class, context)).collect(Collectors.toList());

		target.setProcessType(processTypes);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
