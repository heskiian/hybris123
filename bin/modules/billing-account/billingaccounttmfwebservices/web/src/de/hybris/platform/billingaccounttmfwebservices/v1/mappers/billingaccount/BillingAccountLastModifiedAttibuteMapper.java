/**
 *
 */
package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MappingContext;


/**
 *
 */
public class BillingAccountLastModifiedAttibuteMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	public BillingAccountLastModifiedAttibuteMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillingAccountModel source, final BillingAccount target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getModifiedtime()))
		{
			return;
		}
		target.setLastModified(source.getModifiedtime());
	}
}
