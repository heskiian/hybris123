/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.pricecontext;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.RelatedPartyRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.PriceContext;
import de.hybris.platform.commercefacades.product.data.PriceData;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for related party attribute between {@link PriceData} and {@link PriceContext}
 *
 * @since 2007
 */
public class PriceContextRelatedPartyAttributeMapper extends TmaAttributeMapper<PriceData, PriceContext>
{
	private MapperFacade mapperFacade;

	public PriceContextRelatedPartyAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final PriceContext target, final MappingContext context)
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
