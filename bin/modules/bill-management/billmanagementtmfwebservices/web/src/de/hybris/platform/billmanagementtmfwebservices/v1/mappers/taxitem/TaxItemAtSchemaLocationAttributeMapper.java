/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.taxitem;

import de.hybris.platform.billmanagementservices.model.BmAppliedPartyBillingTaxRateModel;
import de.hybris.platform.billmanagementtmfwebservices.constants.BillmanagementtmfwebservicesConstants;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.TaxItem;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import org.apache.commons.lang3.StringUtils;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for atSchemaLocation attribute between {@link BmAppliedPartyBillingTaxRateModel} and {@link TaxItem}
 *
 * @since 2108
 */
public class TaxItemAtSchemaLocationAttributeMapper extends BmAttributeMapper<BmAppliedPartyBillingTaxRateModel, TaxItem>
{
	public TaxItemAtSchemaLocationAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmAppliedPartyBillingTaxRateModel source, final TaxItem target,
			final MappingContext context)
	{
		if (StringUtils.isNotEmpty(source.getId()))
		{
			target.setAtschemaLocation(BillmanagementtmfwebservicesConstants.BM_API_SCHEMA);
		}
	}
}
