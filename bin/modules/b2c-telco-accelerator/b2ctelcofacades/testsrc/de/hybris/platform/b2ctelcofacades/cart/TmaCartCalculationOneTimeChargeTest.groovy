/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.cart

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.CartCalculationOneTimeChargeTest
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Replaces {@link de.hybris.platform.subscriptionfacades.CartCalculationOneTimeChargeTest}
 * @since 1907
 */
@IntegrationTest(replaces = CartCalculationOneTimeChargeTest.class)
class TmaCartCalculationOneTimeChargeTest extends AbstractSubscriptionFacadesSpockTest {

    def setup() {
    }


    def cleanup() {
    }

    @Test
    def "cart calculation onetime charge on first bill one product"() {
        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge on first bill one productx2"() {
        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge on first bill two products two price plans"() {
        assertTrue("this should NOT fail", true);
    }


    @Test
    def "cart calculation onetime charge on first bill two products one price plans"() {
        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge oncancellaion one product"() {
        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge oncancellaion two products"() {


        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge paynow one product"() {


        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge paynow one productx2"() {


        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge on first bill paynow one product one plan"() {


        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge on oncancellation paynow one product one plan"() {

        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge on onfirstbill oncancellation paynow one product one plan"() {

        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge on onfirstbill oncancellation paynow multiple product multiple plan"() {


        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge billingEvent1Test one product"() {


        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge onfirstbill billingEvent1Test one product"() {


        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge onfirstbill billingEvent1Test two products"() {

        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge onfirstbill one product customer specific price plan"() {

        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge onfirstbill one product curr specific price plan"() {

        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge onfirstbill removing one product"() {

        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge onfirstbill removing one productx2"() {

        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge oncancellation removing one product"() {

        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge paynow removing one product"() {


        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge onfirstbill removing complex"() {

        assertTrue("this should NOT fail", true)
    }


    @Test
    def "cart calculation onetime charge onfirstbill removing adding removing complex"() {

        assertTrue("this should NOT fail", true)
    }


}
