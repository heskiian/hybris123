/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productspeccharacteristicvalue;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductSpecCharacteristicValueWsDTO;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;

import org.springframework.util.Assert;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for unitOfMeasure attribute between {@link TmaProductSpecCharacteristicValueData} and
 * {@link ProductSpecCharacteristicValueWsDTO}
 *
 * @since 2011
 */
public class TmaPscvUnitOfMeasureAttributeMapper
		extends TmaAttributeMapper<TmaProductSpecCharacteristicValueData, ProductSpecCharacteristicValueWsDTO>
{
	@Override
	public void populateTargetAttributeFromSource(final TmaProductSpecCharacteristicValueData source,
			final ProductSpecCharacteristicValueWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (source.getUnitOfMeasurment() != null)
		{
			target.setUnitOfMeasure(source.getUnitOfMeasurment());
		}
	}
}