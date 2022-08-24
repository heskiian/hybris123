/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.financialaccountref;

import de.hybris.platform.billmanagementservices.model.BmFinancialAccountModel;
import de.hybris.platform.billmanagementtmfwebservices.constants.BillmanagementtmfwebservicesConstants;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.FinancialAccountRef;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;
import de.hybris.platform.util.Config;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atReferredType attribute between {@link BmFinancialAccountModel} and {@link FinancialAccountRef}
 *
 * @since 2108
 */
public class FinancialAccountRefReferredTypeAttributeMapper
		extends BmAttributeMapper<BmFinancialAccountModel, FinancialAccountRef>
{
	public FinancialAccountRefReferredTypeAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmFinancialAccountModel source, final FinancialAccountRef target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtreferredType(
					Config.getParameter(BillmanagementtmfwebservicesConstants.FINANCIAL_ACCOUNT_REF_DEFAULT_REFERRED));
		}
	}
}
