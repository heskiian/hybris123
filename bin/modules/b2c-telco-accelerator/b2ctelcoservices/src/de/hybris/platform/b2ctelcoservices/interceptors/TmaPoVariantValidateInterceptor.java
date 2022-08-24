/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;

import de.hybris.platform.b2ctelcoservices.model.TmaPoVariantModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSimpleProductOfferingModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.i18n.L10NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.variants.model.VariantCategoryModel;
import de.hybris.platform.variants.model.VariantValueCategoryModel;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Validation Interceptor for {@link TmaPoVariantModel}.
 * It validates if the product offering variant has a base product and if each VariantCategory defined on the base product has a
 * corresponding VariantValueCategory defined on the variant.
 *
 * @since 1810
 */
public class TmaPoVariantValidateInterceptor implements ValidateInterceptor<TmaPoVariantModel>
{
	protected static final String CATALOG_SYNC_ACTIVE_ATTRIBUTE = "catalog.sync.active";
	private L10NService l10NService;
	private SessionService sessionService;

	@Override
	public void onValidate(final TmaPoVariantModel poVariant, final InterceptorContext ctx) throws InterceptorException
	{
		final Boolean isSyncActive = this.sessionService.getCurrentSession().getAttribute(CATALOG_SYNC_ACTIVE_ATTRIBUTE);
		if (BooleanUtils.isNotTrue(isSyncActive))
		{
			final Collection<CategoryModel> variantValueCategories = poVariant.getSupercategories();
			if (CollectionUtils.isEmpty(variantValueCategories))
			{
				throw new InterceptorException(this.localizeForKey("error.tmapovariant.wrongsupercategory"));
			}
			this.validateBaseProductSuperCategories(poVariant, variantValueCategories);
		}
	}

	protected void validateBaseProductSuperCategories(final TmaPoVariantModel poVariant,
			final Collection<CategoryModel> variantValueCategories) throws InterceptorException
	{
		final TmaSimpleProductOfferingModel basePo = poVariant.getTmaBasePo();
		if (basePo == null)
		{
			throw new InterceptorException(this.getL10NService()
					.getLocalizedString("error.tmapovariant.nobaseproduct", new Object[] { poVariant.getCode() }));
		}

		final Collection<CategoryModel> superCategories = basePo.getSupercategories();
		if (CollectionUtils.isEmpty(superCategories))
		{
			return;
		}

		final Set<CategoryModel> variantCategoriesOfVariantValueCategories = getVariantCategoriesOfVariantValueCategories(
				variantValueCategories);
		final Collection<CategoryModel> baseVariantCategories = superCategories.stream()
				.filter(c -> c instanceof VariantCategoryModel).collect(Collectors.toList());

		if (baseVariantCategories.size() != variantCategoriesOfVariantValueCategories.size())
		{
			throw new InterceptorException(this.getL10NService()
					.getLocalizedString("error.tmapovariant.nosameamountofvariantcategories",
							new Object[] { poVariant.getCode(),
									(variantCategoriesOfVariantValueCategories).size(), basePo.getCode(),
									baseVariantCategories.size() }));
		}

		final Iterator variantCategoriesIterator = variantCategoriesOfVariantValueCategories.iterator();
		while (variantCategoriesIterator.hasNext())
		{
			final CategoryModel variantCategory = (CategoryModel) variantCategoriesIterator.next();
			if (!baseVariantCategories.contains(variantCategory))
			{
				throw new InterceptorException(this.getL10NService()
						.getLocalizedString("error.tmapovariant.variantcategorynotinbaseproduct",
								new Object[] { variantCategory.getCode(), poVariant.getCode(),
										basePo.getCode() }));
			}
		}
	}

	private Set<CategoryModel> getVariantCategoriesOfVariantValueCategories(final Collection<CategoryModel> variantValueCategories)
	{
		Set<CategoryModel> variantCategoriesOfVariantValueCategories = new HashSet<>();
		if (CollectionUtils.isNotEmpty(variantValueCategories))
		{
			variantCategoriesOfVariantValueCategories = variantValueCategories.stream()
					.filter(v -> v instanceof VariantValueCategoryModel)
					.flatMap(v -> v.getSupercategories().stream())
					.filter(c -> c instanceof VariantCategoryModel)
					.collect(Collectors.toSet());
		}
		return variantCategoriesOfVariantValueCategories;
	}

	private String localizeForKey(final String key)
	{
		return this.getL10NService().getLocalizedString(key);
	}

	protected L10NService getL10NService()
	{
		return this.l10NService;
	}

	@Required public void setL10NService(final L10NService l10NService)
	{
		this.l10NService = l10NService;
	}

	protected SessionService getSessionService()
	{
		return this.sessionService;
	}

	@Required public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}
}
