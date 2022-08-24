/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;


/**
 * Validation Interceptor making sure that no circular dependency can be formed within a {@link TmaBundledProductOfferingModel}.
 *
 * @since 6.7
 */
public class BpoCircularDependencyValidationInterceptor extends
		AbstractCircularDependencyValidationInterceptor<TmaProductOfferingModel, TmaBundledProductOfferingModel>
{

	@Override
	public void onValidate(final TmaBundledProductOfferingModel modifiedBpo, final InterceptorContext interceptorContext)
			throws InterceptorException
	{
		if (interceptorContext.isModified(modifiedBpo, TmaBundledProductOfferingModel.CHILDREN))
		{
			validateModifiedItem(modifiedBpo);
		}

		if (interceptorContext.isModified(modifiedBpo, TmaBundledProductOfferingModel.PARENTS))
		{
			validateModifiedParentItem(modifiedBpo);
		}
	}

	@Override
	public Set<TmaProductOfferingModel> getChildren(final TmaBundledProductOfferingModel modifiedType)
	{
		return modifiedType.getChildren();
	}

	@Override
	public Set<TmaBundledProductOfferingModel> getParents(final TmaBundledProductOfferingModel existingType)
	{
		return existingType.getParents();
	}

	@Override
	public Set<TmaBundledProductOfferingModel> filterCompositeItem(
			Set<TmaProductOfferingModel> entries)
	{
		return CollectionUtils.isEmpty(entries) ? Collections.emptySet() : entries.stream()
				.filter(TmaBundledProductOfferingModel.class::isInstance)
				.map(TmaBundledProductOfferingModel.class::cast)
				.collect(Collectors.toSet());
	}
}
