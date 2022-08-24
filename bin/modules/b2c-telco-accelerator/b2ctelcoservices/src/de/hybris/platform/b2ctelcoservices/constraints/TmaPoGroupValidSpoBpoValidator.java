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

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;


/**
 * Validates the children of TmaProductOfferingGroup
 * {@link TmaProductOfferingGroupModel#getChildProductOfferings()}. The
 * TmaProductOfferingGroup can either have all Simple Product Offering or all Bundled Product Offering.
 *
 * @since 6.7
 */
public class TmaPoGroupValidSpoBpoValidator extends BasicConfigurableGuidedSellingValidator<TmaPoGroupValidSpoBpo>
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
	 * Checks if the child offerings of TmaProductOfferingGroup are entirely consisting of either Simple Product Offering or
	 * Bundled Product Offering.
	 *
	 * @param tmaProductOfferingGroupModel
	 * @param context
	 * @return
	 */
	private boolean isEntryValid(final TmaProductOfferingGroupModel tmaProductOfferingGroupModel,
			final ConstraintValidatorContext context)
	{
		final TmaBundledProductOfferingModel parentBpo = tmaProductOfferingGroupModel.getParentBundleProductOffering();
		final Set<TmaProductOfferingModel> existingParentPoList = parentBpo != null ? parentBpo.getChildren() : new HashSet<>();
		final Set<TmaBundledProductOfferingModel> bpoAvailable = new HashSet<>();
		final Set<TmaProductOfferingModel> spoAvailable = new HashSet<>();

		for (final TmaProductOfferingModel tmaProductOfferingModel : existingParentPoList)
		{
			if (tmaProductOfferingModel instanceof TmaBundledProductOfferingModel)
			{
				bpoAvailable.add((TmaBundledProductOfferingModel) tmaProductOfferingModel);
			}
			else
			{
				spoAvailable.add(tmaProductOfferingModel);
			}
		}
		return parentBpo != null ? isEntryValid(tmaProductOfferingGroupModel, context, parentBpo, bpoAvailable, spoAvailable)
				: true;
	}

	private boolean isEntryValid(final TmaProductOfferingGroupModel tmaProductOfferingGroupModel,
			final ConstraintValidatorContext context, final TmaBundledProductOfferingModel poParentBundleModel,
			final Set<TmaBundledProductOfferingModel> bpoAvailable, final Set<TmaProductOfferingModel> spoAvailable)
	{

		return bpoAvailable.containsAll(tmaProductOfferingGroupModel.getChildProductOfferings())
				|| spoAvailable.containsAll(tmaProductOfferingGroupModel.getChildProductOfferings()) ? true
				: buildErrorAndReturn(context, tmaProductOfferingGroupModel.getCode(), poParentBundleModel.getCode());

	}

	private boolean buildErrorAndReturn(final ConstraintValidatorContext context, final String tmaPoGroupCode,
			final String poParentBundleModelCode)
	{
		buildErrorMessage(context, context.getDefaultConstraintMessageTemplate(), tmaPoGroupCode, poParentBundleModelCode);
		return false;
	}
}
