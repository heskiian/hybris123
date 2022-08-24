/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.product.converters.populator;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;


/**
 * Populates the attribute {@link ProductData#itemType} depending on the type of the given SOURCE parameter.
 *
 * @param <SOURCE>
 *           source to retrieve the data from
 * @param <TARGET>
 *           target to be populated with information
 */
public class TelcoProductTypePopulator<SOURCE extends ProductModel, TARGET extends ProductData> implements
		Populator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		target.setItemType(source.getItemtype());
	}
}
