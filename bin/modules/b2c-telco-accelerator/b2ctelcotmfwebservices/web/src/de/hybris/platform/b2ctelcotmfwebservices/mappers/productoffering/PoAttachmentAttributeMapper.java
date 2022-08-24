/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Attachment;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attachment attribute between {@link ProductData} and
 * {@link ProductOffering}
 *
 * @since 1903
 */
public class PoAttachmentAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (source.getImages() == null)
		{
			return;
		}

		final List<Attachment> attachments = CollectionUtils.isEmpty(target.getAttachment()) ? new ArrayList<>()
				: target.getAttachment();
		source.getImages().forEach(imageData -> {
			final Attachment attachmentWsDto = getMapperFacade().map(imageData, Attachment.class, context);
			attachments.add(attachmentWsDto);
		});

		target.setAttachment(attachments);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
