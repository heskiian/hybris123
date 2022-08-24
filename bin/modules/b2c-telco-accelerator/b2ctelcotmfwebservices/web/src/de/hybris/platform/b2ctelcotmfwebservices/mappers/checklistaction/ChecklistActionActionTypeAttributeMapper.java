/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.mappers.checklistaction;


import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionData;
import de.hybris.platform.b2ctelcofacades.mappers.TmaAttributeMapper;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ChecklistAction;


import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for actionType attribute between {@link TmaChecklistActionData} and
 * {@link ChecklistAction}
 *
 * @since 1907
 */
public class ChecklistActionActionTypeAttributeMapper extends TmaAttributeMapper<TmaChecklistActionData, ChecklistAction>
{

	@Override
	public void populateTargetAttributeFromSource(TmaChecklistActionData source, ChecklistAction target, MappingContext context)
	{
		if (source.getActionType() != null)
		{
			target.setActionType(source.getActionType().getCode());
		}
	}
}
