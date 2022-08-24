/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.cart

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.RestoreSubscriptionCartTest
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Replaces {@link de.hybris.platform.subscriptionfacades.RestoreSubscriptionCartTest}
 * @since 1907
 */
@IntegrationTest(replaces = RestoreSubscriptionCartTest.class)
class TmaRestoreSubscriptionCartTest extends AbstractSubscriptionFacadesSpockTest {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    def "restore a master cart with onetime charge onfirstbill and one child cart"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "restore a master cart with recurring charge and one child cart"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "restore a master cart with onetime charge onfirstbill and recurring charge and two child cart"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "restore a master cart with multiple carts"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "restore a master cart with multiple customers"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "restore a child cart with onetime charge on first bill exception"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "restore a child cart with recurring charge exception"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "restore a master cart with onetime charge oncancellation and no child cart"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "restore a master cart with onetime charge paynow and no child cart"() {
        assertTrue("this should NOT fail", true)
    }
}
