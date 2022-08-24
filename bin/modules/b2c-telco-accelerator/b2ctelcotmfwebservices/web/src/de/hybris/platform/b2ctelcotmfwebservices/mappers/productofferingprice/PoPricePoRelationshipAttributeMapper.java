/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productofferingprice;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PoRelationshipRef;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingPrice;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.PriceData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for poRelationship attribute between {@link PriceData} and
 * {@link ProductOfferingPrice}. The list of po relationships is populated with affected product offering and required products
 * from source if any.
 *
 * @since 1903
 */
public class PoPricePoRelationshipAttributeMapper extends TmaAttributeMapper<PriceData, ProductOfferingPrice>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final ProductOfferingPrice target,
			final MappingContext context)
	{
		boolean isPriceOverride = false;
		final List<PoRelationshipRef> poRelationshipRef = new ArrayList<>();
		if (source.getAffectedProductOffering() != null)
		{
			updatePoRelationshipsWithAffectedProducts(source, context, poRelationshipRef);
			isPriceOverride = true;
		}

		if (CollectionUtils.isNotEmpty(source.getRequiredProductOfferings()))
		{
			updatePoRelationshipsWithRequiredProducts(source, context, poRelationshipRef);
			isPriceOverride = true;
		}

		target.setIsPriceOverride(isPriceOverride);
		target.setPoRelationship(poRelationshipRef);
	}

	private void updatePoRelationshipsWithAffectedProducts(PriceData source, MappingContext context,
			List<PoRelationshipRef> poRelationshipRef)
	{
		final PoRelationshipRef poAffectedProduct = getMapperFacade().map(source.getAffectedProductOffering(),
				PoRelationshipRef.class, context);
		poAffectedProduct.setPoAttributeType(PoRelationshipRef.PoAttributeTypeEnum.AFFECTED_PRODUCT);

		if (source.getProduct() != null)
		{
			poAffectedProduct.setBpoId(source.getProduct().getCode());
		}

		poRelationshipRef.add(poAffectedProduct);
	}

	private void updatePoRelationshipsWithRequiredProducts(PriceData source, MappingContext context,
			List<PoRelationshipRef> poRelationshipRef)
	{
		source.getRequiredProductOfferings().forEach(requiredProductData ->
		{
			final PoRelationshipRef poRequiredProduct = getMapperFacade().map(requiredProductData, PoRelationshipRef.class, context);
			poRequiredProduct.setPoAttributeType(PoRelationshipRef.PoAttributeTypeEnum.REQUIRED_PRODUCT);
			poRelationshipRef.add(poRequiredProduct);
		});
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
