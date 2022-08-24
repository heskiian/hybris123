/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.product.impl;

import de.hybris.platform.b2ctelcofacades.product.TmaProductFacade;
import de.hybris.platform.b2ctelcoservices.model.TmaBundledProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.model.TmaProductOfferingModel;
import de.hybris.platform.b2ctelcoservices.services.TmaCustomerReviewService;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.product.data.ReviewData;
import de.hybris.platform.commercefacades.product.impl.DefaultProductFacade;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;


/**
 * Default implementation for {@link TmaProductFacade}.
 *
 * @since 1907
 */
public class DefaultTmaProductFacade extends DefaultProductFacade implements TmaProductFacade
{

	private TmaPoService tmaPoService;

	public DefaultTmaProductFacade(TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	@Override
	public List<ReviewData> getReviewsByLimitAndOffset(String productCode, Integer offset, Integer limit)
			throws UnknownIdentifierException
	{
		final ProductModel product = getProductService().getProductForCode(productCode);
		final List<CustomerReviewModel> reviews = getCustomerReviewService()
				.getReviewsForProductByLimitOffsetAndLanguage(product, offset,
						limit, getCommonI18NService().getCurrentLanguage());

		if (reviews == null)
		{
			return Collections.emptyList();
		}

		List<ReviewData> reviewDataList = getCustomerReviewConverter().convertAll(reviews);

		for (ReviewData reviewData : reviewDataList)
		{
			final ProductData productData = new ProductData();
			productData.setCode(productCode);
			reviewData.setProduct(productData);
		}

		return reviewDataList;
	}

	@Override
	public ReviewData createReview(String productCode, String userId, ReviewData reviewData)
	{
		Assert.notNull(productCode, "Parameter productCode cannot be null.");
		Assert.notNull(userId, "Parameter userId cannot be null.");
		Assert.notNull(reviewData, "Parameter reviewData cannot be null.");

		ProductModel productModel = getProductService().getProductForCode(productCode);
		UserModel userModel = getUserService().getUserForUID(userId);

		CustomerReviewModel customerReviewModel = this.getModelService().create(CustomerReviewModel.class);
		customerReviewModel.setUser(userModel);
		customerReviewModel.setProduct(productModel);
		customerReviewModel.setRating(reviewData.getRating());
		customerReviewModel.setHeadline(reviewData.getHeadline());
		customerReviewModel.setComment(reviewData.getComment());
		customerReviewModel.setLanguage(getCommonI18NService().getCurrentLanguage());
		customerReviewModel.setAlias(reviewData.getAlias());
		getModelService().save(customerReviewModel);

		return (ReviewData) getCustomerReviewConverter().convert(customerReviewModel);
	}

	@Override
	public Integer getNumberOfReviewsByLanguage(String productCode)
	{
		ProductModel productModel = getProductService().getProductForCode(productCode);
		return getCustomerReviewService().getNumberOfReviewsForProductAndLanguage(productModel,
				getCommonI18NService().getCurrentLanguage());
	}

	@Override
	public List<ProductData> getIntermediateBpos(final String poCode, final String rootBpoCode)
	{
		final TmaProductOfferingModel currentPo = getTmaPoService().getPoForCode(poCode);
		final TmaBundledProductOfferingModel targetBpo = getTmaPoService().getBpoForCode(rootBpoCode);

		if (currentPo == null || targetBpo == null)
		{
			return Collections.emptyList();
		}

		final List<TmaBundledProductOfferingModel> intermediateBpoModels = getTmaPoService()
				.getIntermediateBpos(currentPo, targetBpo);

		if (CollectionUtils.isEmpty(intermediateBpoModels))
		{
			return Collections.emptyList();
		}

		final List<ProductData> result = new ArrayList<>();
		for (TmaBundledProductOfferingModel intermediateBpo : intermediateBpoModels)
		{
			final ProductData intermediateBpoData = new ProductData();
			intermediateBpoData.setCode(intermediateBpo.getCode());

			final List<ProductData> parents = new ArrayList<>();

			intermediateBpoModels.forEach((TmaBundledProductOfferingModel bpo) -> {
				if (bpo.getCode().equals(intermediateBpo.getCode()) && CollectionUtils.isEmpty(bpo.getChildren()))
				{
					return;
				}

				bpo.getChildren().stream()
						.filter((TmaProductOfferingModel child) -> child.getCode().equals(intermediateBpo.getCode()))
						.forEach((TmaProductOfferingModel child) -> {
							final ProductData parent = new ProductData();
							parent.setCode(bpo.getCode());
							parents.add(parent);
						});
			});

			intermediateBpoData.setParents(parents);

			result.add(intermediateBpoData);
		}

		return result;
	}

	@Override
	protected TmaCustomerReviewService getCustomerReviewService()
	{
		return (TmaCustomerReviewService) super.getCustomerReviewService();
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}
}
