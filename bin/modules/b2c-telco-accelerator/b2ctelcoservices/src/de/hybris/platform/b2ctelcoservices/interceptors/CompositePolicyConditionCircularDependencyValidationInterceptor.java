/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaCompositePolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Validation Interceptor making sure that no circular dependency can be formed within a {@link TmaCompositePolicyConditionModel}.
 *
 * @since 1810
 */
public class CompositePolicyConditionCircularDependencyValidationInterceptor
		extends AbstractCircularDependencyValidationInterceptor<TmaPolicyConditionModel, TmaCompositePolicyConditionModel>
{
	@Override
	public void onValidate(final TmaCompositePolicyConditionModel modifiedCompositeCondition,
			final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		if (interceptorContext.isModified(modifiedCompositeCondition, TmaCompositePolicyConditionModel.CHILDREN))
		{
			validateModifiedItem(modifiedCompositeCondition);
		}

		if (interceptorContext.isModified(modifiedCompositeCondition, TmaCompositePolicyConditionModel.PARENTS))
		{
			validateModifiedParentItem(modifiedCompositeCondition);
		}
	}

	@Override
	public Set<TmaPolicyConditionModel> getChildren(final TmaCompositePolicyConditionModel modifiedType)
	{
		return modifiedType.getChildren();
	}

	@Override
	public Set<TmaCompositePolicyConditionModel> getParents(final TmaCompositePolicyConditionModel existingType)
	{
		return existingType.getParents();
	}

	@Override
	public Set<TmaCompositePolicyConditionModel> filterCompositeItem(final Set<TmaPolicyConditionModel> entries)
	{
		return CollectionUtils.isEmpty(entries) ? Collections.emptySet() : entries.stream()
				.filter(TmaCompositePolicyConditionModel.class::isInstance)
				.map(TmaCompositePolicyConditionModel.class::cast)
				.collect(Collectors.toSet());
	}

}
