/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.converters.populator.TmaProductSpecCharacteristicPopulator;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Integration Test of {@link TmaProductSpecCharacteristicPopulator}.
 *
 * @since 2102
 */
@IntegrationTest
public class TmaProductSpecCharacteristicPopulatorIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String PSC_ID = "sms";
	private static final String PSCV_ID_1 = "100_min";
	private static final String PSCV_ID_2 = "300_min";
	private static final String PSCV_ID_3 = "500_min";
	private static final String PSC_NAME = "SMS";
	private static final String PSC_DESCRIPTION = "Included sms";

	private TmaProductSpecCharacteristicModel source;
	private TmaProductSpecCharacteristicData target;

	@Resource
	private TmaProductSpecCharacteristicPopulator tmaProductSpecCharacteristicPopulator;

	@Before
	public void setUp()
	{
		target = new TmaProductSpecCharacteristicData();
		source = new TmaProductSpecCharacteristicModel();
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) source.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
		source.setId(PSC_ID);
		source.setName(PSC_NAME);
		source.setDescription(PSC_DESCRIPTION);
		source.setConfigurable(Boolean.FALSE);

		final Set<TmaProductSpecCharacteristicValueModel> productSpecCharacteristicValueModels = new HashSet<>();
		productSpecCharacteristicValueModels.addAll(getTmaProductSpecCharacteristicValueModels());
		source.setProductSpecCharacteristicValues(productSpecCharacteristicValueModels);
	}

	@Test
	public void testPopulate()
	{
		getTmaProductSpecCharacteristicPopulator().populate(source, target);

		assertTrue("It hasn't the expected id", source.getId().equals(target.getId()));
		assertTrue("It hasn't the expected name", source.getName().equals(target.getName()));
		assertTrue("It hasn't the expected description", source.getDescription().equals(target.getDescription()));
		assertTrue("It hasn't the expected configurable flag", Boolean.FALSE.equals(target.isConfigurable()));
		assertTrue("It hasn't the expected Product Specification Characteristic Values",
				source.getProductSpecCharacteristicValues().size() == target.getProductSpecCharacteristicValues().size());
	}

	private TmaProductSpecCharacteristicValueModel getTmaProductSpecCharacteristicValueModel(final String id)
	{
		final TmaProductSpecCharacteristicValueModel tmaProductSpecCharacteristicValueModel =
				new TmaProductSpecCharacteristicValueModel();
		setLocaleProvider((ItemModelContextImpl) tmaProductSpecCharacteristicValueModel.getItemModelContext());
		tmaProductSpecCharacteristicValueModel.setId(id);
		return tmaProductSpecCharacteristicValueModel;
	}

	private Set<TmaProductSpecCharacteristicValueModel> getTmaProductSpecCharacteristicValueModels()
	{
		final TmaProductSpecCharacteristicValueModel tmaProductSpecCharacteristicValueModel1 =
				getTmaProductSpecCharacteristicValueModel(PSCV_ID_1);
		final TmaProductSpecCharacteristicValueModel tmaProductSpecCharacteristicValueModel2 =
				getTmaProductSpecCharacteristicValueModel(PSCV_ID_2);
		final TmaProductSpecCharacteristicValueModel tmaProductSpecCharacteristicValueModel3 =
				getTmaProductSpecCharacteristicValueModel(PSCV_ID_3);
		final Set<TmaProductSpecCharacteristicValueModel> productSpecCharacteristicValueModels =
				new HashSet<>(Arrays.asList(tmaProductSpecCharacteristicValueModel1, tmaProductSpecCharacteristicValueModel2,
						tmaProductSpecCharacteristicValueModel3));
		return productSpecCharacteristicValueModels;
	}

	private TmaProductSpecCharacteristicPopulator getTmaProductSpecCharacteristicPopulator()
	{
		return tmaProductSpecCharacteristicPopulator;
	}

	private void setLocaleProvider(ItemModelContextImpl itemModelContext)
	{
		final StubLocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		itemModelContext.setLocaleProvider(localeProvider);
	}
}
