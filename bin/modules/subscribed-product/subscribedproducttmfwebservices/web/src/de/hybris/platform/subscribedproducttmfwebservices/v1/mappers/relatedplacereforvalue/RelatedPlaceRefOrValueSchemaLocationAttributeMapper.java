/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.relatedplacereforvalue;

import de.hybris.platform.subscribedproductservices.model.SpiRelatedPlaceModel;
import de.hybris.platform.subscribedproducttmfwebservices.constants.SubscribedproducttmfwebservicesConstants;
import de.hybris.platform.subscribedproducttmfwebservices.v1.dto.RelatedPlaceRefOrValue;
import de.hybris.platform.subscribedproducttmfwebservices.v1.mappers.SpiAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atSchemaLocation attribute between {@link SpiRelatedPlaceModel} and
 * {@link RelatedPlaceRefOrValue}
 *
 * @since 2105
 */
public class RelatedPlaceRefOrValueSchemaLocationAttributeMapper
		extends SpiAttributeMapper<SpiRelatedPlaceModel, RelatedPlaceRefOrValue>
{
	public RelatedPlaceRefOrValueSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final SpiRelatedPlaceModel source, final RelatedPlaceRefOrValue target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(SubscribedproducttmfwebservicesConstants.SPI_API_SCHEMA);
		}
	}
}
