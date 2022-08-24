/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billingaccount;

import de.hybris.platform.billingaccountservices.model.BaBillingAccountModel;
import de.hybris.platform.billingaccountservices.model.BaContactModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingAccount;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.Contact;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for contact attribute between {@link BaBillingAccountModel} and {@link BillingAccount}
 *
 * @since 2105
 */
public class BillingAccountContactAttributeMapper extends BaAttributeMapper<BaBillingAccountModel, BillingAccount>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillingAccountContactAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillingAccountModel source, final BillingAccount target,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(source.getContacts()))
		{
			return;
		}

		final List<Contact> contactList = source.getContacts().stream()
				.map(contactModel -> getMapperFacade().map(contactModel, Contact.class, context))
				.collect(Collectors.toList());

		target.setContact(contactList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillingAccount target, final BaBillingAccountModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getContact()))
		{
			return;
		}

		source.setContacts(getAllAccountContacts(target.getContact(), context));
	}

	private Set<BaContactModel> getAllAccountContacts(final List<Contact> contacts, final MappingContext context)
	{
		final Set<BaContactModel> result = new HashSet<>();
		contacts.forEach(contact -> {
			final BaContactModel baContactModel = (BaContactModel) getBaGenericService()
					.createItem(BaContactModel.class);
			getMapperFacade().map(contact, baContactModel, context);
			result.add(baContactModel);
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
