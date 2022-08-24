/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscriptionBaseData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscriptionBaseFacade;
import de.hybris.platform.b2ctelcotmfwebservices.dto.error.TmaErrorRepresentationWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.base.TmaSubscriptionBaseWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.response.TmaApiResponseMessage;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedSubscriptionBaseOwnerOrAdmin;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.ApiParam;


/**
 * Default implementation of {@link TmaSubscriptionApi}.
 *
 * @since 1810
 */

@Controller
public class TmaSubscriptionApiController extends TmaBaseController implements TmaSubscriptionApi
{
	public static final Logger LOG = Logger.getLogger(TmaSubscriptionApi.class);//NOSONAR

	private final HttpServletRequest request;

	@Autowired
	public TmaSubscriptionApiController(final HttpServletRequest request)
	{
		this.request = request;
	}

	@Resource(name = "tmaSubscriptionBaseFacade")
	private TmaSubscriptionBaseFacade tmaSubscriptionBaseFacade;

	@Override
	@IsAuthorizedSubscriptionBaseOwnerOrAdmin
	public ResponseEntity<Object> findSubscriptionbaseForId(
			@ApiParam(value = "Id for subscriptionBase selection", required = true) @PathVariable("subscriptionBaseId") final String subscriptionBaseId,
			@ApiParam(value = "Attributes selection") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @RequestParam(required = false) final String baseSiteId)
	{
		if (getAcceptHeader().isPresent())
		{
			if (getAcceptHeader().get().contains("application/xml") || getAcceptHeader().get().contains("application/json"))
			{
				LOG.debug(TmaSubscriptionApiController.class);
				try
				{
					final TmaSubscriptionBaseData subscriptionBaseData = tmaSubscriptionBaseFacade
							.getSubscriptionBaseBySubscriberIdentity(subscriptionBaseId);

					final TmaSubscriptionBaseWsDto subscriptionBaseWsDto = getDataMapper().map(subscriptionBaseData,
							TmaSubscriptionBaseWsDto.class, fields);
					final String href = getRequestUrl(request);
					setSubscriptionBaseHref(href, subscriptionBaseWsDto);
					return new ResponseEntity<>(subscriptionBaseWsDto, HttpStatus.OK);
				}
				catch (final UnknownIdentifierException | ConversionException e)
				{
					LOG.error("Error occured due to bad request", e);
					final TmaErrorRepresentationWsDto error = new TmaErrorRepresentationWsDto();
					error.setCode(TmaApiResponseMessage.ERROR);
					error.setMessage(YSanitizer.sanitize(e.getMessage()));
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}
			}
		}
		else
		{
			LOG.warn("HttpServletRequest not configured in TmaSubscriptionApiController");
		}
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	protected void setSubscriptionBaseHref(final String href, final TmaSubscriptionBaseWsDto subscriptionBaseWsDto)
	{
		if (StringUtils.isNotEmpty(href))
		{
			subscriptionBaseWsDto.setHref(href);
		}
	}

	@Override
	public Optional<HttpServletRequest> getRequest()
	{
		return Optional.ofNullable(request);
	}
}
