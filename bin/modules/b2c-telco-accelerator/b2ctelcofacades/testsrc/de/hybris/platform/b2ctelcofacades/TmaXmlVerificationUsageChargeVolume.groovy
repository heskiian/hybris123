/**
 *
 */
package de.hybris.platform.b2ctelcofacades

import de.hybris.bootstrap.annotations.IntegrationTest
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest
import de.hybris.platform.subscriptionfacades.XMLVerificationUsageChargeVolume
import de.hybris.platform.util.AppendSpringConfiguration
import org.junit.Test

/**
 * Port of old ATDD-framework tests to Spock framework
 */
@IntegrationTest(replaces = XMLVerificationUsageChargeVolume.class)
class TmaXmlVerificationUsageChargeVolume extends AbstractSubscriptionFacadesSpockTest {

	// is run before every test
	def setup() {
	}

	// is run after every test
	def cleanup() {
	}

	@Test
	def "XML Verification Usage Charge Volume Overage Video"() {
		// Test_XML_Verification_Usage_Charge_Volume_Overage_Video
			}

	@Test
	def "XML Verification Usage Charge Volume TierFrom1To1 Video"() {
		// Test_XML_Verification_Usage_Charge_Volume_TierFrom1To1_Video

	}

	@Test
	def "XML Verification Usage Charge Volume TierFrom1To2 Video"() {
		// Test_XML_Verification_Usage_Charge_Volume_TierFrom1To2_Video

	}

	@Test
	def "XML Verification Usage Charge Volume TierFrom1To2 From3to3 Video"() {
		// Test_XML_Verification_Usage_Charge_Volume_TierFrom1To2_From3To3_Video

	}

	@Test
	def "XML Verification Usage Charge Volume TierFrom1To2 From3to4 Video"() {
		// Test_XML_Verification_Usage_Charge_Volume_TierFrom1To2_From3To4_Video

	}

	@Test
	def "XML Verification Usage Charge Volume Overage TierFrom1To2 From3to3 Video"() {
		// Test_XML_Verification_Usage_Charge_Volume_Overage_TierFrom1To2_From3To3_Video

	}

	@Test
	def "XML Verification Usage Charge Volume Overage video minute"() {
		// Test_XML_Verification_Usage_Charge_Volume_Overage_Video_Minute

	}

	@Test
	def "XML Verification Usage Charge Volume TierFrom1To2 From3To4 video minute"() {
		// Test_XML_Verification_Usage_Charge_Volume_TierFrom1To2_From3To4_Video_Minute

	}

	@Test
	def "XML Verification Usage Charge Volume Overage video TierFrom1To2  minute"() {
		// Test_XML_Verification_Usage_Charge_Volume_Overage_Video_TierFrom1To2_Minute

	}

}
