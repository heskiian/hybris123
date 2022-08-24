/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link TmaMediaAttachmentPopulator}
 *
 * @since 2007
 */
@UnitTest
public class TmaMediaAttachmentPopulatorTest
{
	@Mock
	private Converter<MediaModel, MediaData> mediaConverter;

	private TmaMediaAttachmentPopulator mediaAttachmentPopulator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		mediaAttachmentPopulator = new TmaMediaAttachmentPopulator();
		mediaAttachmentPopulator.setMediaConverter(mediaConverter);
	}

	@Test
	public void testPopulate()
	{
		final TmaProductOfferingModel source = mock(TmaProductOfferingModel.class);
		final Collection<MediaModel> medias = new ArrayList<>();
		final MediaModel dataSheet = mock(MediaModel.class);
		medias.add(dataSheet);
		given(source.getData_sheet()).willReturn(medias);
		final List<MediaData> mediaDatas = Arrays.asList(new MediaData());
		Mockito.when(mediaConverter.convertAll(medias)).thenReturn(mediaDatas);
		final ProductData target = new ProductData();
		mediaAttachmentPopulator.populate(source, target);
		Assert.assertEquals(1, target.getDataSheet().size());
	}
}
