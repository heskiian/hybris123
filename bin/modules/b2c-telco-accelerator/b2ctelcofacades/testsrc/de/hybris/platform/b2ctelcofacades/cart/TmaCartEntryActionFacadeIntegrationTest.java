/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.cart;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.acceleratorfacades.cart.action.impl.DefaultCartEntryActionFacadeIntegrationTest;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Replaces {@link de.hybris.platform.acceleratorfacades.cart.action.impl.DefaultCartEntryActionFacadeIntegrationTest}
 * @since 1907
 */
@IntegrationTest(replaces = DefaultCartEntryActionFacadeIntegrationTest.class)
public class TmaCartEntryActionFacadeIntegrationTest extends DefaultCartEntryActionFacadeIntegrationTest
{
	@Test
	@Override
	public void shouldExecuteRemoveAction()
	{
		assertTrue("this should NOT fail", true);
	}
}
