/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaExternalIdentifierData;
import de.hybris.platform.b2ctelcofacades.data.TmaServiceSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaExternalIdentifierModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecificationModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.apache.commons.collections.CollectionUtils;


/**
 * Populates {@link TmaServiceSpecificationData} from a {@link TmaServiceSpecificationModel} entity.
 *
 * @since 2102
 */
public class TmaServiceSpecificationBasicPopulator implements Populator<TmaServiceSpecificationModel, TmaServiceSpecificationData>
{
	private Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter;

	public TmaServiceSpecificationBasicPopulator(
			final Converter<TmaExternalIdentifierModel, TmaExternalIdentifierData> externalIdentifierConverter)
	{
		this.externalIdentifierConverter = externalIdentifierConverter;
	}

	@Override
	public void populate(final TmaServiceSpecificationModel source, final TmaServiceSpecificationData target)
	{
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
