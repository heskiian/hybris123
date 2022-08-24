/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.subscriptionfacades.AbstractSubscriptionFacadesSpockTest;
import de.hybris.platform.subscriptionfacades.XMLVerificationSubscriptionTermTest
import org.junit.Test;


/**
 *   Replacement of XMLVerificationSubscriptionTermTest from subscriptionfacades
 */
@IntegrationTest(replaces = XMLVerificationSubscriptionTermTest.class)
class TmaXMLVerificationSubscriptionTermTest extends AbstractSubscriptionFacadesSpockTest
{
	// is run before every test
	def setup() {
	}

	// is run after every test
	def cleanup() {
	}

	@Test
	def "Test XML Verification Subscription Term AutoRenewing Monthly"() {
		// Test_XML_Verification_Subscription_Term_AutoRenewing_Monthly
	}

	@Test
	def "Test XML Verification Subscription Term AutoRenewing Daily"() {
		// Test_XML_Verification_Subscription_Term_AutoRenewing_Daily
	}

	@Test
	def "Test XML Verification Subscription Term AutoRenewing Weekly"() {
		// Test_XML_Verification_Subscription_Term_AutoRenewing_Weekly
	}

	@Test
	def "Test XML Verification Subscription Term NonRenewing Daily"() {
		// Test_XML_Verification_Subscription_Term_NonRenewing_Daily
	}

	@Test
	def "Test XML Verification Subscription Term NonRenewing Weekly"() {
		// Test_XML_Verification_Subscription_Term_NonRenewing_Weeky
	}

	@Test
	def "Test XML Verification Subscription Term RenewsOnce Daily"() {
		// Test_XML_Verification_Subscription_Term_RenewsOnce_Daily
	}

	@Test
	def "Test XML Verification Subscription Term RenewsOnce Weekly"() {
		// Test_XML_Verification_Subscription_Term_RenewsOnce_Weekly
	}

}
