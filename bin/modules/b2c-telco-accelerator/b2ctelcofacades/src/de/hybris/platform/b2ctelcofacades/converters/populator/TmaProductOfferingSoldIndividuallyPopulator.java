/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the {@link ProductData} with the {@link TmaProductOfferingModel#SOLDINDIVIDUALLY} flag.
 *
 * @since 1903
 */
public class TmaProductOfferingSoldIndividuallyPopulator extends AbstractProductPopulator<TmaProductOfferingModel, ProductData>
{
	@Override
	public void populate(final TmaProductOfferingModel source, final ProductData target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);
		target.setSoldIndividually(source.getSoldIndividually());
	}
}
