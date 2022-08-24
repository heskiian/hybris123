/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaCompositeProductSpecificationModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductSpecificationModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;


/**
 * Validation Interceptor making sure that no circular dependency can be formed within a {@link TmaCompositeProductSpecificationModel}.
 *
 * @since 6.7
 */
public class CpsCircularDependencyValidationInterceptor
		extends AbstractCircularDependencyValidationInterceptor<TmaProductSpecificationModel, TmaCompositeProductSpecificationModel>
{

	@Override
	public void onValidate(TmaCompositeProductSpecificationModel modifiedCps,
			InterceptorContext interceptorContext) throws InterceptorException
	{
		if (interceptorContext.isModified(modifiedCps, TmaCompositeProductSpecificationModel.CHILDREN))
		{
			validateModifiedItem(modifiedCps);
		}

		if (interceptorContext.isModified(modifiedCps, TmaCompositeProductSpecificationModel.PARENTS))
		{
			validateModifiedParentItem(modifiedCps);
		}
	}

	@Override
	public Set<TmaProductSpecificationModel> getChildren(TmaCompositeProductSpecificationModel modifiedType)
	{
		return modifiedType.getChildren();
	}

	@Override
	public Set<TmaCompositeProductSpecificationModel> getParents(
			TmaCompositeProductSpecificationModel existingType)
	{
		return existingType.getParents();
	}

	@Override
	public Set<TmaCompositeProductSpecificationModel> filterCompositeItem(
			Set<TmaProductSpecificationModel> entries)
	{
		return CollectionUtils.isEmpty(entries) ? Collections.emptySet() : entries.stream()
				.filter(TmaCompositeProductSpecificationModel.class::isInstance)
				.map(TmaCompositeProductSpecificationModel.class::cast)
				.collect(Collectors.toSet());
	}
}
