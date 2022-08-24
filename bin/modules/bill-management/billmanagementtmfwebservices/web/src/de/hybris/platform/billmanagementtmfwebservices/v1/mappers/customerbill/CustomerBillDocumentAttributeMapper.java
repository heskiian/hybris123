/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.billmanagementtmfwebservices.v1.mappers.customerbill;

import de.hybris.platform.billmanagementservices.model.BmPartyBillModel;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.AttachmentRefOrValue;
import de.hybris.platform.billmanagementtmfwebservices.v1.dto.CustomerBill;
import de.hybris.platform.billmanagementtmfwebservices.v1.mappers.BmAttributeMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for billDocument attribute between {@link BmPartyBillModel} and {@link CustomerBill}
 *
 * @since 2108
 */
public class CustomerBillDocumentAttributeMapper extends BmAttributeMapper<BmPartyBillModel, CustomerBill>
{
	private MapperFacade mapperFacade;

	public CustomerBillDocumentAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
	}

	@Override
	public void populateTargetAttributeFromSource(final BmPartyBillModel source, final CustomerBill target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getAttachments()))
		{
			return;
		}
		final List<AttachmentRefOrValue> attachmentRefOrValues = source.getAttachments().stream()
				.map(catalogUnawareMediaModel -> getMapperFacade().map(catalogUnawareMediaModel, AttachmentRefOrValue.class, context))
				.collect(Collectors.toList());
		target.setBillDocument(attachmentRefOrValues);
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}
}
