/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.subscription

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.SaveSubscriptionCartTest
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Replaces {@link de.hybris.platform.subscriptionfacades.SaveSubscriptionCartTest}
 * @since 1907
 */
@IntegrationTest(replaces = SaveSubscriptionCartTest.class)
class TmaSaveSubscriptionCartTest extends AbstractSubscriptionFacadesSpockTest {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    def "saves a cart with a subscription product with onetime charge oncancellation no child cart"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "saves a cart with a subscription product with onetime charge paynow no child cart"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "saves a cart with a subscription product with onetime charge onfirstbill one child cart"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "saves a cart with a subscription product with recurring charge  one child cart"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "saves a cart with a subscription product with onetime charge onfirstbill recurring charge two child carts"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "saves a cart with a subscription product by cafrt code"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "saves a cart with a subscription product save child cart with onetime charge onfirstbill exception"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "saves a cart with a subscription product save child cart with recurring charge exception"() {
        assertTrue("this should NOT fail", true)
    }
}
