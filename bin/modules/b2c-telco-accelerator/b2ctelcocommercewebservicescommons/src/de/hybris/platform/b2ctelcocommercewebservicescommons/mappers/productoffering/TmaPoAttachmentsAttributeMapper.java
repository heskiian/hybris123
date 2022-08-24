/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.productoffering;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MediaWsDTO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for data_sheet attribute between {@link ProductData} and {@link ProductWsDTO}
 *
 * @since 2007
 */
public class TmaPoAttachmentsAttributeMapper extends TmaAttributeMapper<ProductData, ProductWsDTO>
{

	private final MapperFacade mapperFacade;

	/**
	 * A constructor for {@link TmaPoAttachmentsAttributeMapper}
	 *
	 * @param mapperFacade
	 */
	public TmaPoAttachmentsAttributeMapper(final MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final ProductData source, final ProductWsDTO target,
			final MappingContext context)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		if (source.getDataSheet() == null)
		{
			return;
		}
		final List<MediaWsDTO> attachments = new ArrayList<>();
		source.getDataSheet().forEach(document -> {
			final MediaWsDTO attachment = getMapperFacade().map(document, MediaWsDTO.class, context);
			attachments.add(attachment);
		});
		target.setAttachments(attachments);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

} 