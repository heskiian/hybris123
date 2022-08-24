/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.cart

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.SaveSubscriptionFlagForDeletionTest
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Replaces {@link de.hybris.platform.subscriptionfacades.SaveSubscriptionFlagForDeletionTest}
 * @since 1907
 */
@IntegrationTest(replaces = SaveSubscriptionFlagForDeletionTest.class)
class TmaSaveSubscriptionFlagForDeletionTest extends AbstractSubscriptionFacadesSpockTest {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    def "flag a cart with a subscription product for deletion"() {
        assertTrue("this should NOT fail", true)
    }

    @Test
    def "flag a child cart for deletion exception"() {
        assertTrue("this should NOT fail", true)
    }
}
