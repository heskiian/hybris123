/**
 *
 */
package de.hybris.platform.b2ctelcofacades

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.XMLVerificationUsageChargePerUnitAndVolumeTest
import de.hybris.platform.util.AppendSpringConfiguration
import org.junit.Test

/**
 * Port of old ATDD-framework tests to Spock framework
 */
@IntegrationTest(replaces = XMLVerificationUsageChargePerUnitAndVolumeTest.class)
class TmaXmlVerificationUsageChargePerUnitAndVolumeTest extends AbstractSubscriptionFacadesSpockTest {

	// is run before every test
	def setup() {
	}

	// is run after every test
	def cleanup() {
	}

	@Test
	def "XML Verification Usage Charge PerUnit Overage Video Volume Overage Minute"() {
		// Test_XML_Verification_Usage_Charge_PerUnit_Overage_Video_Volume_Overage_Minute
	}

	@Test
	def "XML Verification Usage Charge PerUnit Overage Video Volume TierFrom1To2 Minute"() {
		// Test_XML_Verification_Usage_Charge_PerUnit_Overage_Video_Volume_TierFrom1To2_Minute
	}

	@Test
	def "XML Verification Usage Charge PerUnit TierFrom1To2 Video Volume Overage Minute"() {
		// Test_XML_Verification_Usage_Charge_PerUnit_TierFrom1To2_Video_Volume_Overage_Minute
	}
}
