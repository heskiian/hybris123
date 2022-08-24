/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcofacades.subscription.converters.populator;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscribedProductModel;
import de.hybris.platform.b2ctelcoservices.model.TmaSubscriptionBaseModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;


/**
 * Populates subscribedProduct attribute between {@link TmaSubscriptionBaseData} and {@link TmaSubscriptionBaseModel} entity.
 *
 * @since 1907
 */
public class TmaSubscriptionBaseSubscribedProductPopulator implements Populator<TmaSubscriptionBaseModel, TmaSubscriptionBaseData>
{
	 /**
	  * Converts from {@link TmaSubscribedProductModel} to {@link TmaSubscribedProductData}
	  */
	 private Converter<TmaSubscribedProductModel, TmaSubscribedProductData> tmaSubscribedProductConverter;

	 @Override
	 public void populate(TmaSubscriptionBaseModel source, TmaSubscriptionBaseData target) throws ConversionException
	 {
		  final Set<TmaSubscribedProductData> subscribedProductDataSet = new HashSet<>();

		  for (TmaSubscribedProductModel subscribedProductModel : source.getSubscribedProducts())
		  {
				subscribedProductDataSet.add(getTmaSubscribedProductConverter().convert(subscribedProductModel));
		  }

		  target.setTmaSubscribedProductData(subscribedProductDataSet);
	 }

	 protected Converter<TmaSubscribedProductModel, TmaSubscribedProductData> getTmaSubscribedProductConverter()
	 {
		  return tmaSubscribedProductConverter;
	 }

	 @Required
	 public void setTmaSubscribedProductConverter(
		  Converter<TmaSubscribedProductModel, TmaSubscribedProductData> tmaSubscribedProductConverter)
	 {
		  this.tmaSubscribedProductConverter = tmaSubscribedProductConverter;
	 }
}
