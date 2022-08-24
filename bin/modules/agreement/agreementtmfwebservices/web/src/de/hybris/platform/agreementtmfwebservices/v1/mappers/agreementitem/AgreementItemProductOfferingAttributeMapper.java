/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementitem;

import de.hybris.platform.agreementservices.model.AgrAgreementItemModel;
import de.hybris.platform.agreementservices.model.AgrProductOfferingModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementItem;
import de.hybris.platform.agreementtmfwebservices.v1.dto.ProductOfferingRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for product offering attribute between {@link AgrAgreementItemModel} and {@link AgreementItem}
 *
 * @since 2108
 */
public class AgreementItemProductOfferingAttributeMapper extends AgrAttributeMapper<AgrAgreementItemModel, AgreementItem>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementItemProductOfferingAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final AgrGenericService agrGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.agrGenericService = agrGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final AgrAgreementItemModel source, final AgreementItem target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getProductOfferings()))
		{
			return;
		}

		final List<ProductOfferingRef> productOfferingRefList = new ArrayList<>();
		productOfferingRefList.add(getMapperFacade().map(source.getProductOfferings(), ProductOfferingRef.class, context));

		target.setProductOffering(productOfferingRefList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementItem target, final AgrAgreementItemModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getProductOffering()))
		{
			return;
		}

		final ProductOfferingRef productOfferingRef = target.getProductOffering().get(0);
		AgrProductOfferingModel productOfferingModel = (AgrProductOfferingModel) getAgrGenericService()
				.getItem(AgrProductOfferingModel._TYPECODE, productOfferingRef.getId());
		if (productOfferingModel == null)
		{
			productOfferingModel = (AgrProductOfferingModel) getAgrGenericService()
					.createItem(AgrProductOfferingModel.class);
		}
		getMapperFacade().map(productOfferingRef, productOfferingModel, context);
		getAgrGenericService().saveItem(productOfferingModel);

		source.setProductOfferings(productOfferingModel);

	}

	protected MapperFacade getMapperFacade()
	{
		return mapperFacade;
	}

	protected AgrGenericService getAgrGenericService()
	{
		return agrGenericService;
	}
}
