/**
 *
 */
package de.hybris.platform.b2ctelcofacades

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.CurrencySwitchTest
import de.hybris.platform.util.AppendSpringConfiguration
import org.junit.Test

/**
 * Replacement of test from subscriptionfacades
 */
@IntegrationTest(replaces = CurrencySwitchTest.class)
class TmaCurrencySwitchTest extends AbstractSubscriptionFacadesSpockTest {

	// is run before every test
	def setup() {

	}

	// is run after every test
	def cleanup() {

	}

	@Test
	def "Switch currency one subscription product"() {
		// Test_Switch_Currency_One_SubscriptionProduct

	}

	@Test
	def "Switch currency three subscription products"() {
		// Test_Switch_Currency_Three_SubscriptionProducts

	}

	@Test
	def "Switch currency xml"() {
	}

}
