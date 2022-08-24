/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productterm;

import de.hybris.platform.subscribedproductservices.model.SpiProductTermModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductTerm;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.TimePeriod;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for validFor attribute between {@link SpiProductTermModel} and
 * {@link ProductTerm}
 *
 * @since 2105
 */
public class ProductTermValidForAttributeMapper extends SpiAttributeMapper<SpiProductTermModel, ProductTerm>
{
	private MapperFacade mapperFacade;

	public ProductTermValidForAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductTermModel source, final ProductTerm target,
			final MappingContext context)
	{
		if (source.getStartDate() == null || source.getEndDate() == null)
		{
			return;
		}
		final TimePeriod timePeriod = new TimePeriod();
		timePeriod.setStartDateTime(source.getStartDate());
		timePeriod.setEndDateTime(source.getEndDate());
		target.setValidFor(timePeriod);
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductTerm target, final SpiProductTermModel source,
			final MappingContext context)
	{
		if (target.getValidFor() != null && target.getValidFor().getStartDateTime() != null
				&& target.getValidFor().getEndDateTime() != null)
		{
			source.setStartDate(target.getValidFor().getStartDateTime());
			source.setEndDate(target.getValidFor().getEndDateTime());
		}
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
