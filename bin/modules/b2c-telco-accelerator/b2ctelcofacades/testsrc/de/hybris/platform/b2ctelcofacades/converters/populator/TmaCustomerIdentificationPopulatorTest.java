/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.Mockito.mock;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * Test class for {@link TmaCustomerIdentificationPopulator}
 * 
 * @since 1911
 */

@UnitTest
public class TmaCustomerIdentificationPopulatorTest
{

	private TmaCustomerIdentificationsPopulator populator;
	private CustomerModel source;
	private CustomerData target;
	@Mock
	private Converter<TmaIdentificationModel, TmaIdentificationData> tmaIdentificationConverter;

	@Before
	public void before()
	{
		MockitoAnnotations.initMocks(this);
		populator = new TmaCustomerIdentificationsPopulator(tmaIdentificationConverter);
		target = new CustomerData();
		source = new CustomerModel();
	}

	@Test
	public void testPopulateIdentificationData()
	{
		final Set<TmaIdentificationModel> identifications = new HashSet<>();
		final TmaIdentificationModel identificationModel = mock(TmaIdentificationModel.class);
		identifications.add(identificationModel);
		source.setIdentifications(identifications);
		populator.populate(source, target);
		Assert.assertNotNull(target.getIdentifications());
	}

	@Test
	public void testPopulateNullData()
	{
		source.setIdentifications(null);
		populator.populate(source, target);
		Assert.assertEquals(Collections.EMPTY_LIST, target.getIdentifications());
	}
}
