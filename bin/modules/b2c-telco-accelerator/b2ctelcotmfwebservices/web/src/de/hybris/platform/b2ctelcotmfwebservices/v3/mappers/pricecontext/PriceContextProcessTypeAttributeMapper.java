/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.pricecontext;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessType;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.PriceContext;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for processType attribute between {@link PriceData} and {@link PriceContext}
 *
 * @since 2007
 */
public class PriceContextProcessTypeAttributeMapper extends TmaAttributeMapper<PriceData, PriceContext>
{
	private MapperFacade mapperFacade;

	public PriceContextProcessTypeAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final PriceContext target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProcessTypes()))
		{
			return;
		}

		final List<ProcessType> processTypes = source.getProcessTypes().stream()
				.map(processType -> getMapperFacade().map(processType, ProcessType.class, context)).collect(Collectors.toList());

		target.setProcessType(processTypes);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
