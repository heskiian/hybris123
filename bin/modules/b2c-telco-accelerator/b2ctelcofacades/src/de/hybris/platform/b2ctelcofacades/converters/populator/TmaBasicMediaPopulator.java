/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.cmsfacades.media.populator.BasicMediaPopulator;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;


/**
 * Populates {@link MediaData} from a {@link MediaModel} entity.
 *
 * @since 2007
 */
public class TmaBasicMediaPopulator extends BasicMediaPopulator implements Populator<MediaModel, MediaData>
{
	@Override
	public void populate(final MediaModel source, final MediaData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		super.populate(source, target);
		target.setRealFileName(source.getRealFileName());
		if (!ObjectUtils.isEmpty(source.getFolder()) && StringUtils.isNotBlank(source.getFolder().getQualifier()))
		{
			target.setFolder(source.getFolder().getQualifier().toLowerCase(Locale.ROOT));
		}
	}
}
