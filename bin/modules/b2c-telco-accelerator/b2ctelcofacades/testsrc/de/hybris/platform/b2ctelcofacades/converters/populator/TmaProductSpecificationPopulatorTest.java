/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecTypeData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductUsageSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaServiceSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecTypeModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductUsageSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaServiceSpecificationModel;
import de.hybris.platform.commercefacades.product.data.ProductData;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;


/**
 * Test suite for {@link TmaProductOfferingProductSpecificationPopulator}
 *
 * @since 1810
 */
@UnitTest
public class TmaProductSpecificationPopulatorTest
{
	private static final String PS_ID = "testPsId";
	private static final String PS_NAME = "testPsName";

	private TmaProductOfferingProductSpecificationPopulator poProductSpecificationPopulator;

	@Before
	public void setUp()
	{
		// init te Product Spec populator
		final TmaProductSpecPopulator tmaProductSpecPopulator = new TmaProductSpecPopulator();

		// init the product spec converter
		AbstractPopulatingConverter<TmaProductSpecificationModel, TmaProductSpecificationData> tmaProductSpecificationConverter = new AbstractPopulatingConverter<>();
		List<Populator<TmaProductSpecificationModel, TmaProductSpecificationData>> populatorList = new ArrayList<>();
		populatorList.add(tmaProductSpecPopulator);
		tmaProductSpecificationConverter.setPopulators(populatorList);
		tmaProductSpecificationConverter.setTargetClass(TmaProductSpecificationData.class);

		// init PO populator for product spec
		poProductSpecificationPopulator = new TmaProductOfferingProductSpecificationPopulator();
		poProductSpecificationPopulator.setProductSpecificationConverter(tmaProductSpecificationConverter);
	}

	@Test
	public void testPopulate()
	{
		final TmaProductOfferingModel source = mock(TmaProductOfferingModel.class);
		final TmaProductSpecificationModel productSpecification = mock(TmaProductSpecificationModel.class);
		given(productSpecification.getId()).willReturn(PS_ID);
		given(productSpecification.getName()).willReturn(PS_NAME);
		given(source.getProductSpecification()).willReturn(productSpecification);
		final ProductData target = new ProductData();
		poProductSpecificationPopulator.populate(source, target);
		Assert.assertEquals(source.getProductSpecification().getId(), target.getProductSpecification().getId());
		Assert.assertEquals(source.getProductSpecification().getName(), target.getProductSpecification().getName());
	}

	@Test
	public void testProductSpecificationNull()
	{
		final TmaProductOfferingModel source = mock(TmaProductOfferingModel.class);
		given(source.getProductSpecification()).willReturn(null);
		final ProductData target = new ProductData();
		poProductSpecificationPopulator.populate(source, target);
		Assert.assertNull(target.getProductSpecification());
	}

	@Test
	public void testSourceInstanceOfBPO()
	{
		final TmaBundledProductOfferingModel source = mock(TmaBundledProductOfferingModel.class);
		final ProductData target = new ProductData();
		poProductSpecificationPopulator.populate(source, target);
		Assert.assertNull(target.getProductSpecification());
	}

}
