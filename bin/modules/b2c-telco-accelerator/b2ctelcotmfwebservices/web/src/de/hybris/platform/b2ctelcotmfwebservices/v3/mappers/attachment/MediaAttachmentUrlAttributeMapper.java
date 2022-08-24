/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v3.mappers.attachment;

import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Attachment;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.util.Config;

import org.apache.commons.lang.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for url attribute between {@link MediaData} and {@link Attachment}
 *
 * @since 2007
 */
public class MediaAttachmentUrlAttributeMapper extends TmaAttributeMapper<MediaData, Attachment>
{

	@Override
	public void populateTargetAttributeFromSource(final MediaData source, final Attachment target, final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getUrl()))
		{
			target.setUrl(Config.getParameter(B2ctelcotmfwebservicesConstants.API_BASE_URL).concat(source.getUrl()));
		}
	}

}
