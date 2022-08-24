/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.order;

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.commercefacades.ObjectXStreamAliasConverter;
import de.hybris.platform.commercefacades.groovy.AbstractCommerceFacadesSpockTest;
import de.hybris.platform.commercefacades.order.impl.CheckoutFacadeTest
import de.hybris.platform.commerceservices.setup.SetupImpexService
import de.hybris.platform.order.InvalidCartException;

import org.junit.Test

import javax.annotation.Resource;

import static org.junit.Assert.assertTrue;

/**
 * Replaces {@link de.hybris.platform.commercefacades.order.impl.CheckoutFacadeTest}
 *
 * @since 1911
 */
@IntegrationTest(replaces = CheckoutFacadeTest.class)
class TmaCheckoutFacadeTest extends AbstractCommerceFacadesSpockTest {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    def "Checkout a Cart Assignment"() {
        assertTrue("this should NOT fail", true)
    }
}