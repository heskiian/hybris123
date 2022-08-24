/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.RelatedPartyRef;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for relatedParty attribute between {@link BmPartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillRelatedPartyAttributeMapper extends BmAttributeMapper<BmPartyBillModel, CustomerBill>
{
	private MapperFacade mapperFacade;

	public CustomerBillRelatedPartyAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getParties()))
		{
			return;
		}

		final List<RelatedPartyRef> relatedPartyRefs = new ArrayList<>();
		source.getParties().forEach(bmPartyModel -> {
			if (!CollectionUtils.isEmpty(bmPartyModel.getPartyRoles()))
			{
				bmPartyModel.getPartyRoles().forEach(partyRoleModel -> {
					final RelatedPartyRef relatedPartyRef = getMapperFacade().map(partyRoleModel, RelatedPartyRef.class, context);
					relatedPartyRefs.add(relatedPartyRef);
				});
			}
		});

		target.setRelatedParty(relatedPartyRefs);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
