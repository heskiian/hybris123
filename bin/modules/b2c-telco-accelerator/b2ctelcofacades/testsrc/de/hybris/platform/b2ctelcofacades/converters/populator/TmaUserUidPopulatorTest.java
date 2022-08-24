/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import de.hybris.platform.b2ctelcofacades.data.TmaUserData;
import de.hybris.platform.core.model.user.UserModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;


/**
 * Test suite for {@link TmaUserUidPopulator}
 *
 * @since 1810
 */
public class TmaUserUidPopulatorTest
{
	private static final String USER_UID = "testUid";
	private TmaUserUidPopulator userUidPopulator;

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		userUidPopulator = new TmaUserUidPopulator();
	}

	@Test
	public void testPopulate()
	{
		final UserModel source = mock(UserModel.class);
		given(source.getUid()).willReturn(USER_UID);
		final TmaUserData target = new TmaUserData();
		userUidPopulator.populate(source, target);
		Assert.assertEquals(source.getUid(), target.getUid());
	}
}
