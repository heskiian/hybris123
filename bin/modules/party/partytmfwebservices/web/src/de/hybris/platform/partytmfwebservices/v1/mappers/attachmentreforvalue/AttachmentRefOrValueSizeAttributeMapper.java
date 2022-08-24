/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.partytmfwebservices.v1.mappers.attachmentreforvalue;

import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.partytmfwebservices.v1.dto.AttachmentRefOrValue;
import de.hybris.platform.partytmfwebservices.v1.dto.Quantity;
import de.hybris.platform.partytmfwebservices.v1.mappers.PmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for size attribute between {@link CatalogUnawareMediaModel} and {@link AttachmentRefOrValue}
 *
 * @since 2108
 */
public class AttachmentRefOrValueSizeAttributeMapper extends PmAttributeMapper<CatalogUnawareMediaModel, AttachmentRefOrValue>
{
	public AttachmentRefOrValueSizeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final CatalogUnawareMediaModel source, final AttachmentRefOrValue target,
			final MappingContext context)
	{
		if (source.getSize() == null)
		{
			return;
		}

		final Quantity quantity = new Quantity();
		quantity.setAmount(Float.valueOf(source.getSize()));

		target.setSize(quantity);
	}
}
