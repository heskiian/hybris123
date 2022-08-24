/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.constraints;

import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingGroupModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.collections.CollectionUtils;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Validates the children of TmaProductOfferingGroup
 * {@link TmaProductOfferingGroupModel#getChildProductOfferings()}. Allows only those
 * Product Offerings to be added as a children of TmaProductOfferingGroup which are the children of its parent
 * TmaBundledProductOffering.
 *
 * @since 6.7
 */
public class TmaPoGroupImmediateChildrenValidator extends BasicConfigurableGuidedSellingValidator<TmaPoGroupImmediateChildren>
{
	@Override
	public boolean isValid(final ItemModel value, final ConstraintValidatorContext context)
	{
		validateParameterNotNull(value, "Validating object is null");
		TmaProductOfferingGroupModel tmaProductOfferingGroupModel = null;
		if (value instanceof TmaProductOfferingGroupModel)
		{
			tmaProductOfferingGroupModel = (TmaProductOfferingGroupModel) value;
		}
		return tmaProductOfferingGroupModel != null && tmaProductOfferingGroupModel.getChildProductOfferings() != null
				? isEntryValid(tmaProductOfferingGroupModel, context)
				: true;
	}

	/**
	 * Checks if the child offerings of TmaProductOfferingGroup are subset of the children of its parent
	 * TmaBundledProductOffering
	 *
	 * @param tmaProductOfferingGroupModel
	 * @param context
	 * @return
	 */
	private boolean isEntryValid(final TmaProductOfferingGroupModel tmaProductOfferingGroupModel,
			final ConstraintValidatorContext context)
	{
		final TmaBundledProductOfferingModel poParentBundleModel = tmaProductOfferingGroupModel.getParentBundleProductOffering();
		boolean isHavingValidPos = true;
		final Set<TmaProductOfferingModel> childOfferings = tmaProductOfferingGroupModel.getChildProductOfferings() != null
				? tmaProductOfferingGroupModel.getChildProductOfferings()
				: new HashSet<>();
		if (CollectionUtils.isNotEmpty(childOfferings) && poParentBundleModel != null
				&& CollectionUtils.isNotEmpty(poParentBundleModel.getChildren()))
		{
			isHavingValidPos = poParentBundleModel.getChildren().containsAll(childOfferings);
			buildErrorMessage(context, TmaProductOfferingGroupModel.CHILDPRODUCTOFFERINGS, tmaProductOfferingGroupModel.getCode(),
					poParentBundleModel.getCode());
		}
		return isHavingValidPos;
	}
}
