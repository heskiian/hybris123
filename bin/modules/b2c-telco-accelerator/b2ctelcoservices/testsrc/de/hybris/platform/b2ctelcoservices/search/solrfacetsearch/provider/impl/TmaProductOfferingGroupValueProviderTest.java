/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.search.solrfacetsearch.provider.impl;

import static org.mockito.Mockito.when;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.PropertyFieldValueProviderTestBase;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;


/**
 * Test to check if ProductOfferingGroupValueProvider {@link TmaProductOfferingGroupValueProvider} returns the groups
 * associated with the product Offering.
 *
 */
public class TmaProductOfferingGroupValueProviderTest extends PropertyFieldValueProviderTestBase
{
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private final TmaProductOfferingModel product = new TmaProductOfferingModel();
	@Mock
	private IndexedProperty indexedProperty;

	@Mock
	private TmaBundledProductOfferingModel BPO1, BPO2;

	@Mock
	private TmaProductOfferingGroupModel group;

	private final String FIELD_NAME = "productOfferingGroups";

	@Before
	public void setUp()
	{
		configure();
	}

	@Override
	protected String getPropertyName()
	{
		return FIELD_NAME;
	}

	@Override
	protected void configure()
	{
		setPropertyFieldValueProvider(new TmaProductOfferingGroupValueProvider());
		configureBase();

		((TmaProductOfferingGroupValueProvider) getPropertyFieldValueProvider()).setFieldNameProvider(fieldNameProvider);
		product.setParents(createParents());
		when(fieldNameProvider.getFieldNames(indexedProperty, null)).thenReturn(Lists.newArrayList(getPropertyName()));
	}

	@Test
	public void testGetFiledValues() throws FieldValueProviderException
	{
		final Collection<FieldValue> result = ((FieldValueProvider) getPropertyFieldValueProvider()).getFieldValues(indexConfig,
				indexedProperty, product);
		Assert.assertEquals(2, result.size());
		final Set<String> expectedResult = Sets.newHashSet("bpo1_default", "group1");
		for (final FieldValue fieldValue : result)
		{
			Assert.assertTrue(expectedResult.contains(String.valueOf(fieldValue.getValue())));
		}
	}

	private Set<TmaBundledProductOfferingModel> createParents()
	{
		final List<TmaProductOfferingGroupModel> emptylist = new ArrayList<>();
		when(BPO1.getCode()).thenReturn("bpo1");
		when(BPO1.getProductOfferingGroups()).thenReturn(emptylist);
		group.setParentBundleProductOffering(BPO2);
		group.setChildProductOfferings(new HashSet<>(Arrays.asList(product)));
		group.setCode("group1");
		when(group.getParentBundleProductOffering()).thenReturn(BPO2);
		when(group.getChildProductOfferings()).thenReturn(new HashSet<>(Arrays.asList(product)));
		when(group.getCode()).thenReturn("group1");
		when(BPO2.getCode()).thenReturn("bpo2");
		when(BPO2.getProductOfferingGroups()).thenReturn(Arrays.asList(group));
		final Set<TmaBundledProductOfferingModel> set = new HashSet<>();
		set.add(BPO1);
		set.add(BPO2);
		return set;
	}
}
