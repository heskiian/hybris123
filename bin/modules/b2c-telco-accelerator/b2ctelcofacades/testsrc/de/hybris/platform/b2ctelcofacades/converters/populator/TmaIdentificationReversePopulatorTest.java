/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.junit.Assert.assertEquals;

import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link TmaIdentificationReversePopulator}.
 *
 * @since 1911
 *
 */
public class TmaIdentificationReversePopulatorTest
{
	public static final String NUMBER = "12345";
	public static final String TYPE = "PASSPORT";
	@InjectMocks
	private TmaIdentificationReversePopulator reversePopulator;


	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		reversePopulator = new TmaIdentificationReversePopulator();
	}


	@Test
	public void testPopulate()
	{
		final TmaIdentificationData source = new TmaIdentificationData();
		final TmaIdentificationModel target = new TmaIdentificationModel();
		source.setIdentificationNumber(NUMBER);
		source.setIdentificationType(TYPE);
		reversePopulator.populate(source, target);
		assertEquals(NUMBER, target.getIdentificationNumber());
		assertEquals(TYPE, target.getIdentificationType().getCode());
	}

	@Test
	public void testPopulateNull()
	{
		final TmaIdentificationData source = new TmaIdentificationData();
		final TmaIdentificationModel target = new TmaIdentificationModel();
		reversePopulator.populate(source, target);
		assertEquals(null, target.getIdentificationNumber());
		assertEquals(null, target.getIdentificationType());
	}

}
