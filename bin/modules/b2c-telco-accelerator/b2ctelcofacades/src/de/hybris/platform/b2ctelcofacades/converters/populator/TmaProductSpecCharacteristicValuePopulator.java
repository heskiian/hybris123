/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.converters.Populator;


/**
 * Populates {@link TmaProductSpecCharacteristicValueData} from a {@link TmaProductSpecCharacteristicValueModel} entity.
 *
 * @since 6.7
 */
public class TmaProductSpecCharacteristicValuePopulator
		implements Populator<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueData>
{
	@Override
	public void populate(final TmaProductSpecCharacteristicValueModel source, final TmaProductSpecCharacteristicValueData target)
	{
		target.setId(source.getId());
		target.setValue(source.getValue());
		target.setDescription(source.getDescription());
		target.setUnitOfMeasurment(source.getUnitOfMeasure() != null ? source.getUnitOfMeasure().getId() : null);
		target.setValueType(source.getValueType() != null ? source.getValueType().getCode() : null);
	}
}
