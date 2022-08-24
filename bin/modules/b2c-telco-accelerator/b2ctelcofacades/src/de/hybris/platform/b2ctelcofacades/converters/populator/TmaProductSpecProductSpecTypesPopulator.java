/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;


import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates product spec types information from {@link TmaProductSpecificationModel} to {@link TmaProductSpecificationData}.
 *
 * @since 2102
 */
public class TmaProductSpecProductSpecTypesPopulator
		implements Populator<TmaProductSpecificationModel, TmaProductSpecificationData>
{
	private Converter<TmaProductSpecTypeModel, TmaProductSpecTypeData> tmaProductSpecTypeConverter;

	public TmaProductSpecProductSpecTypesPopulator(
			final Converter<TmaProductSpecTypeModel, TmaProductSpecTypeData> tmaProductSpecTypeConverter)
	{
		this.tmaProductSpecTypeConverter = tmaProductSpecTypeConverter;
	}

	@Override
	public void populate(final TmaProductSpecificationModel source, final TmaProductSpecificationData target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		if (CollectionUtils.isNotEmpty(source.getProductSpecificationTypes()))
		{
			final Set<TmaProductSpecTypeData> productSpecTypeData = new HashSet<>();
			productSpecTypeData.addAll(getTmaProductSpecTypeConverter().convertAll(source.getProductSpecificationTypes()));
			target.setProductSpecType(productSpecTypeData);
		}
	}

	protected Converter<TmaProductSpecTypeModel, TmaProductSpecTypeData> getTmaProductSpecTypeConverter()
	{
		return tmaProductSpecTypeConverter;
	}
}
