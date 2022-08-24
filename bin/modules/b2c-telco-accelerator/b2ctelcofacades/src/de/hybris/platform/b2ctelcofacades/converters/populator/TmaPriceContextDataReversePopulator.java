/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcofacades.converters.populator;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.b2ctelcofacades.data.TmaPriceContextData;
import de.hybris.platform.b2ctelcoservices.enums.TmaProcessType;
import de.hybris.platform.b2ctelcoservices.pricing.context.TmaPriceContext;
import de.hybris.platform.b2ctelcoservices.services.TmaPoService;
import de.hybris.platform.b2ctelcoservices.services.TmaRegionService;
import de.hybris.platform.b2ctelcoservices.services.TmaSubscriptionTermService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.HashSet;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populates the attributes of {@link TmaPriceContext} from {@link TmaPriceContextData}
 *
 * @since 1907
 */
public class TmaPriceContextDataReversePopulator implements Populator<TmaPriceContextData, TmaPriceContext>
{
	private TmaPoService tmaPoService;
	private TmaRegionService tmaRegionService;
	private UserService userService;
	private CommonI18NService i18NService;
	private TmaSubscriptionTermService tmaSubscriptionTermService;
	private EnumerationService enumerationService;

	@Override
	public void populate(final TmaPriceContextData source, final TmaPriceContext target)
	{
		validateParameterNotNullStandardMessage("source", source);
		validateParameterNotNullStandardMessage("target", target);

		if (StringUtils.isNotBlank(source.getProductCode()))
		{
			target.setProduct(getTmaPoService().getPoForCode(source.getProductCode()));
		}
		if (StringUtils.isNotBlank(source.getUserId()))
		{
			target.setUser(getUserService().getUserForUID(source.getUserId()));
		}

		if (StringUtils.isNotBlank(source.getAffectedProductCode()))
		{
			target.setAffectedProduct(getTmaPoService().getPoForCode(source.getAffectedProductCode()));
		}
		if (StringUtils.isNotBlank(source.getCurrencyIsoCode()))
		{
			target.setCurrency(getI18NService().getCurrency(source.getCurrencyIsoCode()));
		}
		populateProcessTypes(source, target);
		populateRegions(source, target);
		populateRequiredProducts(source, target);
		populateSubscriptionTerms(source, target);
	}

	private void populateSubscriptionTerms(final TmaPriceContextData source, final TmaPriceContext target)
	{
		if (CollectionUtils.isNotEmpty(source.getSubscriptionTermIds()))
		{
			target.setSubscriptionTerms(source.getSubscriptionTermIds().stream()
					.map(subscriptionTermId -> getTmaSubscriptionTermService().getSubscriptionTerm(subscriptionTermId))
					.collect(Collectors.toCollection(HashSet::new)));
		}
	}

	private void populateRequiredProducts(final TmaPriceContextData source, final TmaPriceContext target)
	{
		if (CollectionUtils.isNotEmpty(source.getRequiredProductCodes()))
		{
			target.setRequiredProducts((source.getRequiredProductCodes().stream()
					.map(requiredPoCode -> getTmaPoService().getPoForCode(requiredPoCode)))
							.collect(Collectors.toCollection(HashSet::new)));
		}
	}

	private void populateRegions(final TmaPriceContextData source, final TmaPriceContext target)
	{
		if (CollectionUtils.isNotEmpty(source.getRegionIsoCodes()))
		{
			target.setRegions(source.getRegionIsoCodes().stream()
					.map(regionIsoCode -> getTmaRegionService().findRegionByIsocode(regionIsoCode))
					.collect(Collectors.toCollection(HashSet::new)));
		}
	}

	private void populateProcessTypes(final TmaPriceContextData source, final TmaPriceContext target)
	{
		if (CollectionUtils.isNotEmpty(source.getProcessTypeCodes()))
		{
			target.setProcessTypes(source.getProcessTypeCodes().stream()
					.map(processTypeCode -> getProcessType(processTypeCode))
					.collect(Collectors.toCollection(HashSet::new)));
		}
	}

	private TmaProcessType getProcessType(final String code)
	{
		return getEnumerationService().getEnumerationValue(TmaProcessType._TYPECODE, code);
	}

	protected TmaPoService getTmaPoService()
	{
		return tmaPoService;
	}

	@Required
	public void setTmaPoService(final TmaPoService tmaPoService)
	{
		this.tmaPoService = tmaPoService;
	}

	protected TmaRegionService getTmaRegionService()
	{
		return tmaRegionService;
	}

	@Required
	public void setTmaRegionService(final TmaRegionService tmaRegionService)
	{
		this.tmaRegionService = tmaRegionService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected CommonI18NService getI18NService()
	{
		return i18NService;
	}

	@Required
	public void setI18NService(final CommonI18NService i18NService)
	{
		this.i18NService = i18NService;
	}

	protected TmaSubscriptionTermService getTmaSubscriptionTermService()
	{
		return tmaSubscriptionTermService;
	}

	@Required
	public void setTmaSubscriptionTermService(
			final TmaSubscriptionTermService tmaSubscriptionTermService)
	{
		this.tmaSubscriptionTermService = tmaSubscriptionTermService;
	}

	protected EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	@Required
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
