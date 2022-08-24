/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.price;

import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscribedproductservices.model.SpiComponentProdPriceModel;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Money;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.Price;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for taxIncludedAmount attribute between {@link SpiComponentProdPriceModel} and {@link Price}
 *
 * @since 2105
 */
public class PriceTaxIncludedAmountAttributeMapper extends SpiAttributeMapper<SpiComponentProdPriceModel, Price>
{

	private CommonI18NService commonI18NService;

	public PriceTaxIncludedAmountAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final CommonI18NService commonI18NService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.commonI18NService = commonI18NService;
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiComponentProdPriceModel source, final Price target,
			final MappingContext context)
	{
		if (source.getTaxIncludedAmount() == null || source.getCurrency() == null)
		{
			return;
		}

		final Money money = new Money();
		money.setUnit(source.getCurrency().getIsocode());
		money.setValue(Float.valueOf(source.getTaxIncludedAmount().toString()));

		target.setTaxIncludedAmount(money);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Price target, final SpiComponentProdPriceModel source,
			final MappingContext context)
	{
		if (target.getTaxIncludedAmount() == null)
		{
			return;
		}

		if (target.getTaxIncludedAmount().getUnit() != null)
		{
			source.setCurrency(getCommonI18NService().getCurrency(target.getTaxIncludedAmount().getUnit()));
		}

		if (target.getTaxIncludedAmount().getValue() != null)
		{
			source.setTaxIncludedAmount(Double.valueOf(target.getTaxIncludedAmount().getValue()));
		}
	}

	protected CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

}
