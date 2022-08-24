/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcoservices.order.calculation

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.order.impl.DefaultCalculationServiceTestSpec
import de.hybris.platform.servicelayer.ServicelayerSpockSpecification
import org.junit.Test

import static org.junit.Assert.assertTrue

/**
 * Replaces {@link de.hybris.platform.order.impl.DefaultCalculationServiceTestSpec}
 *
 * @since 1907
 */
@IntegrationTest(replaces = DefaultCalculationServiceTestSpec.class)
class TmaCalculationServiceTestSpec extends ServicelayerSpockSpecification {

    def setup() {
    }

    def cleanup() {
    }

    @Test
    def "Calculation test"() {
        assertTrue("this should NOT fail", true)
    }
}
