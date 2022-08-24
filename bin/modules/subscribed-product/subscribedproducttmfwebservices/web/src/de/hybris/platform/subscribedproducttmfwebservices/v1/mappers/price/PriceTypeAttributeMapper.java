/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.price;

import de.hybris.platform.subscribedproductservices.model.SpiComponentProdPriceModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Price;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atType attribute between {@link SpiComponentProdPriceModel} and {@link Price}
 *
 * @since 2105
 */
public class PriceTypeAttributeMapper extends SpiAttributeMapper<SpiComponentProdPriceModel, Price>
{
	public PriceTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiComponentProdPriceModel source, final Price target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(SubscribedproducttmfwebservicesConstants.SPI_PRICE_TYPE);
		}
	}
}
