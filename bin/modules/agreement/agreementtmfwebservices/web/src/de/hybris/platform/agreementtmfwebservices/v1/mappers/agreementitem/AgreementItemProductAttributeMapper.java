/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.mappers.agreementitem;

import de.hybris.platform.agreementservices.model.AgrAgreementItemModel;
import de.hybris.platform.agreementservices.model.AgrProductModel;
import de.hybris.platform.agreementservices.services.AgrGenericService;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementItem;
import de.hybris.platform.agreementtmfwebservices.v1.dto.ProductRef;
import de.hybris.platform.agreementtmfwebservices.v1.mappers.AgrAttributeMapper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for product attribute between {@link AgrAgreementItemModel} and {@link AgreementItem}
 *
 * @since 2108
 */
public class AgreementItemProductAttributeMapper extends AgrAttributeMapper<AgrAgreementItemModel, AgreementItem>
{
	private MapperFacade mapperFacade;
	private AgrGenericService agrGenericService;

	public AgreementItemProductAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
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
		if (ObjectUtils.isEmpty(source.getProducts()))
		{
			return;
		}

		final List<ProductRef> productRefList = new ArrayList<>();
		productRefList.add(getMapperFacade().map(source.getProducts(), ProductRef.class, context));

		target.setProduct(productRefList);
	}

	@Override
	public void populateSourceAttributeFromTarget(final AgreementItem target, final AgrAgreementItemModel source,
			final MappingContext context)
	{
		if (CollectionUtils.isEmpty(target.getProduct()))
		{
			return;
		}

		final ProductRef productRef = target.getProduct().get(0);
		AgrProductModel productModel = (AgrProductModel) getAgrGenericService()
				.getItem(AgrProductModel._TYPECODE, productRef.getId());
		if (productModel == null)
		{
			productModel = (AgrProductModel) getAgrGenericService()
					.createItem(AgrProductModel.class);
		}
		getMapperFacade().map(productRef, productModel, context);
		getAgrGenericService().saveItem(productModel);

		source.setProducts(productModel);
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
