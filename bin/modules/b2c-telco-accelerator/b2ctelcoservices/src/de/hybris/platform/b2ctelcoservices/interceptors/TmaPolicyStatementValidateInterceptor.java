/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaPolicyStatementModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.type.TypeService;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Required;


/**
 * Interceptor to validate the minimum and maximum cardinality configured for a {@link TmaPolicyStatementModel}.
 *
 * @since 6.7
 */
public class TmaPolicyStatementValidateInterceptor implements ValidateInterceptor<TmaPolicyStatementModel>
{
	private TypeService typeService;

	@Override
	public void onValidate(TmaPolicyStatementModel policyStatementModel, InterceptorContext interceptorContext)
			throws InterceptorException
	{
		final Integer minValue = policyStatementModel.getMin();
		final Integer maxValue = policyStatementModel.getMax();

		final AttributeDescriptorModel attributeDescriptorModel = getTypeService().getAttributeDescriptor(policyStatementModel
				.getItemModelContext().getItemType(), "min");

		if (!attributeDescriptorModel.getOptional() && minValue == null)
		{
			throw new InterceptorException(MessageFormat
					.format("A minimum cardinality value is missing for {0} policy rule and it needs to be configured",
							policyStatementModel.getCode()), this);
		}

		if (isLessThanZero(minValue) || isLessThanZero(maxValue))
		{
			throw new InterceptorException("Min or max cardinality values cannot be less than 0.", this);
		}

		if (maxValue != null && minValue != null && minValue > maxValue)
		{
			throw new InterceptorException("Min cardinality value can take values between 0 and max cardinality value", this);
		}

	}

	protected boolean isLessThanZero(Integer value)
	{
		return value != null && value < 0;
	}

	protected TypeService getTypeService()
	{
		return typeService;
	}

	@Required
	public void setTypeService(TypeService typeService)
	{
		this.typeService = typeService;
	}
}
