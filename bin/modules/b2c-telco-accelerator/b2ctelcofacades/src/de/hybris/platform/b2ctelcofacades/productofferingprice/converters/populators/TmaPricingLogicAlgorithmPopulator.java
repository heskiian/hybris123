/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmData;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmSpecData;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmSpecModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populator implementation for {@link TmaPricingLogicAlgorithmModel} as source and {@link TmaPricingLogicAlgorithmData} as target type.
 *
 * @since 2102
 */
public class TmaPricingLogicAlgorithmPopulator<S extends TmaPricingLogicAlgorithmModel, T extends TmaPricingLogicAlgorithmData>
		implements Populator<S, T>
{
	private Converter<TmaPricingLogicAlgorithmSpecModel, TmaPricingLogicAlgorithmSpecData> pricingLogicAlgorithmSpecConverter;
	private Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter;

	public TmaPricingLogicAlgorithmPopulator(
			final Converter<TmaPricingLogicAlgorithmSpecModel, TmaPricingLogicAlgorithmSpecData> pricingLogicAlgorithmSpecConverter,
			final Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter)
	{
		this.pricingLogicAlgorithmSpecConverter = pricingLogicAlgorithmSpecConverter;
		this.externalIdentifierConverter = externalIdentifierConverter;
	}

	@Override
	public void populate(final TmaPricingLogicAlgorithmModel source, final TmaPricingLogicAlgorithmData target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setId(source.getId());
		target.setName(source.getName());
		target.setDescription(source.getDescription());
		target.setOnlineDate(source.getOnlineDate());
		target.setOfflineDate(source.getOfflineDate());

		if (source.getPricingLogicAlgorithmSpec() != null)
		{
			target.setPricingLogicAlgorithmSpec(
					getPricingLogicAlgorithmSpecConverter().convert(source.getPricingLogicAlgorithmSpec()));
		}

		if (CollectionUtils.isNotEmpty(source.getExternalIds()))
		{
			target.setExternalIds(getExternalIdentifierConverter().convertAll(source.getExternalIds()));
		}
	}

	protected Converter<TmaPricingLogicAlgorithmSpecModel, TmaPricingLogicAlgorithmSpecData> getPricingLogicAlgorithmSpecConverter()
	{
		return pricingLogicAlgorithmSpecConverter;
	}

	protected Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> getExternalIdentifierConverter()
	{
		return externalIdentifierConverter;
	}
}
