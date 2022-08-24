/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaProductPriceClassData;
import de.hybris.platform.b2ctelcofacades.data.TmaUserData;
import de.hybris.platform.b2ctelcofacades.data.UnitData;
import de.hybris.platform.b2ctelcoservices.model.TmaProductPriceClassModel;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.data.RegionData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.c2l.RegionModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.subscriptionfacades.data.SubscriptionPricePlanData;
import de.hybris.platform.subscriptionfacades.data.SubscriptionTermData;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.model.SubscriptionTermModel;

import org.springframework.beans.factory.annotation.Required;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;


/**
 * Populates {@link SubscriptionPricePlanData} based on {@link SubscriptionPricePlanModel}.
 *
 * @since 6.7
 */
public class TmaSubscriptionPricePlanPopulator implements Populator<PriceRowModel, SubscriptionPricePlanData>
{
	private Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanUsageChargePopulator;
	private Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanOneTimeChargePopulator;
	private Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanRecurringChargePopulator;
	private Converter<SubscriptionTermModel, SubscriptionTermData> tmaSubscriptionTermConverter;
	private Converter<ProductModel, ProductData> productConverter;
	private Converter<UnitModel, UnitData> tmaUnitConverter;
	private Converter<UserModel, TmaUserData> userConverter;
	private TmaSubscriptionTermService tmaSubscriptionTermService;
	private Converter<TmaProductPriceClassModel, TmaProductPriceClassData> tmaRequiredProductClassesConverter;
	private Converter<RegionModel, RegionData> regionConverter;

	@Override
	public void populate(final PriceRowModel source, final SubscriptionPricePlanData target)
	{
		validateParameterNotNullStandardMessage("target", target);
		validateParameterNotNullStandardMessage("source", source);

		if (source instanceof SubscriptionPricePlanModel)
		{
			final SubscriptionPricePlanModel pricePlanModel = (SubscriptionPricePlanModel) source;
			target.setName(pricePlanModel.getName());
		}
	}

	protected Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> getPricePlanOneTimeChargePopulator()
	{
		return pricePlanOneTimeChargePopulator;
	}

	@Required
	public void setPricePlanOneTimeChargePopulator(
			final Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanOneTimeChargePopulator)
	{
		this.pricePlanOneTimeChargePopulator = pricePlanOneTimeChargePopulator;
	}

	protected Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> getPricePlanRecurringChargePopulator()
	{
		return pricePlanRecurringChargePopulator;
	}

	@Required
	public void setPricePlanRecurringChargePopulator(
			final Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanRecurringChargePopulator)
	{
		this.pricePlanRecurringChargePopulator = pricePlanRecurringChargePopulator;
	}

	protected Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> getPricePlanUsageChargePopulator()
	{
		return pricePlanUsageChargePopulator;
	}

	@Required
	public void setPricePlanUsageChargePopulator(
			final Populator<SubscriptionPricePlanModel, SubscriptionPricePlanData> pricePlanUsageChargePopulator)
	{
		this.pricePlanUsageChargePopulator = pricePlanUsageChargePopulator;
	}

	public Converter<SubscriptionTermModel, SubscriptionTermData> getTmaSubscriptionTermConverter()
	{
		return tmaSubscriptionTermConverter;
	}

	@Required
	public void setTmaSubscriptionTermConverter(
			final Converter<SubscriptionTermModel, SubscriptionTermData> tmaSubscriptionTermConverter)
	{
		this.tmaSubscriptionTermConverter = tmaSubscriptionTermConverter;
	}

	public TmaSubscriptionTermService getTmaSubscriptionTermService()
	{
		return tmaSubscriptionTermService;
	}

	@Required
	public void setTmaSubscriptionTermService(final TmaSubscriptionTermService tmaSubscriptionTermService)
	{
		this.tmaSubscriptionTermService = tmaSubscriptionTermService;
	}

	protected Converter<ProductModel, ProductData> getProductConverter()
	{
		return productConverter;
	}

	@Required
	public void setProductConverter(final Converter<ProductModel, ProductData> productConverter)
	{
		this.productConverter = productConverter;
	}

	protected Converter<UnitModel, UnitData> getTmaUnitConverter()
	{
		return tmaUnitConverter;
	}

	@Required
	public void setTmaUnitConverter(final Converter<UnitModel, UnitData> tmaUnitConverter)
	{
		this.tmaUnitConverter = tmaUnitConverter;
	}

	protected Converter<TmaProductPriceClassModel, TmaProductPriceClassData> getTmaRequiredProductClassesConverter()
	{
		return tmaRequiredProductClassesConverter;
	}

	@Required
	public void setTmaRequiredProductClassesConverter(
			final Converter<TmaProductPriceClassModel, TmaProductPriceClassData> tmaRequiredProductClassesConverter)
	{
		this.tmaRequiredProductClassesConverter = tmaRequiredProductClassesConverter;
	}

	protected Converter<UserModel, TmaUserData> getUserConverter()
	{
		return userConverter;
	}

	@Required
	public void setUserConverter(final Converter<UserModel, TmaUserData> userConverter)
	{
		this.userConverter = userConverter;
	}

	protected Converter<RegionModel, RegionData> getRegionConverter()
	{
		return regionConverter;
	}

	@Required
	public void setRegionConverter(final Converter<RegionModel, RegionData> regionConverter)
	{
		this.regionConverter = regionConverter;
	}

}
