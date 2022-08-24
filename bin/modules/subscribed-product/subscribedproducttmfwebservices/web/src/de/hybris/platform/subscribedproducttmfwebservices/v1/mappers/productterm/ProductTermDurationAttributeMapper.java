/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productterm;

import de.hybris.platform.subscribedproductservices.model.SpiProductTermModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductTerm;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Quantity;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.springframework.util.StringUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for duration attribute between {@link SpiProductTermModel} and
 * {@link ProductTerm}
 *
 * @since 2105
 */
public class ProductTermDurationAttributeMapper extends SpiAttributeMapper<SpiProductTermModel, ProductTerm>
{
	private MapperFacade mapperFacade;

	public ProductTermDurationAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductTermModel source, final ProductTerm target,
			final MappingContext context)
	{
		if (source.getAmount() == null || StringUtils.isEmpty(source.getUnits()))
		{
			return;
		}

		final Quantity quantity = new Quantity();
		quantity.setAmount(source.getAmount().floatValue());
		quantity.setUnits(source.getUnits());
		target.setDuration(quantity);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
