/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueUseData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.BillingTimeData;
import de.hybris.platform.subscriptionservices.model.BillingTimeModel;

import org.springframework.util.ObjectUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of {@link TmaProductOfferingPriceData} from {@link TmaProductOfferingPriceModel}.
 *
 * @since 2007
 */
public class TmaProductOfferingPricePopulator<SOURCE extends TmaProductOfferingPriceModel,
		TARGET extends TmaProductOfferingPriceData> implements Populator<SOURCE, TARGET>
{
	private Converter<BillingTimeModel, BillingTimeData> billingTimeConverter;
	private Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueUseData> productSpecCharacteristicValueUseConverter;


	public TmaProductOfferingPricePopulator(final Converter<BillingTimeModel, BillingTimeData> billingTimeConverter,
			final Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueUseData> productSpecCharacteristicValueUseConverter)
	{
		this.billingTimeConverter = billingTimeConverter;
		this.productSpecCharacteristicValueUseConverter = productSpecCharacteristicValueUseConverter;
	}

	@Override
	public void populate(SOURCE source, TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setId(source.getId());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
		if (!ObjectUtils.isEmpty(source.getPriceEvent()))
		{
			target.setPriceEvent(getBillingTimeConverter().convert(source.getPriceEvent()));
		}
		if (!ObjectUtils.isEmpty(source.getProductSpecCharacteristicValues()))
		{
			target.setProductSpecCharacteristicValueUses(
					getProductSpecCharacteristicValueUseConverter().convertAll(source.getProductSpecCharacteristicValues()));
		}
	}

	protected Converter<BillingTimeModel, BillingTimeData> getBillingTimeConverter()
	{
		return billingTimeConverter;
	}

	protected Converter<TmaProductSpecCharacteristicValueModel, TmaProductSpecCharacteristicValueUseData> getProductSpecCharacteristicValueUseConverter()
	{
		return productSpecCharacteristicValueUseConverter;
	}
}
