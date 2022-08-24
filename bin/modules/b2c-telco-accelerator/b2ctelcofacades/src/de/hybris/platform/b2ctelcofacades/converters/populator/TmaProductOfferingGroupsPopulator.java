/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaOfferingGroupData;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;


/**
 * Populates {@link ProductData} with the offering group configured on {@link TmaProductOfferingModel}.
 *
 * @since 1903
 */

public class TmaProductOfferingGroupsPopulator implements Populator<TmaProductOfferingModel, ProductData>
{
	private Converter<TmaProductOfferingGroupModel, TmaOfferingGroupData> offeringGroupConverter;

	@Override
	public void populate(final TmaProductOfferingModel productOfferingModel, ProductData productData)
			throws ConversionException
	{
		if (!(productOfferingModel instanceof TmaBundledProductOfferingModel))
		{
			return;
		}
		List<TmaProductOfferingGroupModel> offeringGroups = ((TmaBundledProductOfferingModel) productOfferingModel)
				.getProductOfferingGroups();
		productData.setOfferingGroups(getOfferingGroupDataList(offeringGroups));

	}

	private List<TmaOfferingGroupData> getOfferingGroupDataList(List<TmaProductOfferingGroupModel> offeringGroups)
	{
		final List<TmaOfferingGroupData> offeringGroupDataList = new ArrayList<>();
		if (CollectionUtils.isEmpty(offeringGroups))
		{
			return offeringGroupDataList;
		}
		for (TmaProductOfferingGroupModel group : offeringGroups)
		{
			TmaOfferingGroupData groupData = new TmaOfferingGroupData();
			offeringGroupDataList.add(getOfferingGroupConverter().convert(group, groupData));
		}
		return offeringGroupDataList;
	}

	protected Converter<TmaProductOfferingGroupModel, TmaOfferingGroupData> getOfferingGroupConverter()
	{
		return offeringGroupConverter;
	}

	public void setOfferingGroupConverter(
			Converter<TmaProductOfferingGroupModel, TmaOfferingGroupData> offeringGroupConverter)
	{
		this.offeringGroupConverter = offeringGroupConverter;
	}
}
