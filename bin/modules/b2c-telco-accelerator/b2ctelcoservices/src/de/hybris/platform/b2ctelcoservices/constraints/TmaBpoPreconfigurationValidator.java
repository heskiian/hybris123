/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.constraints;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.core.model.ItemModel;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.ConstraintValidatorContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Validates the {@link TmaSimpleProductOfferingModel} used in creating a new {@link TmaBundledProductOfferingModel}
 * The method checks if the
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel#getPreConfigSpos()} are valid child of the Root
 * Bundle selected.
 * {@link de.hybris.platform.b2ctelcoservices.model.TmaBpoPreConfigModel#getRootBpo()}.
 *
 * @since 6.7
 */
public class TmaBpoPreconfigurationValidator extends BasicConfigurableGuidedSellingValidator<TmaBpoPreConfiguration>
{
	@Override
	public boolean isValid(final ItemModel value, final ConstraintValidatorContext context)
	{
		validateParameterNotNull(value, "Validating object is null");
		TmaBpoPreConfigModel tmaBpoPreConfigModel = null;
		if (value instanceof TmaBpoPreConfigModel)
		{
			tmaBpoPreConfigModel = (TmaBpoPreConfigModel) value;
		}
		return ((tmaBpoPreConfigModel != null) && (isEntryValid(tmaBpoPreConfigModel, context)));
	}

	private boolean isEntryValid(final TmaBpoPreConfigModel tmaBpoPreConfigModel, final ConstraintValidatorContext context)
	{
		final Set<TmaSimpleProductOfferingModel> rootBpoProductOfferingList = this
				.getEntireSpoListForRootBpo(tmaBpoPreConfigModel.getRootBpo(), new LinkedHashSet<>());

		return CollectionUtils.isNotEmpty(tmaBpoPreConfigModel.getPreConfigSpos())
				&& CollectionUtils.isNotEmpty(rootBpoProductOfferingList)
				&& (rootBpoProductOfferingList.containsAll(tmaBpoPreConfigModel.getPreConfigSpos())
				&& this.checkIfCatalogVersionAreValid(tmaBpoPreConfigModel)
				? true
				: this.buildErrorMessageAndReturn(context, tmaBpoPreConfigModel.getCode()));
	}

	private boolean checkIfCatalogVersionAreValid(final TmaBpoPreConfigModel tmaBpoPreConfigModel)
	{
		if (!StringUtils.equalsIgnoreCase(getPreConfigCatalog(tmaBpoPreConfigModel),
				tmaBpoPreConfigModel.getRootBpo().getCatalogVersion().getPk().toString()))
		{
			return false;
		}
		for (final TmaSimpleProductOfferingModel spo : tmaBpoPreConfigModel.getPreConfigSpos())
		{
			if (!StringUtils.equalsIgnoreCase(spo.getCatalogVersion().getPk().toString(),
					getPreConfigCatalog(tmaBpoPreConfigModel)))
			{
				return false;
			}
		}
		return true;
	}

	private String getPreConfigCatalog(final TmaBpoPreConfigModel tmaBpoPreConfigModel)
	{
		return tmaBpoPreConfigModel.getCatalogVersion().getPk().toString();
	}

	private Set<TmaSimpleProductOfferingModel> getEntireSpoListForRootBpo(
			final TmaBundledProductOfferingModel bundledProductOfferingModel,
			final Set<TmaSimpleProductOfferingModel> rootBpoProductOfferingList)
	{
		final Set<TmaProductOfferingModel> rootBpoChildrenList = bundledProductOfferingModel.getChildren();
		for (final TmaProductOfferingModel productOffering : rootBpoChildrenList)
		{
			if (productOffering instanceof TmaSimpleProductOfferingModel)
			{
				rootBpoProductOfferingList.add((TmaSimpleProductOfferingModel) productOffering);
			}
			else
			{
				getEntireSpoListForRootBpo((TmaBundledProductOfferingModel) productOffering, rootBpoProductOfferingList);
			}
		}
		return rootBpoProductOfferingList;
	}

	private boolean buildErrorMessageAndReturn(final ConstraintValidatorContext context, final String code)
	{
		buildErrorMessage(context, TmaBpoPreConfigModel.PRECONFIGSPOS, code);
		return false;
	}

}
