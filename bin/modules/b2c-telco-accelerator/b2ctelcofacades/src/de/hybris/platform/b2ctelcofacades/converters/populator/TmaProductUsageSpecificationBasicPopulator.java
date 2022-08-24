/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductUsageSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates {@link TmaProductUsageSpecificationData} with basic attributes from {@link TmaProductUsageSpecificationModel}.
 *
 * @since 2102
 */
public class TmaProductUsageSpecificationBasicPopulator
		implements Populator<TmaProductUsageSpecificationModel, TmaProductUsageSpecificationData>
{
	private Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter;

	public TmaProductUsageSpecificationBasicPopulator(
			final Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter)
	{
		this.externalIdentifierConverter = externalIdentifierConverter;
	}

	@Override
	public void populate(final TmaProductUsageSpecificationModel source, final TmaProductUsageSpecificationData target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setId(source.getId());
		target.setName(source.getName());
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
