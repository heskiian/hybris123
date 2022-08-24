/*
 *  Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.price.converters;

import de.hybris.platform.b2ctelcofacades.data.TmaProductPriceClassData;
import de.hybris.platform.b2ctelcofacades.data.TmaUserData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductPriceClassModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import org.springframework.util.ObjectUtils;

import static de.hybris.platform.converters.Converters.convertAll;
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates {@link PriceData} context attributes based on {@link PriceRowModel}.
 *
 * @since 2007
 */
public class TmaPriceRowContextPopulator<SOURCE extends PriceRowModel, TARGET extends PriceData>
		implements Populator<SOURCE, TARGET>
{
	private Converter<SubscriptionTermModel, SubscriptionTermData> tmaSubscriptionTermConverter;
	private TmaSubscriptionTermService tmaSubscriptionTermService;
	private Converter<ProductModel, ProductData> productConverter;
	private Converter<TmaProductPriceClassModel, TmaProductPriceClassData> tmaRequiredProductClassesConverter;
	private Converter<RegionModel, RegionData> regionConverter;
	private Converter<UserModel, TmaUserData> userConverter;

	public TmaPriceRowContextPopulator(
			final Converter<SubscriptionTermModel, SubscriptionTermData> tmaSubscriptionTermConverter,
			final TmaSubscriptionTermService tmaSubscriptionTermService,
			final Converter<ProductModel, ProductData> productConverter,
			final Converter<TmaProductPriceClassModel, TmaProductPriceClassData> tmaRequiredProductClassesConverter,
			final Converter<RegionModel, RegionData> regionConverter,
			final Converter<UserModel, TmaUserData> userConverter)
	{
		this.tmaSubscriptionTermConverter = tmaSubscriptionTermConverter;
		this.tmaSubscriptionTermService = tmaSubscriptionTermService;
		this.productConverter = productConverter;
		this.tmaRequiredProductClassesConverter = tmaRequiredProductClassesConverter;
		this.regionConverter = regionConverter;
		this.userConverter = userConverter;
	}

	@Override
	public void populate(final SOURCE source, final TARGET target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		target.setStartTime(source.getStartTime());
		target.setEndTime(source.getEndTime());
		target.setProduct(getProductConverter().convert(source.getProduct()));
		target.setDistributionChannels(source.getDistributionChannels());
		target.setProcessTypes(source.getProcessTypes());
		target.setSubscriptionTerms(
				convertAll(getTmaSubscriptionTermService().getSubscriptionTermsFor(source), getTmaSubscriptionTermConverter()));
		if (!ObjectUtils.isEmpty(source.getAffectedProductOffering()))
		{
			target.setAffectedProductOffering(getProductConverter().convert(source.getAffectedProductOffering()));
		}
		target.setRequiredProductOfferings(convertAll(source.getRequiredProductOfferings(), getProductConverter()));
		target.setRequiredProductClasses(convertAll(source.getRequiredProductClasses(), getTmaRequiredProductClassesConverter()));
		target.setRegions(getRegionConverter().convertAll(source.getRegions()));
		if (!ObjectUtils.isEmpty(source.getUser()))
		{
			target.setUser(getUserConverter().convert(source.getUser()));
		}
		if (!ObjectUtils.isEmpty(source.getUg()))
		{
			target.setUserPriceGroupID(source.getUg().toString());
		}
	}

	protected Converter<SubscriptionTermModel, SubscriptionTermData> getTmaSubscriptionTermConverter()
	{
		return tmaSubscriptionTermConverter;
	}

	protected TmaSubscriptionTermService getTmaSubscriptionTermService()
	{
		return tmaSubscriptionTermService;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	protected Converter<TmaProductPriceClassModel, TmaProductPriceClassData> getTmaRequiredProductClassesConverter()
	{
		return tmaRequiredProductClassesConverter;
	}

	protected Converter<RegionModel, RegionData> getRegionConverter()
	{
		return regionConverter;
	}

	protected Converter<UserModel, TmaUserData> getUserConverter()
	{
		return userConverter;
	}
}
