
/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmsfacades.data.MediaData;
import de.hybris.platform.cmsfacades.uniqueidentifier.UniqueItemIdentifierService;
import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TmaBasicMediaPopulator}
 *
 * @since 2007
 */
@UnitTest
public class TmaBasicMediaPopulatorTest
{
	private static final String FILE_NAME = "termsAndConditions.pdf";
	private static final String FOLDER_NAME = "Insurance";

	@Mock
	private UniqueItemIdentifierService uniqueItemIdentifierService;

	private TmaBasicMediaPopulator mediaPopulator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		mediaPopulator = new TmaBasicMediaPopulator();
		mediaPopulator.setUniqueItemIdentifierService(uniqueItemIdentifierService);
	}

	@Test
	public void testPopulate()
	{
		final MediaModel source = mock(MediaModel.class);
		final MediaData target = new MediaData();

		given(source.getRealFileName()).willReturn(FILE_NAME);
		final MediaFolderModel folder = mock(MediaFolderModel.class);
		given(folder.getQualifier()).willReturn(FOLDER_NAME);
		given(source.getFolder()).willReturn(folder);
		given(uniqueItemIdentifierService.getItemData(source)).willReturn(Optional.empty());
		mediaPopulator.populate(source, target);
		Assert.assertEquals(FILE_NAME, target.getRealFileName());
		Assert.assertEquals(FOLDER_NAME.toLowerCase(), target.getFolder());
	}
}
