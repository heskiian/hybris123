/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaCompositeProductSpecificationData;
import de.hybris.platform.b2ctelcofacades.data.TmaProductSpecificationData;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;


/**
 * Populates the children information to a {@link TmaCompositeProductSpecificationData} from a
 * {@link TmaCompositeProductSpecificationModel} entity.
 *
 * @since 2102
 */
public class TmaCompositeProductSpecificationChildrenPopulator
		implements Populator<TmaCompositeProductSpecificationModel, TmaCompositeProductSpecificationData>
{
	private Map<String, Converter<TmaProductSpecificationModel, TmaProductSpecificationData>> productSpecificationConverterMap;

	public TmaCompositeProductSpecificationChildrenPopulator(
			final Map<String, Converter<TmaProductSpecificationModel, TmaProductSpecificationData>> productSpecificationConverterMap)
	{
		this.productSpecificationConverterMap = productSpecificationConverterMap;
	}

	@Override
	public void populate(final TmaCompositeProductSpecificationModel source, final TmaCompositeProductSpecificationData target)
	{
		final Set<TmaProductSpecificationModel> childrenProductSpec = source.getChildren();
		if (CollectionUtils.isEmpty(childrenProductSpec))
		{
			return;
		}

		final Set<TmaProductSpecificationData> childrenPsData = new HashSet<>();

		childrenProductSpec.forEach(childPs ->
		{
			final Converter<TmaProductSpecificationModel, TmaProductSpecificationData> productSpecConverter =
					getProductOfferingPriceConverterMap().get(childPs.getItemtype());
			if (!ObjectUtils.isEmpty(productSpecConverter))
			{
				childrenPsData.add(productSpecConverter.convert(childPs));
			}
		});

		target.setChildren(childrenPsData);
	}


	protected Map<String, Converter<TmaProductSpecificationModel, TmaProductSpecificationData>> getProductOfferingPriceConverterMap()
	{
		return productSpecificationConverterMap;
	}
}
