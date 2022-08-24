/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.contact;

import de.hybris.platform.billingaccountservices.model.BaContactMediumModel;
import de.hybris.platform.billingaccountservices.model.BaContactModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Contact;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.ContactMedium;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for contact mediums attribute between {@link BaContactModel} and {@link Contact}
 *
 * @since 2105
 */
public class ContactContMediumAttributeMapper extends BaAttributeMapper<BaContactModel, Contact>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public ContactContMediumAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaContactModel source, final Contact target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getContactMediums()))
		{
			return;
		}

		final List<ContactMedium> contactMediumList = source.getContactMediums().stream()
				.map(contactMediumModel -> getMapperFacade().map(contactMediumModel, ContactMedium.class, context))
				.collect(Collectors.toList());

		target.setContactMedium(contactMediumList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final Contact target, final BaContactModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getContactMedium()))
		{
			return;
		}

		source.setContactMediums(getAllContactMediums(target.getContactMedium(), context));
	}

	private Set<BaContactMediumModel> getAllContactMediums(final List<ContactMedium> contactMediumList,
			final MappingContext context)
	{
		final Set<BaContactMediumModel> result = new HashSet<>();
		contactMediumList.forEach(contactMedium -> {
			final BaContactMediumModel baContactMediumModel = (BaContactMediumModel) getBaGenericService()
					.createItem(BaContactMediumModel.class);
			getMapperFacade().map(contactMedium, baContactMediumModel, context);
			result.add(baContactMediumModel);
		});
		return result;
	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected BaGenericService getBaGenericService()
	{
		return baGenericService;
	}
}
