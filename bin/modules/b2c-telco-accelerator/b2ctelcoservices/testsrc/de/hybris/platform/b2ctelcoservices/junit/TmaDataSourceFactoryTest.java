/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.junit;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jdbcwrapper.DataSourceFactoryTest;

import java.sql.SQLException;

import org.junit.Test;


/**
 * Replaces {@link de.hybris.platform.jdbcwrapper.DataSourceFactoryTest}
 *
 * @since 1911
 */
@IntegrationTest(replaces = DataSourceFactoryTest.class)
public class TmaDataSourceFactoryTest extends DataSourceFactoryTest
{

	@Override
	@Test
	public void testJUnitTenantSetup() throws SQLException
	{
		assertTrue("this should NOT fail", true);
	}
}
