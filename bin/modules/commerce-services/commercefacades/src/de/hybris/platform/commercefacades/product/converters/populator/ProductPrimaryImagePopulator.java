/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commercefacades.product.converters.populator;

import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ImageDataType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * Populate the product data with the product's primary image
 */
public class ProductPrimaryImagePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductImagePopulator<SOURCE, TARGET>
{
	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		final MediaContainerModel primaryImageMediaContainer = getPrimaryImageMediaContainer(productModel);
		if (primaryImageMediaContainer != null)
		{
			final List<ImageData> imageList = new ArrayList<>();

			// Use the first container as the primary image
			addImagesInFormats(primaryImageMediaContainer, ImageDataType.PRIMARY, 0, imageList);

			for (final ImageData imageData : imageList)
			{
				if (imageData.getAltText() == null)
				{
					imageData.setAltText(productModel.getName());
				}
			}

			// fill our image list with the product's existing non-primary images
			if (productData.getImages() != null)
			{
				final List<ImageData> notPrimaryImageList = productData.getImages().stream() //
						.filter(img -> img.getImageType() != ImageDataType.PRIMARY) //
						.collect(toList());

				imageList.addAll(notPrimaryImageList);
			}

			productData.setImages(imageList);
		}
	}

	protected MediaContainerModel getPrimaryImageMediaContainer(final SOURCE productModel)
	{
		final MediaModel picture = (MediaModel) getProductAttribute(productModel, ProductModel.PICTURE);
		if (picture != null)
		{
			return picture.getMediaContainer();
		}
		return null;
	}
}
