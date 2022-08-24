/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.cart;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MessageWsDTO;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercewebservicescommons.dto.order.CartWsDTO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
 * JUnit Tests for the @{TmaCartMessageAttributeMapper}
 *
 * @since 1911
 */
@UnitTest
public class TmaCartMessageAttributeMapperTest
{
	private static final String ERROR_CODE = "COMPATIBILITY";
	private static final String ERROR_MESSAGE = "Missing required product";
	
	@Mock
	private MapperFacade mapperFacade;

	@InjectMocks
	private final TmaCartMessageAttributeMapper mapper = new TmaCartMessageAttributeMapper();

	private CartData source;
	private MappingContext context;
	private CartWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		source = new CartData();
		target = new CartWsDTO();
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{

		final Set<TmaCartValidationData> invalidMessages = new HashSet<>();
		invalidMessages.add(createCartValidationData(ERROR_CODE, ERROR_MESSAGE));
		source.setValidationMessages(invalidMessages);

		invalidMessages.forEach(invalidMessage ->
		{
			final MessageWsDTO message = new MessageWsDTO();
			message.setType(invalidMessage.getCode());
			message.setValue(invalidMessage.getMessage());

			Mockito.when(mapperFacade.map(invalidMessage, MessageWsDTO.class, context)).thenReturn(message);
		});

		mapper.populateTargetAttributeFromSource(source, target, context);

		Assert.assertEquals(1, source.getValidationMessages().size());
		Assert.assertEquals(source.getValidationMessages().iterator().next().getMessage(), target.getMessage().get(0).getValue());
		Assert.assertEquals(source.getValidationMessages().iterator().next().getCode(), target.getMessage().get(0).getType());
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

	protected TmaCartValidationData createCartValidationData(final String errorCode, final String errorMessage)
	{
		final TmaCartValidationData cartValidationData = new TmaCartValidationData();
		cartValidationData.setCode(errorCode);
		cartValidationData.setMessage(errorMessage);
		return cartValidationData;
	}

}
