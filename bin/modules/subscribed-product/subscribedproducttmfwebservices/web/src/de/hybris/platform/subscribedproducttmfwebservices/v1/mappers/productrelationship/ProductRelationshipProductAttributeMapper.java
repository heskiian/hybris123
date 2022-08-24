/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productrelationship;

import de.hybris.platform.subscribedproductservices.model.SpiProductBundleModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductComponentModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductRelationshipModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductRefOrValue;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductRelationship;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for product attribute between {@link SpiProductRelationshipModel} and
 * {@link ProductRelationship}
 *
 * @since 2105
 */
public class ProductRelationshipProductAttributeMapper
		extends SpiAttributeMapper<SpiProductRelationshipModel, ProductRelationship>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductRelationshipProductAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductRelationshipModel source, final ProductRelationship target,
			final MappingContext context)
	{
		if (source.getTarget() == null)
		{
			return;
		}

		final ProductRefOrValue productRefOrValue = new ProductRefOrValue();
		productRefOrValue.setId(source.getTarget().getId());
		productRefOrValue.setName(source.getTarget().getName());
		productRefOrValue.setHref(SubscribedproducttmfwebservicesConstants.PRODUCT_API_URL + source.getTarget().getId());
		target.setProduct(productRefOrValue);
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductRelationship target, final SpiProductRelationshipModel source,
			final MappingContext context)
	{
		SpiProductModel spiProductModel;
		if (getSpiGenericService().getItem(SpiProductModel._TYPECODE, target.getProduct().getId()) != null)
		{
			spiProductModel = (SpiProductModel) getSpiGenericService()
					.getItem(SpiProductModel._TYPECODE, target.getProduct().getId());
			source.setTarget(spiProductModel);
			return;
		}

		if (Boolean.TRUE.equals(target.getProduct().isIsBundle()))
		{
			spiProductModel = (SpiProductBundleModel) getSpiGenericService()
					.createItem(SpiProductBundleModel.class);
		}
		else
		{
			spiProductModel = (SpiProductComponentModel) getSpiGenericService()
					.createItem(SpiProductComponentModel.class);
		}
		getMapperFacade().map(target.getProduct(), spiProductModel, context);
		source.setTarget(spiProductModel);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected SpiGenericService getSpiGenericService()
	{
		return spiGenericService;
	}
}
