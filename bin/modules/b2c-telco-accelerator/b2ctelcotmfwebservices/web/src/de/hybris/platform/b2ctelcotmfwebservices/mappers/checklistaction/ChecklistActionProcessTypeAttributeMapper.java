/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.checklistaction;

import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionData;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ChecklistAction;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ProcessType;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for processType attribute between {@link TmaChecklistActionData} and
 * {@link ChecklistAction}
 *
 * @since 1907
 */
public class ChecklistActionProcessTypeAttributeMapper extends TmaAttributeMapper<TmaChecklistActionData, ChecklistAction>
{
	private MapperFacade mapperFacade;

	@Override
	public void populateTargetAttributeFromSource(TmaChecklistActionData source, ChecklistAction target, MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getProcessTypes()))
		{
			return;
		}
		List<ProcessType> processTypeList=new ArrayList<>();
		for(TmaProcessType processType : source.getProcessTypes())
		{
			processTypeList.add(getMapperFacade().map(processType, ProcessType .class));
		}
		target.setProcessType(processTypeList);
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
