/**
 *
 */
package de.hybris.platform.b2ctelcofacades

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.XMLVerificationComplexTest
import de.hybris.platform.util.AppendSpringConfiguration
import org.junit.Test

/**
 * Replacement of XMLVerificationComplexTest
 */
@IntegrationTest(replaces = XMLVerificationComplexTest.class)
class TmaXmlVerificationComplexTest extends AbstractSubscriptionFacadesSpockTest {

	// is run before every test
	def setup() {
	}

	// is run after every test
	def cleanup() {
	}

	@Test
	def "XML Verification Complex Currency USD"() {

	}

	@Test
	def "XML Verification Complex Currency EUR"() {
		// Test_XML_Verification_Complex_Currency_EUR
	}
}
