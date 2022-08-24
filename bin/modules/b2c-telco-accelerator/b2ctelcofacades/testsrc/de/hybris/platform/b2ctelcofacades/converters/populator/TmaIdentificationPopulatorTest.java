/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcoservices.enums.TmaIdentificationType;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link TmaIdentificationPopulator}.
 *
 * @since 1911
 *
 */
@UnitTest
public class TmaIdentificationPopulatorTest
{
	public static final String NUMBER = "12345";
	@InjectMocks
	private TmaIdentificationPopulator populator;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		populator = new TmaIdentificationPopulator();
	}


	@Test
	public void testPopulate()
	{
		final TmaIdentificationModel source = new TmaIdentificationModel();
		final TmaIdentificationData target = new TmaIdentificationData();
		source.setIdentificationNumber(NUMBER);
		source.setIdentificationType(TmaIdentificationType.DRIVING_LICENSE);
		populator.populate(source, target);
		assertEquals(NUMBER, target.getIdentificationNumber());
		assertEquals(TmaIdentificationType.DRIVING_LICENSE.getCode(), target.getIdentificationType());
	}

	@Test
	public void testPopulateNull()
	{
		final TmaIdentificationModel source = new TmaIdentificationModel();
		final TmaIdentificationData target = new TmaIdentificationData();
		populator.populate(source, target);
		assertEquals(null, target.getIdentificationNumber());
		assertEquals(null, target.getIdentificationType());
	}
}
