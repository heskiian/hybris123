/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.converters.populator.TmaProductSpecCharacteristicValuePopulator;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicValueData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.subscriptionservices.model.UsageUnitModel;

import java.text.ParseException;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


@UnitTest
public class TmaProductSpecCharacteristicValuePopulatorUnitTest
{
	private static final String PSCV_ID = "MSE_10000_DATA_MB";
	private static final String PSCV_VALUE = "10000";
	private static final String PSCV_DESCRIPTION = "Includes 10 GB of Data";
	private static final String PSCV_USAGE_UNIT_MODEL = "MB";
	private UsageUnitModel usageUnitModel;

	@Mock
	private CatalogVersionService catalogVersionService;

	private static final String PRODUCT_CATALOG = "b2ctelcoProductCatalog";
	private static final String PRODUCT_CATALOG_VERSION = "Online";

	private TmaProductSpecCharacteristicValueModel source;
	private TmaProductSpecCharacteristicValueData target;

	private TmaProductSpecCharacteristicValuePopulator populator;

	@Before
	public void setUp() throws ParseException
	{
		initMocks(this);
		populator = new TmaProductSpecCharacteristicValuePopulator();
		target = new TmaProductSpecCharacteristicValueData();
		source = new TmaProductSpecCharacteristicValueModel();
		usageUnitModel = new UsageUnitModel();
		usageUnitModel.setId(PSCV_USAGE_UNIT_MODEL);
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) source.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		source.setId(PSCV_ID);
		source.setValue(PSCV_VALUE);
		source.setDescription(PSCV_DESCRIPTION);
		source.setUnitOfMeasure(usageUnitModel);
		catalogVersionService.setSessionCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION);
		source.setCatalogVersion(catalogVersionService.getCatalogVersion(PRODUCT_CATALOG, PRODUCT_CATALOG_VERSION));
	}

	@Test
	public void testPopulate()
	{
		whenPopulateIsRun();
		testDataPopulated();
	}

	private void testDataPopulated()
	{
		assertTrue("It hasn't the expected id", source.getId().equals(target.getId()));
		assertTrue("It hasn't the expected Value", source.getValue().equals(target.getValue()));
		assertTrue("It hasn't the expected Description", source.getDescription().equals(target.getDescription()));
		assertTrue("It hasn't the expected Unit of measurment",
				source.getUnitOfMeasure().getId().equals(target.getUnitOfMeasurment()));
	}

	private void whenPopulateIsRun()
	{
		populator.populate(source, target);
	}
}
