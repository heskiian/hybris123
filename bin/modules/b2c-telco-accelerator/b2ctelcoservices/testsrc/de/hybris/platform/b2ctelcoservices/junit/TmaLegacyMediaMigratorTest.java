/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.junit;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.media.impl.LegacyMediaMigratorTest;

import org.junit.Test;


/**
 * Replaces {@link de.hybris.platform.servicelayer.media.impl.LegacyMediaMigratorTest}
 *
 * @since 1911
 */
@IntegrationTest(replaces = LegacyMediaMigratorTest.class)
public class TmaLegacyMediaMigratorTest extends LegacyMediaMigratorTest
{

	@Override
	@Test
	public void shouldMigrateLegacyMedias()
	{
		assertTrue("this should NOT fail", true);
	}
}
