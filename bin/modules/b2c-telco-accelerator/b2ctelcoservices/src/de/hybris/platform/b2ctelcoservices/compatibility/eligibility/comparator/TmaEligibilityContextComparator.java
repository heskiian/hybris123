/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.compatibility.eligibility.comparator;

import de.hybris.platform.b2ctelcoservices.eligibility.data.TmaEligibilityContext;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;


/**
 * Comparator for determining duplicate values of {@link TmaEligibilityContext} based on all attributes of
 * {@link TmaEligibilityContext}.
 *
 * @since 2003
 */
public class TmaEligibilityContextComparator implements Comparator<TmaEligibilityContext>
{
	@Override
	public int compare(final TmaEligibilityContext instance1, final TmaEligibilityContext instance2)
	{
		if (instance1 instanceof TmaEligibilityContext && instance2 instanceof TmaEligibilityContext)
		{
			return (instance1.getBillingSystemId().equals(instance2.getBillingSystemId())
					&& instance1.getSubscriptionBaseId().equals(instance2.getSubscriptionBaseId())
					&& compareStringList(instance1.getBaseSiteCodes(), instance2.getBaseSiteCodes())
					&& compareStringList(instance1.getProcessesCodes(), instance2.getProcessesCodes())
					&& compareStringList(instance1.getRequiredPoCodes(), instance2.getRequiredPoCodes())
					&& compareStringList(instance1.getTermCodes(), instance2.getTermCodes())) ? 0 : 1;
		}
		return 1;
	}

	protected boolean compareStringList(final List<String> list1, final List<String> list2)
	{
		if (CollectionUtils.isEmpty(list1) && CollectionUtils.isEmpty(list2))
		{
			return true;
		}
		if (CollectionUtils.isNotEmpty(list1) && CollectionUtils.isNotEmpty(list2))
		{
			return list1.equals(list2);
		}
		return false;
	}

}
