/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Attachment;
import de.hybris.platform.b2ctelcotmfwebservices.v3.dto.ProductOffering;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for attachment attribute between {@link ProductData} and
 * {@link ProductOffering}
 *
 * @since 2007
 */
public class PoMediaAttachmentAttributeMapper extends TmaAttributeMapper<ProductData, ProductOffering>
{
	private final MapperFacade mapperFacade;

	/**
	 * A constructor for {@link PoMediaAttachmentAttributeMapper}
	 *
	 * @param mapperFacade
	 */
	public PoMediaAttachmentAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductOffering target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getDataSheet()))
		{
			return;
		}

		final List<Attachment> attachments = CollectionUtils.isEmpty(target.getAttachment()) ? new ArrayList<>()
				: target.getAttachment();

		source.getDataSheet().forEach(document -> {
			final Attachment attachmentWsDto = getMapperFacade().map(document, Attachment.class, context);
			attachments.add(attachmentWsDto);
		});

		target.setAttachment(attachments);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

}