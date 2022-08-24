/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Unit Test for {@link TmaProductSpecPopulator}
 *
 * @since 2102
 */
@UnitTest
public class TmaProductSpecPopulatorIntegrationTest
{
	private static final String ATOMIC_PS_ID_1 = "composite_id_1";
	private static final String ATOMIC_PS_NAME_1 = "internet";
	private static final String DESCRIPTION_PS_1 = "Internet Product Specification";
	private static final String BRAND_PS_NAME_1 = "Internet Brand";

	private TmaProductSpecPopulator tmaProductSpecPopulator;

	private TmaProductSpecificationModel source;
	private TmaProductSpecificationData target;

	@Before
	public void setUp()
	{
		tmaProductSpecPopulator = new TmaProductSpecPopulator();
		source = getProductSpecificationModel();
		setLocale(source);
		target = new TmaCompositeProductSpecificationData();
	}

	@Test
	public void testPopulate()
	{
		tmaProductSpecPopulator.populate(source, target);

		assertTrue("It hasn't the expected id", source.getId().equals(target.getId()));
		assertTrue("It hasn't the expected name", source.getName().equals(target.getName()));
		assertTrue("It hasn't the expected description", source.getDescription().equals(target.getDescription()));
		assertTrue("It hasn't the expected description", source.getBrand().equals(target.getBrand()));
	}

	private void setLocale(ItemModel itemModel)
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) itemModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}

	private TmaProductSpecificationModel getProductSpecificationModel()
	{
		final TmaProductSpecificationModel tmaProductSpecificationModel = new TmaAtomicProductSpecificationModel();
		setLocale(tmaProductSpecificationModel);
		tmaProductSpecificationModel.setId(ATOMIC_PS_ID_1);
		tmaProductSpecificationModel.setName(ATOMIC_PS_NAME_1);
		tmaProductSpecificationModel.setDescription(DESCRIPTION_PS_1);
		tmaProductSpecificationModel.setBrand(BRAND_PS_NAME_1);
		return tmaProductSpecificationModel;
	}
}
