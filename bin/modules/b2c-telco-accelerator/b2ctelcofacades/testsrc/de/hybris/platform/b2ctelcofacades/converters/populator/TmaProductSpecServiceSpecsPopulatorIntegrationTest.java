/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaServiceSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCustomerFacingServiceSpecModel;
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
 * Integration Test of {@link TmaProductSpecServiceSpecsPopulator}.
 *
 * @since 2102
 */
@IntegrationTest
public class TmaProductSpecServiceSpecsPopulatorIntegrationTest extends ServicelayerTransactionalTest
{
	private static final String CFS_ID_1 = "fttx_data_svc";

	private TmaProductSpecificationModel source;
	private TmaProductSpecificationData target;

	@Resource
	private TmaProductSpecServiceSpecsPopulator tmaProductSpecServiceSpecsPopulator;

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
		source.setCfsSpecs(getCustomerFacingServiceSpecModels());

		getTmaProductSpecServiceSpecsPopulator().populate(source, target);

		assertTrue("It hasn't the expected Service Specifications", target.getServiceSpecification().size() == 1);

		final TmaCustomerFacingServiceSpecModel expectedCfs = source.getCfsSpecs().iterator().next();
		final TmaServiceSpecificationData actualCfs = target.getServiceSpecification().iterator().next();
		assertTrue("It hasn't the expected Service Specifications", expectedCfs.getId().equals(actualCfs.getId()));
	}

	private TmaCustomerFacingServiceSpecModel getCustomerFacingServiceSpecModel(final String id)
	{
		final TmaCustomerFacingServiceSpecModel customerFacingServiceSpecModel = new TmaCustomerFacingServiceSpecModel();
		setLocale(customerFacingServiceSpecModel);
		customerFacingServiceSpecModel.setId(id);
		return customerFacingServiceSpecModel;
	}

	private Set<TmaCustomerFacingServiceSpecModel> getCustomerFacingServiceSpecModels()
	{
		final Set<TmaCustomerFacingServiceSpecModel> customerFacingServiceSpecModels = new HashSet<>();
		customerFacingServiceSpecModels.add(getCustomerFacingServiceSpecModel(CFS_ID_1));
		return customerFacingServiceSpecModels;
	}

	private void setLocale(ItemModel itemModel)
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) itemModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}

	protected TmaProductSpecServiceSpecsPopulator getTmaProductSpecServiceSpecsPopulator()
	{
		return tmaProductSpecServiceSpecsPopulator;
	}
}
