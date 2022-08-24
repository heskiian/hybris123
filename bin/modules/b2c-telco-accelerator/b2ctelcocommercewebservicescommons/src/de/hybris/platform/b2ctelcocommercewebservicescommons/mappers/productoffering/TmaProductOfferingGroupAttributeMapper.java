/*
 *
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.OfferingGroupWsDTO;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcofacades.product.TmaProductOfferFacade;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * TmaProductOfferingGroupAttributeMapper populates value of offeringGroups attribute from {@link ProductData} to
 * {@link ProductWsDTO}
 *
 * @since 1907
 */
public class TmaProductOfferingGroupAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{

	private MapperFacade mapperFacade;
	private TmaProductOfferFacade productOfferFacade;

	public TmaProductOfferingGroupAttributeMapper(final TmaProductOfferFacade productOfferFacade)
	{
		this.productOfferFacade = productOfferFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		if (!getProductOfferFacade().isBpo(source) || CollectionUtils.isEmpty(source.getOfferingGroups()))
		{
			return;
		}

		final List<OfferingGroupWsDTO> offeringGroups = new ArrayList<>();
		source.getOfferingGroups().forEach(offeringGroup ->
		{
			final OfferingGroupWsDTO productOfferingGroup = getMapperFacade().map(offeringGroup, OfferingGroupWsDTO.class, context);
			offeringGroups.add(productOfferingGroup);
		});

		target.setOfferingGroup(offeringGroups);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	protected TmaProductOfferFacade getProductOfferFacade()
	{
		return productOfferFacade;
	}
}
