/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaAtomicPolicyConditionModel;
import de.hybris.platform.b2ctelcoservices.model.TmaPolicyConditionModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.text.MessageFormat;


/**
 * Interceptor to validate that no instances of {@link TmaPolicyConditionModel} can be created. Due to backwards compatibility
 * constraints, the {@link TmaPolicyConditionModel} cannot be made abstract and therefore custom interceptor is required.
 *
 * @since 1810
 */
public class TmaPolicyConditionValidateInterceptor implements ValidateInterceptor<TmaPolicyConditionModel>
{
	@Override
	public void onValidate(TmaPolicyConditionModel policyConditionModel, InterceptorContext interceptorContext)
			throws InterceptorException
	{
		if (TmaPolicyConditionModel._TYPECODE.equals(policyConditionModel.getItemtype()))
		{
			throw new InterceptorException(
					MessageFormat.format("{0} instances cannot be created. Define instead {1} instances.", TmaPolicyConditionModel._TYPECODE,
							TmaAtomicPolicyConditionModel._TYPECODE),this);
		}
	}
}
