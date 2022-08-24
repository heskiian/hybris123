/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.BundledProductOffering;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for bundledPo attribute between {@link ProductData} and {@link ProductOffering}
 *
 * @since 1903
 */
public class PoBundledPoAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{

	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (!TmaBundledProductOfferingModel._TYPECODE.equalsIgnoreCase(source.getItemType()) || source.getChildren() == null)
		{
			return;
		}

		final List<BundledProductOffering> bundledProductOfferings = new ArrayList<>();
		source.getChildren().forEach(productData ->
		{
			final BundledProductOffering bundledProductOfferingWsDto = getMapperFacade()
					.map(productData, BundledProductOffering.class, context);
			bundledProductOfferings.add(bundledProductOfferingWsDto);

		});

		target.setBundledProductOffering(bundledProductOfferings);
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
