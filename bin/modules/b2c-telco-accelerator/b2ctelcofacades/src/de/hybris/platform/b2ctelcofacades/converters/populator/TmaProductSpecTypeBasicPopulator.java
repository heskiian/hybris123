/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecTypeData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.converters.Populator;


/**
 * Populates {@link TmaProductSpecTypeData} from a {@link TmaProductSpecTypeModel} entity.
 *
 * @since 2102
 */
public class TmaProductSpecTypeBasicPopulator implements Populator<TmaProductSpecTypeModel, TmaProductSpecTypeData>
{
	@Override
	public void populate(final TmaProductSpecTypeModel source, final TmaProductSpecTypeData target)
	{
		target.setId(source.getCode());
		target.setDescription(source.getDescription());
	}
}
