/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Integration Test of {@link TmaProductSpecPscPopulator}.
 *
 * @since 2102
 */
@IntegrationTest
public class TmaProductSpecPscPopulatorIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String PSC_ID_1 = "upload_speed";

	private TmaProductSpecificationModel source;
	private TmaProductSpecificationData target;

	@Resource
	private TmaProductSpecPscPopulator tmaProductSpecPscPopulator;

	@Before
	public void setUp()
	{
		source = new TmaAtomicProductSpecificationModel();
		setLocale(source);
		target = new TmaProductSpecificationData();
	}

	@Test
	public void testPopulate()
	{
		source.setProductSpecCharacteristics(getProductSpecCharacteristics());

		getTmaProductSpecPscPopulator().populate(source, target);

		assertTrue("It hasn't the expected Product Spec Characteristics",
				target.getProductSpecCharacteristic().size() == 1);
		final TmaProductSpecCharacteristicModel expectedPsc = source.getProductSpecCharacteristics().iterator().next();
		final TmaProductSpecCharacteristicData actualPsc = target.getProductSpecCharacteristic().iterator().next();
		assertTrue("It hasn't the expected Product Spec Characteristics", expectedPsc.getId().equals(actualPsc.getId()));
	}

	private TmaProductSpecCharacteristicModel getProductSpecCharacteristicModel(final String id)
	{
		final TmaProductSpecCharacteristicModel pscModel = new TmaProductSpecCharacteristicModel();
		setLocale(pscModel);
		pscModel.setId(id);
		pscModel.setConfigurable(false);
		return pscModel;
	}

	private Set<TmaProductSpecCharacteristicModel> getProductSpecCharacteristics()
	{
		final Set<TmaProductSpecCharacteristicModel> productSpecCharacteristicModels = new HashSet<>();
		productSpecCharacteristicModels.add(getProductSpecCharacteristicModel(PSC_ID_1));
		return productSpecCharacteristicModels;
	}

	private void setLocale(ItemModel itemModel)
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) itemModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}

	protected TmaProductSpecPscPopulator getTmaProductSpecPscPopulator()
	{
		return tmaProductSpecPscPopulator;
	}
}
