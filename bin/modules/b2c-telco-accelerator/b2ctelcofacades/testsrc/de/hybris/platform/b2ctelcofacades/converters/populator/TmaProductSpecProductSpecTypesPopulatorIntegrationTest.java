/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
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
public class TmaProductSpecProductSpecTypesPopulatorIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String PRODUCT_SPEC_TYPE_ID_1 = "Plan";

	private TmaProductSpecificationModel source;
	private TmaProductSpecificationData target;

	@Resource
	private TmaProductSpecProductSpecTypesPopulator tmaProductSpecProductSpecTypesPopulator;

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
		source.setProductSpecificationTypes(getProductSpecTypeModels());

		getTmaProductSpecProductSpecTypesPopulator().populate(source, target);

		assertTrue("It hasn't the expected Product Spec Types", target.getProductSpecType().size() == 1);

		final TmaProductSpecTypeModel expectedProductSpecType = source.getProductSpecificationTypes().iterator().next();
		final TmaProductSpecTypeData actualProductSpecType = target.getProductSpecType().iterator().next();
		assertTrue("It hasn't the expected Product Spec Types",
				expectedProductSpecType.getCode().equals(actualProductSpecType.getId()));
	}

	private TmaProductSpecTypeModel getProductSpecTypeModel(final String id)
	{
		final TmaProductSpecTypeModel productSpecTypeModel = new TmaProductSpecTypeModel();
		setLocale(productSpecTypeModel);
		productSpecTypeModel.setCode(id);
		return productSpecTypeModel;
	}

	private Set<TmaProductSpecTypeModel> getProductSpecTypeModels()
	{
		final Set<TmaProductSpecTypeModel> productSpecTypeModels = new HashSet<>();
		productSpecTypeModels.add(getProductSpecTypeModel(PRODUCT_SPEC_TYPE_ID_1));
		return productSpecTypeModels;
	}

	private void setLocale(ItemModel itemModel)
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) itemModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}

	protected TmaProductSpecProductSpecTypesPopulator getTmaProductSpecProductSpecTypesPopulator()
	{
		return tmaProductSpecProductSpecTypesPopulator;
	}
}
