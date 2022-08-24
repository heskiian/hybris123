/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.junit;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.ddl.dbtypesystem.impl.DefaultSQLRowsProviderTest;

import org.junit.Test;


/**
 * Replaces {@link de.hybris.bootstrap.ddl.dbtypesystem.impl.DefaultSQLRowsProviderTest}
 *
 * @since 1911
 */
@IntegrationTest(replaces = DefaultSQLRowsProviderTest.class)
public class TmaDefaultSQLRowsProviderTest extends DefaultSQLRowsProviderTest
{
	@Override
	@Test
	public void testGetNumberSeriesRows() throws Exception
	{
		assertTrue("this should NOT fail", true);
	}

	@Override
	@Test
	public void testTypeRowMapper()
	{
		assertTrue("this should NOT fail", true);
	}
}


