/*
 * Copyright (c)  2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.billingaccounttmfwebservices.v1.mappers.billstructure;

import de.hybris.platform.billingaccountservices.model.BaBillStructureModel;
import de.hybris.platform.billingaccountservices.model.BaBillingCycleSpecificationModel;
import de.hybris.platform.billingaccountservices.services.BaGenericService;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillStructure;
import de.hybris.platform.billingaccounttmfwebservices.v1.dto.BillingCycleSpecificationRefOrValue;
import de.hybris.platform.billingaccounttmfwebservices.v1.mappers.BaAttributeMapper;

import org.springframework.util.ObjectUtils;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;


/**
 * This attribute Mapper class maps data for cycleSpecification attribute between
 * {@link BaBillStructureModel} and {@link BillStructure}
 *
 * @since 2105
 */
public class BillStructureCycleSpecAttributeMapper extends BaAttributeMapper<BaBillStructureModel, BillStructure>
{
	private MapperFacade mapperFacade;
	private BaGenericService baGenericService;

	public BillStructureCycleSpecAttributeMapper(final String sourceAttributeName, final String targetAttributeName,
			final MapperFacade mapperFacade, final BaGenericService baGenericService)
	{
		super(sourceAttributeName, targetAttributeName);
		this.mapperFacade = mapperFacade;
		this.baGenericService = baGenericService;
	}

	@Override
	public void populateTargetAttributeFromSource(final BaBillStructureModel source, final BillStructure target,
			final MappingContext context)
	{
		if (ObjectUtils.isEmpty(source.getCycleSpecification()))
		{
			return;
		}
		target.setCycleSpecification(
				getMapperFacade().map(source.getCycleSpecification(), BillingCycleSpecificationRefOrValue.class, context));
	}

	@Override
	public void populateSourceAttributeFromTarget(final BillStructure target, final BaBillStructureModel source,
			final MappingContext context)
	{
		if (target.getCycleSpecification() == null)
		{
			return;
		}
		source.setCycleSpecification(getBillCycleSpecification(target.getCycleSpecification(), context));
	}

	private BaBillingCycleSpecificationModel getBillCycleSpecification(
			final BillingCycleSpecificationRefOrValue billingCycleSpecificationRefOrValue, final MappingContext context)
	{
		BaBillingCycleSpecificationModel billingCycleSpecificationModel = (BaBillingCycleSpecificationModel) getBaGenericService()
				.getItem(BaBillingCycleSpecificationModel._TYPECODE, billingCycleSpecificationRefOrValue.getId());
		if (billingCycleSpecificationModel == null)
		{
			billingCycleSpecificationModel = (BaBillingCycleSpecificationModel) getBaGenericService()
					.createItem(BaBillingCycleSpecificationModel.class);
		}
		getMapperFacade().map(billingCycleSpecificationRefOrValue, billingCycleSpecificationModel, context);
		getBaGenericService().saveItem(billingCycleSpecificationModel);
		return billingCycleSpecificationModel;
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
