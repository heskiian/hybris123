/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.checklist.converters.populators;

import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionParamData;
import de.hybris.platform.b2ctelcoservices.checklist.context.TmaChecklistContext;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates the attributes of {@link TmaChecklistContext} from {@link TmaChecklistActionParamData}
 *
 * @since 1907
 */
public class TmaChecklistActionParamDataReversePopulator implements Populator<TmaChecklistActionParamData, TmaChecklistContext>
{
	private TmaPoService tmaPoService;

	@Override
	public void populate(TmaChecklistActionParamData source,
			TmaChecklistContext target) throws ConversionException
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		target.setProcessType(source.getProcessType());
		target.setProductOfferings(getProductOfferings(source.getProductCodes()));
	}

	private List<ProductModel> getProductOfferings(List<String> productOfferingsCodes)
	{
		if (CollectionUtils.isEmpty(productOfferingsCodes))
		{
			return new ArrayList<>();
		}

		List<ProductModel> productOfferings = new ArrayList<>();
		productOfferingsCodes.forEach(productCode ->
		{
			ProductModel productModel = getTmaPoService().getPoForCode(productCode);
			productOfferings.add(productModel);
		});
		return productOfferings;
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	@Required
	public void setTmaPoService(TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}
}
