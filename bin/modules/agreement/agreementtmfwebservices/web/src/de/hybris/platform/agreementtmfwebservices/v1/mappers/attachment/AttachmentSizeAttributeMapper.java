/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.attachment;

import de.hybris.platform.agreementtmfwebservices.v1.dto.AttachmentRefOrValue;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Quantity;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for size attribute between {@link CatalogUnawareMediaModel} and
 * {@link AttachmentRefOrValue}
 *
 * @since 2108
 */
public class AttachmentSizeAttributeMapper extends AgrAttributeMapper<CatalogUnawareMediaModel, AttachmentRefOrValue>
{
	public AttachmentSizeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
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
		quantity.setAmount(source.getSize().floatValue());
		target.setSize(quantity);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AttachmentRefOrValue target, final CatalogUnawareMediaModel source,
			final MappingContext context)
	{
		if (target.getSize() == null || target.getSize().getAmount() == null)
		{
			return;
		}

		source.setSize(target.getSize().getAmount().longValue());
	}

}
