/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TmaMediaPopulator}
 *
 * @since 1810
 */
@UnitTest
public class TmaMediaPopulatorTest
{
	@Mock
	private Converter<MediaModel, ImageData> imageConverter;

	private TmaMediaPopulator mediaPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		mediaPopulator = new TmaMediaPopulator();
		mediaPopulator.setImageConverter(imageConverter);
	}

	@Test
	public void testPopulate()
	{
		final TmaProductOfferingModel source = mock(TmaProductOfferingModel.class);
		final Collection<MediaModel> medias = new ArrayList<>();
		final MediaModel media1 = mock(MediaModel.class);
		medias.add(media1);
		final List<MediaContainerModel> galleryImages = new ArrayList<>();
		final MediaContainerModel galleryImage1 = mock(MediaContainerModel.class);
		given(galleryImage1.getMedias()).willReturn(medias);
		galleryImages.add(galleryImage1);
		given(source.getGalleryImages()).willReturn(galleryImages);
		final ProductData target = new ProductData();
		mediaPopulator.populate(source, target);
		Assert.assertEquals(1, target.getImages().size());
	}

	@Test
	public void testEmptyGalleryImages()
	{
		final TmaProductOfferingModel source = mock(TmaProductOfferingModel.class);
		given(source.getGalleryImages()).willReturn(null);
		final ProductData target = new ProductData();
		mediaPopulator.populate(source, target);
		Assert.assertEquals(CollectionUtils.EMPTY_COLLECTION, target.getImages());
	}
}
