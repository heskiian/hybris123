/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator.variants.options;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.commercefacades.product.ImageFormatMapping;
import de.hybris.platform.commercefacades.product.data.ImageData;
import de.hybris.platform.commercefacades.product.data.VariantOptionData;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


/**
 * Unit tests for {@link TmaVariantOptionDataMediaPopulator}.
 *
 * @since 1810
 */
@UnitTest
public class TmaVariantOptionDataMediaPopulatorTest
{
	private static final String MEDIA_FORMAT1 = "zoom";
	private static final String MEDIA_FORMAT2 = "product";
	private static final String MEDIA_FORMAT_UNKNOWN = "unknown_format";
	private static final List<String> IMAGE_FORMATS = Arrays.asList(MEDIA_FORMAT1, MEDIA_FORMAT2);

	private TmaVariantOptionDataMediaPopulator mediaPopulator;
	private VariantOptionData variantOptionData;

	@Mock
	private TmaPoVariantModel poVariant;
	@Mock
	private TmaSimpleProductOfferingModel baseSpo;
	@Mock
	private Converter<MediaModel, ImageData> imageConverter;
	@Mock
	private ImageFormatMapping acceleratorImageFormatMapping;


	@Before
	public void setUp()
	{
		initMocks(this);
		when(poVariant.getTmaBasePo()).thenReturn(baseSpo);
		this.variantOptionData = new VariantOptionData();
		this.mediaPopulator = new TmaVariantOptionDataMediaPopulator();
		this.mediaPopulator.setImageConverter(imageConverter);
		this.mediaPopulator.setImageFormats(IMAGE_FORMATS);
		this.mediaPopulator.setAcceleratorImageFormatMapping(acceleratorImageFormatMapping);
	}

	@Test
	public void givenNoMedias_thenMediasAreNotPopulated()
	{
		givenMediaContainers(poVariant, null);
		givenMediaContainers(baseSpo, null);
		whenMediasArePopulated();
		thenMediasSizeIs(0);
	}

	@Test
	public void givenVariantWithMedias_thenMediasArePopulated()
	{
		givenMediaContainers(poVariant, Arrays.asList(createMediaContainer(MEDIA_FORMAT1, MEDIA_FORMAT2)));
		givenMediaContainers(baseSpo, null);
		whenMediasArePopulated();
		thenMediasSizeIs(2);
	}

	@Test
	public void givenBasePoWithMedias_thenMediasArePopulated()
	{
		givenMediaContainers(poVariant, null);
		givenMediaContainers(baseSpo, Arrays.asList(createMediaContainer(MEDIA_FORMAT1)));
		whenMediasArePopulated();
		thenMediasSizeIs(1);
	}

	@Test
	public void givenVariantWithManyContainers_thenMediasArePopulated()
	{
		givenMediaContainers(poVariant,
				Arrays.asList(createMediaContainer(MEDIA_FORMAT1, MEDIA_FORMAT2), createMediaContainer(MEDIA_FORMAT1)));
		givenMediaContainers(baseSpo, null);
		whenMediasArePopulated();
		thenMediasSizeIs(3);
	}

	@Test
	public void givenMediasWithUnknownFormat_thenMediasArePopulated()
	{
		givenMediaContainers(poVariant, Arrays.asList(createMediaContainer(MEDIA_FORMAT_UNKNOWN)));
		givenMediaContainers(baseSpo, null);
		whenMediasArePopulated();
		thenMediasSizeIs(1);
	}

	private void thenMediasSizeIs(int expectedSize)
	{
		assertEquals(expectedSize, variantOptionData.getVariantOptionQualifiers().size());
	}

	private void whenMediasArePopulated()
	{
		mediaPopulator.populate(poVariant, variantOptionData);
	}

	private void givenMediaContainers(final TmaProductOfferingModel poModel, final List<MediaContainerModel> mediaContainers)
	{
		when(poModel.getGalleryImages()).thenReturn(mediaContainers);
	}

	private MediaContainerModel createMediaContainer(String... mediaFormats)
	{
		MediaContainerModel mediaContainer = mock(MediaContainerModel.class);
		final List<MediaModel> medias = new ArrayList<>();
		for (String mediaFormat : mediaFormats)
		{
			medias.add(createMedia(mediaFormat));
		}
		when(mediaContainer.getMedias()).thenReturn(medias);
		return mediaContainer;
	}

	private MediaModel createMedia(final String mediaFormatName)
	{
		final MediaModel media = mock(MediaModel.class);
		final MediaFormatModel mediaFormatModel = mock(MediaFormatModel.class);
		final ImageData imageData = mock(ImageData.class);
		when(mediaFormatModel.getName()).thenReturn(mediaFormatName);
		when(media.getMediaFormat()).thenReturn(mediaFormatModel);
		when(imageConverter.convert(media)).thenReturn(imageData);
		when(acceleratorImageFormatMapping.getMediaFormatQualifierForImageFormat(mediaFormatName)).thenReturn(mediaFormatName);
		return media;
	}
}
