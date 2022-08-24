/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.productofferingprice.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcofacades.data.TmaPricingLogicAlgorithmSpecData;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPricingLogicAlgorithmSpecModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populator implementation for {@link TmaPricingLogicAlgorithmSpecModel} as source and {@link TmaPricingLogicAlgorithmSpecData} as target type.
 *
 * @since 2102
 */
public class TmaPricingLogicAlgorithmSpecPopulator<S extends TmaPricingLogicAlgorithmSpecModel, T extends TmaPricingLogicAlgorithmSpecData>
		implements Populator<S, T>
{

	private Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter;

	public TmaPricingLogicAlgorithmSpecPopulator(
			final Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter)
	{
		this.externalIdentifierConverter = externalIdentifierConverter;
	}

	@Override
	public void populate(final TmaPricingLogicAlgorithmSpecModel source, final TmaPricingLogicAlgorithmSpecData target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setId(source.getId());
		target.setName(source.getName());

		if (source.getApprovalStatus() != null)
		{
			target.setApprovalStatus(source.getApprovalStatus().getCode());
		}

		target.setDescription(source.getDescription());
		target.setOnlineDate(source.getOnlineDate());
		target.setOfflineDate(source.getOfflineDate());

		if (CollectionUtils.isNotEmpty(source.getExternalIds()))
		{
			target.setExternalIds(getExternalIdentifierConverter().convertAll(source.getExternalIds()));
		}
	}

	protected Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> getExternalIdentifierConverter()
	{
		return externalIdentifierConverter;
	}
}
