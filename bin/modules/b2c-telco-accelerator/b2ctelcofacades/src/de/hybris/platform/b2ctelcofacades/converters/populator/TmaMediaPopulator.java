/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * Populates {@link ProductData} from a {@link ProductModel} entity.
 *
 * @since 1810
 */
public class TmaMediaPopulator extends AbstractProductPopulator<ProductModel, ProductData>
{
	private Converter<MediaModel, ImageData> imageConverter;

	@Override
	public void populate(final ProductModel source, final ProductData target)
	{
		Assert.notNull(source, "Parameter source cannot be null.");
		Assert.notNull(target, "Parameter target cannot be null.");
		final TmaProductOfferingModel productOfferingModel = (TmaProductOfferingModel) source;
		final List<ImageData> imageData = CollectionUtils.isEmpty(target.getImages()) ? new ArrayList<>()
				: (List<ImageData>) target.getImages();
		if (!ObjectUtils.isEmpty(productOfferingModel.getGalleryImages()))
		{
			imageData.addAll(getGallaryImages(productOfferingModel));
		}
		target.setImages(imageData);
	}

	private List<ImageData> getGallaryImages(final TmaProductOfferingModel poModel)
	{
		final List<ImageData> imageDataList = new ArrayList<>();
		for (final MediaModel mediaModel : getMediaModels(poModel))
		{
			final ImageData imageData = new ImageData();
			imageData.setCode(mediaModel.getCode());
			imageData.setDescription(mediaModel.getDescription());
			imageData.setMime(mediaModel.getMime());
			imageData.setImageType(ImageDataType.GALLERY);
			imageDataList.add(getImageConverter().convert(mediaModel, imageData));
		}
		return imageDataList;
	}

	private Collection<MediaModel> getMediaModels(final TmaProductOfferingModel poModel)
	{
		final Collection<MediaModel> medias = new ArrayList<>();
		final List<MediaContainerModel> galleryImages = poModel.getGalleryImages();
		if (galleryImages != null)
		{
			galleryImages.forEach(mediaContainer -> medias.addAll(mediaContainer.getMedias()));
		}
		return medias;
	}

	protected Converter<MediaModel, ImageData> getImageConverter()
	{
		return imageConverter;
	}

	@Required
	public void setImageConverter(final Converter<MediaModel, ImageData> imageConverter)
	{
		this.imageConverter = imageConverter;
	}
}
