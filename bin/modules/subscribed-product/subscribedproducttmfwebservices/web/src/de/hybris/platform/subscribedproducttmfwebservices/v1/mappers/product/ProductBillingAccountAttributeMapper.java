/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.product;

import de.hybris.platform.subscribedproductservices.model.SpiBillingAccountModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.BillingAccountRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Product;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for billingAccount attribute between {@link SpiProductModel} and {@link Product}
 *
 * @since 2105
 */
public class ProductBillingAccountAttributeMapper extends SpiAttributeMapper<SpiProductModel, Product>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductBillingAccountAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductModel source, final Product target,
			final MappingContext context)
	{
		if (source.getBillingAccount() == null)
		{
			return;
		}
		target.setBillingAccount(getMapperFacade().map(source.getBillingAccount(), BillingAccountRef.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final Product target, final SpiProductModel source, final MappingContext context)
	{
		if (target.getBillingAccount() == null)
		{
			return;
		}

		SpiBillingAccountModel spiBillingAccountModel = (SpiBillingAccountModel) getSpiGenericService()
				.getItem(SpiBillingAccountModel._TYPECODE, target.getBillingAccount().getId());
		if (spiBillingAccountModel == null)
		{
			spiBillingAccountModel = (SpiBillingAccountModel) getSpiGenericService().createItem(SpiBillingAccountModel.class);
			spiBillingAccountModel.setId(target.getBillingAccount().getId());
			getSpiGenericService().saveItem(spiBillingAccountModel);
		}

		getMapperFacade().map(target.getBillingAccount(), spiBillingAccountModel, context);
		getSpiGenericService().saveItem(spiBillingAccountModel);
		source.setBillingAccount(spiBillingAccountModel);
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
