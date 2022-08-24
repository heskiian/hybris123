/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.services.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcoservices.daos.GenericSearchDao;
import de.hybris.platform.b2ctelcoservices.daos.TmaProductSpecCharactersticsValueDao;
import de.hybris.platform.b2ctelcoservices.daos.impl.DefaultGenericSearchDao;
import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecCharacteristicValueModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.services.TmaProductSpecificationService;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Default implementation of the {@link TmaProductSpecificationService}.
 *
 * @since 6.7
 */
public class DefaultTmaProductSpecificationService implements TmaProductSpecificationService
{
	private TmaProductSpecCharactersticsValueDao tmaProductSpecCharactersticsValueDao;
	private GenericSearchDao tmaProductSpecificationDao;

	public DefaultTmaProductSpecificationService(final DefaultGenericSearchDao tmaProductSpecificationDao)
	{
		this.tmaProductSpecificationDao = tmaProductSpecificationDao;
	}

	@Override
	public Set<TmaProductSpecCharacteristicModel> getProductSpecCharacteristicsForPsStructure(
			final TmaProductSpecificationModel productSpecification)
	{
		validateParameterNotNullStandardMessage(TmaProductSpecificationModel._TYPECODE, productSpecification);

		final Set<TmaProductSpecCharacteristicModel> productSpecCharacteristics = new HashSet<>();
		productSpecCharacteristics.addAll(productSpecification.getProductSpecCharacteristics());
		addProductSpecsCharacteristicsFromPsConfig(productSpecification, productSpecCharacteristics);

		return productSpecCharacteristics;
	}

	@Override
	public TmaProductSpecCharacteristicValueModel getTmaProductSpecCharacteristicValueModelForId(final String pscvId)
	{
		final List<TmaProductSpecCharacteristicValueModel> tmaProductSpecCharacteristicValueModels = getTmaProductSpecCharactersticsValueDao()
				.findPscvById(pscvId);
		if (CollectionUtils.isNotEmpty(tmaProductSpecCharacteristicValueModels))
		{
			return tmaProductSpecCharacteristicValueModels.get(0);
		}
		return null;
	}

	@Override
	public TmaProductSpecificationModel getProductSpecification(final String id)
	{
		validateParameterNotNull(id, "Product Specification Id should not be null");
		final Map<String, Object> parameters = new HashMap();
		parameters.put(TmaProductSpecificationModel.ID, id);
		return (TmaProductSpecificationModel) getTmaProductSpecificationDao().findUnique(parameters);
	}

	private void addProductSpecsCharacteristicsFromPsConfig(final TmaProductSpecificationModel productSpecification,
			final Set<TmaProductSpecCharacteristicModel> productSpecCharacteristics)
	{
		if (!isCompositeProductSpec(productSpecification))
		{
			return;
		}

		final TmaCompositeProductSpecificationModel compositeProductSpec = (TmaCompositeProductSpecificationModel) productSpecification;
		compositeProductSpec.getChildren().forEach(childProductSpec -> {
			productSpecCharacteristics.addAll(childProductSpec.getProductSpecCharacteristics());
			addProductSpecsCharacteristicsFromPsConfig(childProductSpec, productSpecCharacteristics);
		});
	}

	private boolean isCompositeProductSpec(final TmaProductSpecificationModel productSpecification)
	{
		return TmaCompositeProductSpecificationModel._TYPECODE.equals(productSpecification.getItemtype());
	}


	protected TmaProductSpecCharactersticsValueDao getTmaProductSpecCharactersticsValueDao()
	{
		return tmaProductSpecCharactersticsValueDao;
	}

	@Required
	public void setTmaProductSpecCharactersticsValueDao(
			final TmaProductSpecCharactersticsValueDao tmaProductSpecCharactersticsValueDao)
	{
		this.tmaProductSpecCharactersticsValueDao = tmaProductSpecCharactersticsValueDao;
	}

	protected GenericSearchDao getTmaProductSpecificationDao()
	{
		return tmaProductSpecificationDao;
	}
}
