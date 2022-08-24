/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.SubscriptionTermsTest
import org.junit.Test

/**
 *  Replacement of SubscriptionTermsTest from subscriptionfacades
 */
@IntegrationTest(replaces = SubscriptionTermsTest.class)
class TmaSubscriptionTermsTest extends AbstractSubscriptionFacadesSpockTest{
    // is run before every test
    def setup() {
    }

    // is run after every test
    def cleanup() {
    }

    @Test
    def "Test Subscription Terms Default For Cancellable"() {
        // Test_Subscription_Terms_Default_For_Cancellable

    }

    @Test
    def "Test Subscription Terms Not Cancellable"() {
        // Test_Subscription_Terms_Not_Cancellable

    }

    @Test
    def "Test Subscription Terms Custom Billing Plan"() {
        // Test_Subscription_Terms_Custom_BillingPlan

    }

}
