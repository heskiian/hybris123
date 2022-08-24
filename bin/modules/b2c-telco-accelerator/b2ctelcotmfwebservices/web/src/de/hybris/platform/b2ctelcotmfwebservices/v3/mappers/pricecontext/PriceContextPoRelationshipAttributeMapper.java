/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.pricecontext;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PoRelationshipRef;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.PriceContext;
import de.hybris.platform.commercefacades.product.data.PriceData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for poRelationship attribute between {@link PriceData} and {@link PriceContext}
 *
 * @since 2007
 */
public class PriceContextPoRelationshipAttributeMapper extends TmaAttributeMapper<PriceData, PriceContext>
{
	private MapperFacade mapperFacade;

	public PriceContextPoRelationshipAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final PriceData source, final PriceContext target, final MappingContext context)
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

	private void updatePoRelationshipsWithAffectedProducts(final PriceData source, final MappingContext context,
			final List<PoRelationshipRef> poRelationshipRef)
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

	private void updatePoRelationshipsWithRequiredProducts(final PriceData source,final MappingContext context,
			final List<PoRelationshipRef> poRelationshipRef)
	{
		source.getRequiredProductOfferings().forEach(requiredProductData ->
		{
			final PoRelationshipRef poRequiredProduct = getMapperFacade().map(requiredProductData, PoRelationshipRef.class, context);
			poRequiredProduct.setPoAttributeType(PoRelationshipRef.PoAttributeTypeEnum.REQUIRED_PRODUCT);
			poRelationshipRef.add(poRequiredProduct);
		});
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}
