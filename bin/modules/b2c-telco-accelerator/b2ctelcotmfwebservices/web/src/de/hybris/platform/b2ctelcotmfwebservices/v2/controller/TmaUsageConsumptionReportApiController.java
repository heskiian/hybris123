/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaAverageServiceUsageData;
import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcotmfwebservices.constants.B2ctelcotmfwebservicesConstants;
import de.hybris.platform.b2ctelcotmfwebservices.dto.TmaProductRefWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.error.TmaErrorRepresentationWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage.TmaBalanceWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage.TmaBucketWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage.TmaConsumptionCounterWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.usage.TmaUsageConsumptionReportWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.response.TmaApiResponseMessage;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedSubscriptionBaseOwnerOrAdmin;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.subscriptionfacades.exceptions.SubscriptionFacadeException;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;


/**
 * Default implementation of {@link TmaUsageConsumptionReportApi}.
 * @since 1810
 */

@Controller
public class TmaUsageConsumptionReportApiController extends TmaBaseController implements TmaUsageConsumptionReportApi
{
	private static final Logger LOG = LoggerFactory.getLogger(TmaUsageConsumptionReportApi.class);//NOSONAR

	@Autowired
	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Resource(name = "tmaSubscribedProductFacade")
	private TmaSubscribedProductFacade tmaSubscribedProductFacade;

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@org.springframework.beans.factory.annotation.Autowired
	public TmaUsageConsumptionReportApiController(final ObjectMapper objectMapper, final HttpServletRequest request)
	{
		this.objectMapper = objectMapper;
		this.request = request;
	}

	@Override
	@IsAuthorizedSubscriptionBaseOwnerOrAdmin
	public ResponseEntity<Object> usageConsumptionReportFind(
			@ApiParam(value = "SubscriberIdentity contains Id of subcriptionbase", required = true) @Valid @RequestParam(value = "subscriptionBase.id", required = true) final String subscriptionBaseId,
			@ApiParam(value = "Attribute selection") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Susbcribed Product ID") @Valid @RequestParam(value = "product.id", required = false) final String productId,
			@ApiParam(value = "Identifier of the BaseSite") @RequestParam(required = false) final String baseSiteId)
	{
		if (getAcceptHeader().isPresent())
		{
			if (getAcceptHeader().get().contains("application/xml") || getAcceptHeader().get().contains("application/json"))
			{
				try
				{
					final TmaUsageConsumptionReportWsDto tmaUsageConsumptionReportWsDtos = prepareUsageConsumptionReportData(
							subscriptionBaseId, productId, fields);
					return new ResponseEntity<>(tmaUsageConsumptionReportWsDtos, HttpStatus.OK);
				}
				catch (final ModelNotFoundException | SubscriptionFacadeException | UnknownIdentifierException
						| ConversionException e)
				{
					LOG.error("Error occured due to bad request", e);
					final TmaErrorRepresentationWsDto error = new TmaErrorRepresentationWsDto();
					error.setCode(TmaApiResponseMessage.ERROR);
					error.setMessage(YSanitizer.sanitize(e.getMessage()));
					LOG.error(e.getMessage(), e);
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}
			}
		}
		else
		{
			LOG.warn(
					"ObjectMapper or HttpServletRequest not configured in default TmaUsageConsumptionReportApi interface so no example is generated");
		}
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	protected TmaBucketWsDto prepareBucketDatas(final TmaAverageServiceUsageData tmaAverageServiceUsageData,
			final TmaSubscribedProductData tmaSubscribedProductData, final String fields)
	{
		final List<TmaConsumptionCounterWsDto> tmaConsumptionCounterWsDtos = new ArrayList<>();
		final List<TmaBalanceWsDto> tmaBalanceWsDtos = new ArrayList<>();
		final String bucketUsagelabel = messageSource
				.getMessage(B2ctelcotmfwebservicesConstants.TMA_BUCKET_LIST_WSDTO_NAME_PROPERTY, null, Locale.ENGLISH);
		final TmaBucketWsDto tmaBucketWsDto = getDataMapper().map(tmaAverageServiceUsageData, TmaBucketWsDto.class, fields);
		tmaConsumptionCounterWsDtos.add(getDataMapper().map(tmaAverageServiceUsageData, TmaConsumptionCounterWsDto.class, fields));
		tmaBalanceWsDtos.add(getDataMapper().map(tmaAverageServiceUsageData, TmaBalanceWsDto.class, fields));
		tmaBucketWsDto.setBucketCounter(tmaConsumptionCounterWsDtos);
		tmaBucketWsDto.setBucketBalance(tmaBalanceWsDtos);
		tmaBucketWsDto.setProduct(getDataMapper().map(tmaSubscribedProductData, TmaProductRefWsDto.class, fields));
		tmaBucketWsDto.setName(bucketUsagelabel + " " + tmaSubscribedProductData.getName());
		return tmaBucketWsDto;
	}

	protected TmaUsageConsumptionReportWsDto prepareUsageConsumptionReportData(final String subscriptionBaseId,
			final String productId, final String fields) throws SubscriptionFacadeException
	{
		final List<TmaBucketWsDto> tmaBucketWsDtos = new ArrayList<>();
		final List<TmaSubscribedProductData> tmaSubscribedProductDatas = new ArrayList<>();
		final Map<TmaSubscribedProductData, Set<TmaAverageServiceUsageData>> subscriptionBaseServicesWithValueMap = tmaSubscribedProductFacade
				.getSubscriptionBaseProductWithAvgValues(subscriptionBaseId, productId);
		subscriptionBaseServicesWithValueMap.entrySet().forEach(mapItem ->
		{
			final TmaSubscribedProductData tmaSubscribedProductData = mapItem.getKey();
			//prepare Bucket
			for (final TmaAverageServiceUsageData tmaAverageServiceUsageData : mapItem.getValue())
			{
				tmaBucketWsDtos.add(prepareBucketDatas(tmaAverageServiceUsageData, tmaSubscribedProductData, fields));
			}
			tmaSubscribedProductDatas.add(tmaSubscribedProductData);
		});
		final TmaUsageConsumptionReportWsDto usageConsumptionReportWsDto = getDataMapper().map(tmaSubscribedProductDatas.get(0),
				TmaUsageConsumptionReportWsDto.class, fields);
		final String href = getRequestUrl(request);
		if (StringUtils.isNotEmpty(href))
		{
			usageConsumptionReportWsDto.setHref(href);
		}
		usageConsumptionReportWsDto.setBucket(tmaBucketWsDtos);
		return usageConsumptionReportWsDto;
	}

	@Override
	public Optional<HttpServletRequest> getRequest()
	{
		return Optional.ofNullable(request);
	}

}
