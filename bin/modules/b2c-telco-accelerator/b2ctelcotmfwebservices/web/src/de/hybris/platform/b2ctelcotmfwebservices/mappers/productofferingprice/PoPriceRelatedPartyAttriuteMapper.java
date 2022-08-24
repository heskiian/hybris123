/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.commercefacades.product.data.PriceData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for related party attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}
 *
 * @since 1911
 */
public class PoPriceRelatedPartyAttriuteMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;

	public PoPriceRelatedPartyAttriuteMapper(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		if (source.getUser() != null)
		{
			target.setRelatedParty(getMapperFacade().map(source.getUser(), RelatedPartyRef.class, context));
		}
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
