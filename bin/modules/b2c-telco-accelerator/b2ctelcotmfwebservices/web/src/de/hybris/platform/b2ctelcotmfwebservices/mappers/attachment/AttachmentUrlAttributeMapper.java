/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.mappers.attachment;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Attachment;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.util.Config;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang.StringUtils;


/**
 * This attribute Mapper class maps data for url attribute between {@link ImageData} and {@link Attachment}
 *
 * @since 1903
 */
public class AttachmentUrlAttributeMapper extends TmaAttributeMapper<ImageData, Attachment>
{

	@Override
	public void populateTargetAttributeFromSource(ImageData source, Attachment target, MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getUrl()))
		{
			target.setUrl(Config.getParameter(B2ctelcotmfwebservicesConstants.API_BASE_URL).concat(source.getUrl()));
		}
	}
}
