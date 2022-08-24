/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductUsageSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.springframework.util.ObjectUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates usage specification information from {@link TmaProductSpecificationModel} to {@link TmaProductSpecificationData}.
 *
 * @since 2102
 */
public class TmaProductSpecUsageSpecPopulator implements Populator<TmaProductSpecificationModel, TmaProductSpecificationData>
{
	private Converter<TmaProductUsageSpecificationModel, TmaProductUsageSpecificationData> tmaProductUsageSpecificationConverter;

	public TmaProductSpecUsageSpecPopulator(
			final Converter<TmaProductUsageSpecificationModel, TmaProductUsageSpecificationData> tmaProductUsageSpecificationConverter)
	{
		this.tmaProductUsageSpecificationConverter = tmaProductUsageSpecificationConverter;
	}

	@Override
	public void populate(final TmaProductSpecificationModel source, final TmaProductSpecificationData target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		if (!ObjectUtils.isEmpty(source.getProductUsageSpecification()))
		{
			target.setUsageSpecification(getTmaProductUsageSpecificationConverter().convert(source.getProductUsageSpecification()));
		}
	}

	protected Converter<TmaProductUsageSpecificationModel, TmaProductUsageSpecificationData> getTmaProductUsageSpecificationConverter()
	{
		return tmaProductUsageSpecificationConverter;
	}
}
