/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.OfferingGroup;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for offeringGroup attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 2007
 */
public class PoGroupAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	private MapperFacade mapperFacade;
	private TmaProductOfferFacade productOfferFacade;

	public PoGroupAttributeMapper(final MapperFacade mapperFacade, final TmaProductOfferFacade productOfferFacade)
	{
		this.mapperFacade = mapperFacade;
		this.productOfferFacade = productOfferFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (!getProductOfferFacade().isBpo(source) || CollectionUtils.isEmpty(source.getOfferingGroups()))
		{
			return;
		}

		final List<OfferingGroup> offeringGroups = new ArrayList<>();
		source.getOfferingGroups().forEach(offeringGroup ->
		{
			final OfferingGroup productOfferingGroup = getMapperFacade().map(offeringGroup, OfferingGroup.class, context);
			offeringGroups.add(productOfferingGroup);
		});

		target.setOfferingGroup(offeringGroups);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected TmaProductOfferFacade getProductOfferFacade()
	{
		return productOfferFacade;
	}
}
