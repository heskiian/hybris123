/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.productprice;

import de.hybris.platform.subscribedproductservices.model.SpiBillingAccountModel;
import de.hybris.platform.subscribedproductservices.model.SpiProductPriceModel;
import de.hybris.platform.subscribedproductservices.services.SpiGenericService;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.BillingAccountRef;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.ProductPrice;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for billingAccount attribute between {@link SpiProductPriceModel} and {@link ProductPrice}
 *
 * @since 2105
 */
public class ProductPriceBillingAccountAttributeMapper extends SpiAttributeMapper<SpiProductPriceModel, ProductPrice>
{
	private MapperFacade mapperFacade;
	private SpiGenericService spiGenericService;

	public ProductPriceBillingAccountAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final SpiGenericService spiGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.spiGenericService = spiGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiProductPriceModel source, final ProductPrice target,
			MappingContext context)
	{
		if (source.getBillingAccount() == null)
		{
			return;
		}
		target.setBillingAccount(getMapperFacade().map(source.getBillingAccount(), BillingAccountRef.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final ProductPrice target, final SpiProductPriceModel source,
			final MappingContext context)
	{
		if (target.getBillingAccount() == null)
		{
			return;
		}

		SpiBillingAccountModel billingAccountModel = (SpiBillingAccountModel) getSpiGenericService()
				.getItem(SpiBillingAccountModel._TYPECODE, target.getBillingAccount().getId());
		if (billingAccountModel == null)
		{
			billingAccountModel = (SpiBillingAccountModel) getSpiGenericService().createItem(SpiBillingAccountModel.class);
			billingAccountModel.setId(target.getBillingAccount().getId());
		}

		getMapperFacade().map(target.getBillingAccount(), billingAccountModel, context);
		getSpiGenericService().saveItem(billingAccountModel);
		source.setBillingAccount(billingAccountModel);
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
