/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import static org.junit.Assert.assertEquals;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.EnumerationValueData;
import de.hybris.platform.b2ctelcoservices.enums.TmaServiceType;
import de.hybris.platform.core.HybrisEnumValue;

import org.junit.Before;
import org.junit.Test;


@UnitTest
public class EnumerationValuePopulatorTest
{
	private EnumerationValuePopulator populator;
	private HybrisEnumValue source;
	private EnumerationValueData target;

	@Before
	public void setup()
	{
		this.populator = new EnumerationValuePopulator();
		this.target = new EnumerationValueData();
	}

	@Test
	public void testPopulateCode()
	{
		givenModel(TmaServiceType.TARIFF_PLAN);
		whenPopulateIsRun();
		thenDataIsPopulated();
	}

	private void givenModel(HybrisEnumValue enumValue)
	{
		source = enumValue;
	}

	private void whenPopulateIsRun()
	{
		populator.populate(source, target);
	}

	private void thenDataIsPopulated()
	{
		assertEquals(source.getCode(), target.getCode());
	}
}
