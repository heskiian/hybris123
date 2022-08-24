/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.pricealteration;

import de.hybris.platform.subscribedproductservices.model.SpiPriceAlterationModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.PriceAlteration;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atBaseType attribute between {@link SpiPriceAlterationModel} and
 * {@link PriceAlteration}
 *
 * @since 2105
 */
public class PriceAlterationBaseTypeAttributeMapper extends SpiAttributeMapper<SpiPriceAlterationModel, PriceAlteration>
{
	public PriceAlterationBaseTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiPriceAlterationModel source,
			final PriceAlteration target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAttype(SubscribedproducttmfwebservicesConstants.SPI_PRICE_ALTERATION_BASE_TYPE);
		}
	}
}
