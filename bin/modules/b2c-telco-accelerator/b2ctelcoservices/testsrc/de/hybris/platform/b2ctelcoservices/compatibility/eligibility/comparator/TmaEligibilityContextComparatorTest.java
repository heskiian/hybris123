/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.comparator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;

import java.util.Collections;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Unit test for {@link TmaEligibilityContextComparator}.
 *
 * @since 2003
 */
@UnitTest
public class TmaEligibilityContextComparatorTest
{
	private TmaEligibilityContext tmaEligibilityContext1;
	private TmaEligibilityContext tmaEligibilityContext2;
	private TmaEligibilityContextComparator comparator;

	@Before
	public void setup()
	{
		tmaEligibilityContext1 = new TmaEligibilityContext();
		tmaEligibilityContext2 = new TmaEligibilityContext();
		comparator = new TmaEligibilityContextComparator();
	}

	@Test
	public void shouldCompareEquals()
	{
		// Given
		tmaEligibilityContext1.setBillingSystemId("IN");
		tmaEligibilityContext1.setSubscriptionBaseId("001101010");
		tmaEligibilityContext1.setBaseSiteCodes(Collections.singletonList("testSite"));
		tmaEligibilityContext1.setProcessesCodes(Collections.singletonList("ACQ"));
		tmaEligibilityContext1.setRequiredPoCodes(Collections.singletonList("myPhone"));
		tmaEligibilityContext1.setTermCodes(Collections.singletonList("term1weekly"));

		tmaEligibilityContext2.setBillingSystemId("IN");
		tmaEligibilityContext2.setSubscriptionBaseId("001101010");
		tmaEligibilityContext2.setBaseSiteCodes(Collections.singletonList("testSite"));
		tmaEligibilityContext2.setProcessesCodes(Collections.singletonList("ACQ"));
		tmaEligibilityContext2.setRequiredPoCodes(Collections.singletonList("myPhone"));
		tmaEligibilityContext2.setTermCodes(Collections.singletonList("term1weekly"));

		// Then
		final int comparisonResult = comparator.compare(tmaEligibilityContext1, tmaEligibilityContext2);
		Assert.assertTrue(0 == comparisonResult);
	}

	@Test
	public void shouldCompareNotEqual()
	{
		// Given
		tmaEligibilityContext1.setBillingSystemId("IN");
		tmaEligibilityContext1.setSubscriptionBaseId("001101010");
		tmaEligibilityContext1.setBaseSiteCodes(Collections.singletonList("testSite"));
		tmaEligibilityContext1.setProcessesCodes(Collections.singletonList("ACQ"));
		tmaEligibilityContext1.setRequiredPoCodes(Collections.singletonList("myPhone"));
		tmaEligibilityContext1.setTermCodes(Collections.singletonList("term1weekly"));

		tmaEligibilityContext2.setBillingSystemId("IN");
		tmaEligibilityContext2.setSubscriptionBaseId("");
		tmaEligibilityContext2.setBaseSiteCodes(Collections.singletonList(""));
		tmaEligibilityContext2.setProcessesCodes(Collections.singletonList(""));
		tmaEligibilityContext2.setRequiredPoCodes(Collections.singletonList(""));
		tmaEligibilityContext2.setTermCodes(Collections.singletonList(""));

		// Then
		final int comparisonResult = comparator.compare(tmaEligibilityContext1, tmaEligibilityContext2);
		Assert.assertTrue(0 != comparisonResult);
	}

}
