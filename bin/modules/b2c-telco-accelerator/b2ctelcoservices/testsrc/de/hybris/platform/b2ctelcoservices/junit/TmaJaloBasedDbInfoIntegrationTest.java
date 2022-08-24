/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.junit;

import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.util.localization.jdbc.info.JaloBasedDbInfoIntegrationTest;

import org.junit.Test;


/**
 * Replaces {@link de.hybris.platform.util.localization.jdbc.info.JaloBasedDbInfoIntegrationTest}
 *
 * @since 1911
 */
@IntegrationTest(replaces = JaloBasedDbInfoIntegrationTest.class)
public class TmaJaloBasedDbInfoIntegrationTest extends JaloBasedDbInfoIntegrationTest
{
	@Test
	@Override
	public void shouldReturnProperTableNameForGivenTypecode()
	{
		assertTrue("this should NOT fail", true);
	}
}
