/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.populators;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.upilintegrationservices.data.I_UtilsProdSalesAttributeType;
import de.hybris.platform.upilintegrationservices.model.UpilAdditionalAttributesModel;

import org.springframework.util.ObjectUtils;


/**
 * Populates {@link UpilAdditionalAttributesModel} to {@link I_UtilsProdSalesAttributeType}
 *
 * @since 1911
 */

public class UpilSalesAttributePopulator implements Populator<UpilAdditionalAttributesModel, I_UtilsProdSalesAttributeType>
{

	@Override
	public void populate(final UpilAdditionalAttributesModel source, final I_UtilsProdSalesAttributeType target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);
		target.setUtilitiesProduct(source.getPricePlan().getCode());
		target.setUtilsSalesFactValue(source.getUtilsSalesFactValue());
		if (!ObjectUtils.isEmpty(source.getCurrency()))
		{
			target.setCurrency(source.getCurrency().getIsocode());
		}
		if (!ObjectUtils.isEmpty(source.getSemantics()))
		{
			target.setUtilsSemanticsName1(source.getSemantics().getSemanticsName1());
			target.setUtilsSemanticsName2(source.getSemantics().getSemanticsName2());
		}
	}
}
