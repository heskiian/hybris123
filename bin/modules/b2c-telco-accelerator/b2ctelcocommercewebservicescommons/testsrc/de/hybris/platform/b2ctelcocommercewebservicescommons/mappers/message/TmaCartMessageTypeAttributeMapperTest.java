/*
 *   Copyright (c) 2020 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcooccaddon.mappers.message;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaCartValidationData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.MessageWsDTO;
import de.hybris.platform.b2ctelcocommercewebservicescommons.mappers.message.TmaCartMessageTypeAttributeMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * JUnit Tests for the @{TmaCartMessageTypeAttributeMapper}
 *
 * @since 1911
 */
@UnitTest
public class TmaCartMessageTypeAttributeMapperTest
{
	private static final String ERROR_CODE = "COMPATIBILITY";
	private static final String ERROR_MESSAGE = "Missing required product";

	@InjectMocks
	private final TmaCartMessageTypeAttributeMapper mapper = new TmaCartMessageTypeAttributeMapper();

	@Mock
	private MapperFacade mapperFacade;

	MappingContext context;

	private TmaCartValidationData source;
	private MessageWsDTO target;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
		target = new MessageWsDTO();
		source = createCartValidationData(ERROR_CODE, ERROR_MESSAGE);
	}

	@Test
	public void testPopulateTargetAttributeFromSource()
	{
		mapper.populateTargetAttributeFromSource(source, target, context);
		Assert.assertEquals(source.getCode(), target.getType());
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
