/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.checklistaction;

import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ChecklistAction;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProductSpecCharacteristicRef;

import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for productSpecCharacteristicRef attribute between {@link TmaChecklistActionData} and
 * {@link ChecklistAction}
 *
 * @since 1907
 */
public class ChecklistActionPscRefAttributeMapper extends TmaAttributeMapper<TmaChecklistActionData, ChecklistAction>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(TmaChecklistActionData source, ChecklistAction target, MappingContext context)
	{

		if (source.getCharacteristic() != null)
		{
			target.setProductSpecCharacteristic(
					getMapperFacade().map(source.getCharacteristic(), ProductSpecCharacteristicRef.class));
		}
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
