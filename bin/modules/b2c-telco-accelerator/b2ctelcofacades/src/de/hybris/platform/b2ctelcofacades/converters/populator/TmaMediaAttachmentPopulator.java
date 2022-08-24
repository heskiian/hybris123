/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * Populates dataSheet details of {@link ProductData} from a {@link ProductModel} entity.
 *
 * @since 2007
 */
public class TmaMediaAttachmentPopulator extends AbstractProductPopulator<ProductModel, ProductData>
{
	private Converter<MediaModel, MediaData> mediaConverter;

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		final TmaProductOfferingModel productOfferingModel = (TmaProductOfferingModel) source;
		final List<MediaData> documentDatas = CollectionUtils.isEmpty(target.getDataSheet()) ? new ArrayList<>()
				: target.getDataSheet();

		if (!ObjectUtils.isEmpty(productOfferingModel.getData_sheet()))
		{
			documentDatas.addAll(getMediaConverter().convertAll(productOfferingModel.getData_sheet()));
		}

		target.setDataSheet(documentDatas);
	}

	protected Converter<MediaModel, MediaData> getMediaConverter()
	{
		return mediaConverter;
	}

	public void setMediaConverter(final Converter<MediaModel, MediaData> mediaConverter)
	{
		this.mediaConverter = mediaConverter;
	}


}
