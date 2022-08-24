/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.S
 */

package de.hybris.platform.b2ctelcofacades.cart

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.CartCalculationOneTimeAndRecurringChargeTest
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Replaces {@link de.hybris.platform.subscriptionfacades.CartCalculationOneTimeAndRecurringChargeTest}
 * @since 1907
 */
@IntegrationTest(replaces = CartCalculationOneTimeAndRecurringChargeTest.class)
class TmaCartCalculationOneTimeAndRecurringChargeTest extends AbstractSubscriptionFacadesSpockTest {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    def "cart calculation onetime and recurring charge on first bill cycle from 1 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on first bill cycle from 1 to 1 from 2 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on first bill cycle from 1 to 3 from 4 to 4 and from 5 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on first bill cycle from 1 to 1 from 2 one productx2"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on first bill cycle from 1 to 1 from 2 one productx3"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on first bill cycle from 1 to 1 from 2 two product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on cancellatin cycle from 1 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on cancellatin cycle from 1 to 1 from 2 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on cancellatin cycle from 1 to 1 from 2 one productx2"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on cancellatin cycle from 1 to 1 from 2 one productx3 "() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge on cancellation cycle from 1 to 1 from 2 two product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge pay now cycle from 1 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge pay now cycle from 1 to 1 from 2 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge pay now cycle from 1 to 3 from 4 to 4 from 5 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge paynow cycle from 1 to 1 from 2 one productx2"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge pay now cycle from 1 to 1 from 2 one productx3"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge paynow cycle from 1 to 1 from 2 two product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge onfirstbill oncancellation paynow cycle from 1 to 1 from 2 one product one plan"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge onfirstbill oncancellation paynow cycle from 1 to 1 from 2 multiple products multiple plans"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge billingeventteat1 cycle from 1 to 1 from 2 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge billingeventteat1 cycle from 1 to 1 from 2 two product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge removing complex"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation onetime and recurring charge removing adding removing complex"() {
        assertTrue("this should NOT fail", true)
    }
}
