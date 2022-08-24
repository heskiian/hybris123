/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaAtomicProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.StubLocaleProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.internal.model.impl.LocaleProvider;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;

import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;


/**
 * Unit Test for {@link TmaCompositeProductSpecificationChildrenPopulator}
 *
 * @since 2102
 */
@UnitTest
public class TmaCompositeProductSpecificationChildrenPopulatorTest
{
	private static final String COMPOSITE_PRODUCT_SPEC_ID = "starter_plan";
	private static final String COMPOSITE_PRODUCT_SPEC_NAME = "Starter Plan";
	private static final String ATOMIC_PRODUCT_SPEC_NAME = "unlimitedPlans";
	private static final String ATOMIC_PRODUCT_SPEC_ID = "unlimitedPlans5G";

	@Mock
	private Map<String, Converter<TmaProductSpecificationModel, TmaProductSpecificationData>> productSpecificationConverterMap;
	@Mock
	Converter<TmaProductSpecificationModel, TmaProductSpecificationData> tmaProductSpecificationConverter;

	private TmaCompositeProductSpecificationChildrenPopulator tmaCompositeProductSpecificationChildrenPopulator;
	private TmaCompositeProductSpecificationModel source;
	private TmaCompositeProductSpecificationData target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = createCompositeProductSpec(COMPOSITE_PRODUCT_SPEC_ID);
		target = new TmaCompositeProductSpecificationData();
		tmaCompositeProductSpecificationChildrenPopulator = new TmaCompositeProductSpecificationChildrenPopulator(productSpecificationConverterMap);
		final TmaProductSpecificationModel atomicProductSpecModel = createAtomicProductSpecModel(ATOMIC_PRODUCT_SPEC_ID);
		final Set<TmaProductSpecificationModel> children = new HashSet<>();
		children.add(atomicProductSpecModel);
		source.setChildren(children);
		when(productSpecificationConverterMap.get(TmaAtomicProductSpecificationModel._TYPECODE))
				.thenReturn(tmaProductSpecificationConverter);
		when(productSpecificationConverterMap.get(TmaCompositeProductSpecificationModel._TYPECODE))
				.thenReturn(tmaProductSpecificationConverter);
	}

	@Test
	public void testPopulate()
	{
		tmaCompositeProductSpecificationChildrenPopulator.populate(source, target);
		final TmaProductSpecificationData atomicProductSpecData =
				target.getChildren().stream().filter(ps -> ps instanceof TmaProductSpecificationData).findFirst().get();
		final TmaProductSpecificationModel atomicProductSpecModel =
				source.getChildren().stream().filter(ps -> ps instanceof TmaProductSpecificationModel).findFirst().get();
		Assert.assertEquals(1, target.getChildren().size());
		Assert.assertEquals(atomicProductSpecModel.getId(), atomicProductSpecData.getId());
		Assert.assertEquals(atomicProductSpecModel.getName(), atomicProductSpecData.getName());
	}

	private TmaAtomicProductSpecificationModel createAtomicProductSpecModel(final String id)
	{
		final TmaAtomicProductSpecificationModel productSpecificationModel = new TmaAtomicProductSpecificationModel();
		setLocale(productSpecificationModel);
		productSpecificationModel.setId(id);
		productSpecificationModel.setName(ATOMIC_PRODUCT_SPEC_NAME);
		setupMockAtomicProductSpec(productSpecificationModel);
		return productSpecificationModel;
	}

	private TmaProductSpecificationData setupMockAtomicProductSpec(final TmaAtomicProductSpecificationModel atomicProductSpecModel)
	{
		final TmaProductSpecificationData productSpecificationData = new TmaProductSpecificationData();
		productSpecificationData.setId(ATOMIC_PRODUCT_SPEC_ID);
		productSpecificationData.setName(ATOMIC_PRODUCT_SPEC_NAME);
		when(tmaProductSpecificationConverter.convert(atomicProductSpecModel)).thenReturn(productSpecificationData);
		return productSpecificationData;
	}

	private void setLocale(final ItemModel itemModel)
	{
		final LocaleProvider localeProvider = new StubLocaleProvider(Locale.ENGLISH);
		final ItemModelContextImpl itemModelContext = (ItemModelContextImpl) itemModel.getItemModelContext();
		itemModelContext.setLocaleProvider(localeProvider);
	}

	private TmaCompositeProductSpecificationModel createCompositeProductSpec(final String id)
	{
		final TmaCompositeProductSpecificationModel compositeProductSpecificationModel = new TmaCompositeProductSpecificationModel();
		setLocale(compositeProductSpecificationModel);
		compositeProductSpecificationModel.setId(id);
		compositeProductSpecificationModel.setName(COMPOSITE_PRODUCT_SPEC_NAME);
		return compositeProductSpecificationModel;
	}
}
