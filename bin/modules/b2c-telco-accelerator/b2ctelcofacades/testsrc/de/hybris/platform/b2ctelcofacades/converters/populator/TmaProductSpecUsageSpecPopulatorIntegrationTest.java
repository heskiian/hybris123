/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Integration Test of {@link TmaProductSpecUsageSpecPopulator}.
 *
 * @since 2102
 */
@IntegrationTest
public class TmaProductSpecUsageSpecPopulatorIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String PRODUCT_USAGE_SPEC_ID_1 = "ipdr_wlan_spec";

	private TmaProductSpecificationModel source;
	private TmaProductSpecificationData target;

	@Resource
	private TmaProductSpecUsageSpecPopulator tmaProductSpecUsageSpecPopulator;

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
		source.setProductUsageSpecification(getProductUsageSpecificationModel(PRODUCT_USAGE_SPEC_ID_1));

		getTmaProductSpecUsageSpecPopulator().populate(source, target);
		assertTrue("It hasn't the expected Product Usage Specification",
				source.getProductUsageSpecification().getId().equals(target.getUsageSpecification().getId()));
	}

	private TmaProductUsageSpecificationModel getProductUsageSpecificationModel(final String id)
	{
		final TmaProductUsageSpecificationModel productUsageSpecificationModel = new TmaProductUsageSpecificationModel();
		setLocale(productUsageSpecificationModel);
		productUsageSpecificationModel.setId(id);
		return productUsageSpecificationModel;
	}

	private void setLocale(ItemModel itemModel)
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) itemModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}

	protected TmaProductSpecUsageSpecPopulator getTmaProductSpecUsageSpecPopulator()
	{
		return tmaProductSpecUsageSpecPopulator;
	}
}

