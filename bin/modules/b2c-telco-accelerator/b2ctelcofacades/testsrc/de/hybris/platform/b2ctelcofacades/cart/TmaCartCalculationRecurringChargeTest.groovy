/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.cart

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.CartCalculationRecurringChargeTest
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Replaces {@link de.hybris.platform.subscriptionfacades.CartCalculationRecurringChargeTest}
 * @since 1907
 */
@IntegrationTest(replaces = CartCalculationRecurringChargeTest.class)
class TmaCartCalculationRecurringChargeTest extends AbstractSubscriptionFacadesSpockTest {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    def "cart calculation recurring charge cycle from 0 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 1 from 2 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 1 from 2 to 2 from 3 to 3 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 1 from 2 to 2 from 3 to 4 one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 3 from 4 to 6  one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 3 from 4 to 4  one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 3 from 4 to 5  one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 two products"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 1 from 2 two products"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 1 from 2 to 2 from 3 to 3 two products"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 1 from 2 to 2 from 3 to 4 two products"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 3 from 4 to 6 two products"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 3 from 4 to 4 two products"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 3 from 4 to 4 from 5 two products"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge cycle from 1 to 1 from 2 to 4 one productx2"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge different cycle ranges three products"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge removing one product"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge removing one productx2"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge removing adding removing one productx1"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "cart calculation recurring charge removing complex"() {
        assertTrue("this should NOT fail", true)
    }
}
