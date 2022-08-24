/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaChecklistPolicyStatementModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;

import java.text.MessageFormat;


/**
 * Interceptor to validate that no instances of {@link TmaChecklistPolicyStatementModel} can be created. Due to backwards
 * compatibility constraints, the {@link TmaChecklistPolicyStatementModel} cannot be made abstract and therefore custom interceptor
 * is required.
 *
 * @since 1911
 */
public class TmaChecklistPolicyStatementValidateInterceptor implements ValidateInterceptor<TmaChecklistPolicyStatementModel>
{

	@Override
	public void onValidate(TmaChecklistPolicyStatementModel tmaChecklistPolicyStatement,
			InterceptorContext interceptorContext) throws InterceptorException
	{
		if (TmaChecklistPolicyStatementModel._TYPECODE.equals(tmaChecklistPolicyStatement.getItemtype()))
		{
			throw new InterceptorException(
					MessageFormat.format("{0} instances cannot be created. Define instead child instances.",
							TmaChecklistPolicyStatementModel._TYPECODE), this);
		}
	}
}
