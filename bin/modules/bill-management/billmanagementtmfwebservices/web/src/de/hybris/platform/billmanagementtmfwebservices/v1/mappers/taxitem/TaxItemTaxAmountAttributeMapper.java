/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.taxitem;

import de.hybris.platform.billmanagementservices.model.BmAppliedPartyBillingTaxRateModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.Money;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.TaxItem;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for taxAmount attribute between {@link BmAppliedPartyBillingTaxRateModel} and {@link TaxItem}
 *
 * @since 2108
 */
public class TaxItemTaxAmountAttributeMapper extends BmAttributeMapper<BmAppliedPartyBillingTaxRateModel, TaxItem>
{
	public TaxItemTaxAmountAttributeMapper(final String sourceAttributeName, final String targetAttributeName)
	{
		super(sourceAttributeName, targetAttributeName);
	}

	@Override
	public void populateTargetAttributeFromSource(final BmAppliedPartyBillingTaxRateModel source, final TaxItem target,
			final MappingContext context)
	{
		if (source.getQuantity() != null && source.getCurrency() != null)
		{
			final Money money = new Money();
			money.setValue(source.getQuantity());
			money.setUnit(source.getCurrency().getIsocode());
			target.setTaxAmount(money);
		}
	}
}
