/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaServiceSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecificationModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates service specifications information from {@link TmaProductSpecificationModel} to {@link TmaProductSpecificationData}.
 *
 * @since 2102
 */
public class TmaProductSpecServiceSpecsPopulator implements Populator<TmaProductSpecificationModel, TmaProductSpecificationData>
{
	private Converter<TmaServiceSpecificationModel, TmaServiceSpecificationData> tmaServiceSpecificationConverter;

	public TmaProductSpecServiceSpecsPopulator(
			final Converter<TmaServiceSpecificationModel, TmaServiceSpecificationData> tmaServiceSpecificationConverter)
	{
		this.tmaServiceSpecificationConverter = tmaServiceSpecificationConverter;
	}

	@Override
	public void populate(final TmaProductSpecificationModel source, final TmaProductSpecificationData target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		if (CollectionUtils.isNotEmpty(source.getCfsSpecs()))
		{
			final Set<TmaServiceSpecificationData> serviceSpecificationData = new HashSet<>();
			serviceSpecificationData.addAll(getTmaServiceSpecificationConverter().convertAll(source.getCfsSpecs()));
			target.setServiceSpecification(serviceSpecificationData);
		}
	}

	protected Converter<TmaServiceSpecificationModel, TmaServiceSpecificationData> getTmaServiceSpecificationConverter()
	{
		return tmaServiceSpecificationConverter;
	}
}
