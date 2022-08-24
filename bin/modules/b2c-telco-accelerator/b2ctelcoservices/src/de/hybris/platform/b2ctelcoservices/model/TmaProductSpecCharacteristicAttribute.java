/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.model;

import de.hybris.platform.b2ctelcoservices.services.TmaProductSpecificationService;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

import org.springframework.beans.factory.annotation.Required;


/**
 * AttributeHandler for dynamic attribute productSpecCharValue. Product Specification Characteristic Value For The
 * Average Service Usage.
 *
 * @since 18.05
 */
public class TmaProductSpecCharacteristicAttribute
		extends AbstractDynamicAttributeHandler<TmaProductSpecCharacteristicValueModel, TmaAverageServiceUsageModel>
{
	TmaProductSpecificationService tmaProductSpecificationService;

	@Override
	public TmaProductSpecCharacteristicValueModel get(final TmaAverageServiceUsageModel tmaAverageServiceUsageModel)
	{
		final TmaProductSpecCharacteristicValueModel tmaProductSpecCharacteristicValueModel = getTmaProductSpecificationService()
				.getTmaProductSpecCharacteristicValueModelForId(tmaAverageServiceUsageModel.getPscvId());

		return tmaProductSpecCharacteristicValueModel;
	}

	@Override
	public void set(final TmaAverageServiceUsageModel model, final TmaProductSpecCharacteristicValueModel value)
	{
		throw new UnsupportedOperationException();
	}

	protected TmaProductSpecificationService getTmaProductSpecificationService()
	{
		return tmaProductSpecificationService;
	}

	@Required
	public void setTmaProductSpecificationService(final TmaProductSpecificationService tmaProductSpecificationService)
	{
		this.tmaProductSpecificationService = tmaProductSpecificationService;
	}
}
