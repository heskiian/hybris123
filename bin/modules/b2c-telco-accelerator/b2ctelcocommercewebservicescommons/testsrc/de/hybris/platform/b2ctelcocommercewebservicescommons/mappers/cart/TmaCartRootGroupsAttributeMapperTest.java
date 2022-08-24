/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.RootGroupsWsDTO;
import de.hybris.platform.commercefacades.order.EntryGroupData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;
import de.hybris.platform.core.enums.GroupType;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaCartRootGroupsAttributeMapper}
 */
@UnitTest
public class TmaCartRootGroupsAttributeMapperTest
{
	@Mock
	private MapperFacade mapperFacade;
	
	@InjectMocks
	private final TmaCartRootGroupsAttributeMapper mapper = new TmaCartRootGroupsAttributeMapper();

	MappingContext context;

	CartData source;

	CartWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new CartData();
		target = new CartWsDTO();

		source.setRootGroups(Arrays.asList(entryGroupData(), entryGroupData()));
	}

	protected EntryGroupData entryGroupData()
	{
		final EntryGroupData result = new EntryGroupData();
		result.setGroupNumber(0);
		result.setGroupType(GroupType.B2CTELCO_BPO);
		result.setExternalReferenceId("mobileDeal");
		result.setErroneous(true);
		result.setValidationMessages(null);
		result.setIsAutoPickEnabled(false);
		return result;
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		final RootGroupsWsDTO rootGroup = new RootGroupsWsDTO();
		rootGroup.setGroupNumber(entryGroupData().getGroupNumber());
		source.getRootGroups().forEach(group ->
		{
			Mockito.when(mapperFacade.map(group, RootGroupsWsDTO.class, context)).thenReturn(rootGroup);
		});
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getRootGroups().get(1).getGroupNumber(), target.getRootGroups().get(1).getGroupNumber());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullSource()
	{
		mapper.populateTargetAttributeFromSource(null, target, context);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPopulateTargetAttributeFromSourceWithNullTarget()
	{
		mapper.populateTargetAttributeFromSource(source, null, context);
	}
}
