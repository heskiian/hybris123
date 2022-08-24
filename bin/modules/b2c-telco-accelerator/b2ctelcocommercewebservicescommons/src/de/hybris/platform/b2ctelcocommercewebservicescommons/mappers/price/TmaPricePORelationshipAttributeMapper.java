/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.price;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.PoRelationshipWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.ProductOfferingPriceWsDTO;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * The TmaPricePORelationshipAttributeMapper class maps data for PO Relationship attribute between
 * {@link PriceData} and {@link ProductOfferingPriceWsDTO}
 *
 * @since 1907
 */
public class TmaPricePORelationshipAttributeMapper
		extends TmaAttributeMapper<PriceData, ProductOfferingPriceWsDTO>
{

	public static final String AFFECTED_PRODUCT = "AFFECTED_PRODUCT";

	public static final String REQUIRED_PRODUCT = "REQUIRED_PRODUCT";

	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPriceWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");

		final List<PoRelationshipWsDTO> poRelationshipRef = new ArrayList<>();
		if (source.getAffectedProductOffering() != null)
		{
			updatePoRelationshipsWithAffectedProducts(source, context, poRelationshipRef);
		}

		if (CollectionUtils.isNotEmpty(source.getRequiredProductOfferings()))
		{
			updatePoRelationshipsWithRequiredProducts(source, context, poRelationshipRef);
		}

		target.setPoRelationship(poRelationshipRef);
	}

	private void updatePoRelationshipsWithAffectedProducts(final PriceData source, final MappingContext context,
			final List<PoRelationshipWsDTO> poRelationshipRef)
	{
		final PoRelationshipWsDTO poAffectedProduct = getMapperFacade().map(source.getAffectedProductOffering(),
				PoRelationshipWsDTO.class, context);
		poAffectedProduct.setPoAttributeType(AFFECTED_PRODUCT);

		if (source.getProduct() != null)
		{
			poAffectedProduct.setBpoId(source.getProduct().getCode());
		}

		poRelationshipRef.add(poAffectedProduct);
	}

	private void updatePoRelationshipsWithRequiredProducts(final PriceData source, final MappingContext context,
			final List<PoRelationshipWsDTO> poRelationshipRef)
	{
		source.getRequiredProductOfferings().forEach(requiredProductData ->
		{
			final PoRelationshipWsDTO poRequiredProduct = getMapperFacade().map(requiredProductData, PoRelationshipWsDTO.class,
					context);
			poRequiredProduct.setPoAttributeType(REQUIRED_PRODUCT);
			poRelationshipRef.add(poRequiredProduct);
		});
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

}
