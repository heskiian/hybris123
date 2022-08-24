/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.checklist.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecCharacteristicData;
import de.hybris.platform.b2ctelcoservices.model.TmaConfigurablePscvPolicyStatementModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of {@link TmaConfigurablePscvPolicyStatementModel} from {@link TmaChecklistActionData}
 *
 * @since 1907
 */
public class TmaChecklistActionCharacteristicDataPopulator
		implements Populator<TmaConfigurablePscvPolicyStatementModel, TmaChecklistActionData>
{
	@Override
	public void populate(TmaConfigurablePscvPolicyStatementModel source,
			TmaChecklistActionData target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		TmaProductSpecCharacteristicData productSpecCharacteristicData = new TmaProductSpecCharacteristicData();
		TmaProductSpecCharacteristicModel tmaProductSpecCharacteristicModel = source.getProductSpecCharacteristic();
		productSpecCharacteristicData.setId(tmaProductSpecCharacteristicModel.getId());
		productSpecCharacteristicData.setName(tmaProductSpecCharacteristicModel.getName());
		productSpecCharacteristicData.setDescription(tmaProductSpecCharacteristicModel.getDescription());
		target.setCharacteristic(productSpecCharacteristicData);
	}
}
