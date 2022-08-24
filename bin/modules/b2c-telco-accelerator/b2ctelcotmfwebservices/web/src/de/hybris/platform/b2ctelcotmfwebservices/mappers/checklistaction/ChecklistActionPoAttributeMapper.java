/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.checklistaction;

import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ChecklistAction;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductOfferingRef;
import org.springframework.beans.factory.annotation.Required;

import java.util.ArrayList;
import java.util.List;


/**
 * This attribute Mapper class maps data for productOffering attribute between {@link TmaChecklistActionData} and
 * {@link ChecklistAction}
 *
 * @since 1907
 */
public class ChecklistActionPoAttributeMapper extends TmaAttributeMapper<TmaChecklistActionData, ChecklistAction>
{
	/**
	 * Mapper facade
	 */
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(TmaChecklistActionData source, ChecklistAction target, MappingContext context)
	{
		if (source.getProductOfferings() == null)
		{
			return;
		}

		final List<ProductOfferingRef> productOfferingRefList = new ArrayList<>();
		source.getProductOfferings().forEach(productData ->
		{
			final ProductOfferingRef productOfferingRef = getMapperFacade()
					.map(productData, ProductOfferingRef.class, context);
			productOfferingRefList.add(productOfferingRef);
		});

		target.setProductOffering(productOfferingRefList);
	}

	public MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	@Required
	public void setMapperFacade(MapperFacade mapperFacade)
	{
		this.mapperFacade = mapperFacade;
	}
}
