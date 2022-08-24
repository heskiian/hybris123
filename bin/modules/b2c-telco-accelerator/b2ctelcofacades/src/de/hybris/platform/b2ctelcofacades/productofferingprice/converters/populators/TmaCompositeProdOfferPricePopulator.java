/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProdOfferPriceData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductOfferingPriceData;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProdOfferPriceModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingPriceModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.ObjectUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of {@link TmaCompositeProdOfferPriceData} from {@link TmaCompositeProdOfferPriceModel}.
 *
 * @since 2007
 */
public class TmaCompositeProdOfferPricePopulator<SOURCE extends TmaCompositeProdOfferPriceModel,
		TARGET extends TmaCompositeProdOfferPriceData> implements Populator<SOURCE, TARGET>
{
	private Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> productOfferingPriceConverterMap;

	public TmaCompositeProdOfferPricePopulator(
			final Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> productOfferingPriceConverterMap)
	{
		this.productOfferingPriceConverterMap = productOfferingPriceConverterMap;
	}

	@Override
	public void populate(SOURCE source, TARGET target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		final Set<TmaProductOfferingPriceModel> childrenPops = source.getChildren();
		if (CollectionUtils.isEmpty(childrenPops))
		{
			return;
		}

		final Set<TmaProductOfferingPriceData> childrenPopsData = new HashSet<>();

		childrenPops.forEach(childPop ->
		{
			final Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData> productOfferingPriceConverter =
					getProductOfferingPriceConverterMap().get(childPop.getItemtype());
			if (!ObjectUtils.isEmpty(productOfferingPriceConverter))
			{
				childrenPopsData.add(productOfferingPriceConverter.convert(childPop));
			}
		});

		target.setChildren(childrenPopsData);
	}

	protected Map<String, Converter<TmaProductOfferingPriceModel, TmaProductOfferingPriceData>> getProductOfferingPriceConverterMap()
	{
		return productOfferingPriceConverterMap;
	}

}
