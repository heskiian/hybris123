/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecTypeData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.text.ParseException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


@UnitTest
public class TmaProductSpecTypeBasicPopulatorUnitTest
{
	private static final String PS_TYPE_ID = "electricity";
	private static final String PS_TYPE_DESCRIPTION = "Electricity";

	private TmaProductSpecTypeData target;
	private TmaProductSpecTypeModel source;

	private TmaProductSpecTypeBasicPopulator populator;

	@Before
	public void setUp() throws ParseException
	{
		populator = new TmaProductSpecTypeBasicPopulator();
		target = new TmaProductSpecTypeData();
	}

	@Test
	public void testPopulate()
	{
		givenSource(PS_TYPE_ID);
		whenPopulateIsRun();
		thenTheTargetIsPopulated();
	}

	private void givenSource(final String id)
	{
		source = new TmaProductSpecTypeModel();
		setLocale();
		source.setCode(id);
		source.setDescription(PS_TYPE_DESCRIPTION);
	}

	private void whenPopulateIsRun()
	{
		populator.populate(source, target);
	}

	private void thenTheTargetIsPopulated()
	{
		assertTrue("It hasn't the expected id", source.getCode().equals(target.getId()));
		assertTrue("It hasn't the expected description", source.getDescription().equals(target.getDescription()));
	}

	private void setLocale()
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) source.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}
}
