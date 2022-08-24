/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.data.TmaSubscribedProductData;
import de.hybris.platform.b2ctelcofacades.subscription.TmaSubscribedProductFacade;
import de.hybris.platform.b2ctelcotmfwebservices.dto.error.TmaErrorRepresentationWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.dto.subscription.TmaProductWsDto;
import de.hybris.platform.b2ctelcotmfwebservices.response.TmaApiResponseMessage;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.util.Optional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiParam;

/**
 * @deprecated since 2102. Please use V3 version of
 *             {@link de.hybris.platform.b2ctelcotmfwebservices.v3.controller.TmaProductApiController}
 *
 */

@Deprecated(since = "2102")
@Controller
public class TmaProductApiController extends TmaBaseController implements TmaProductApi
{

	@Resource(name = "objectMapper")
	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Resource(name = "tmaSubscribedProductFacade")
	private TmaSubscribedProductFacade tmaSubscribedProductFacade;


	@org.springframework.beans.factory.annotation.Autowired
	public TmaProductApiController(final ObjectMapper objectMapper, final HttpServletRequest request)
	{
		this.objectMapper = objectMapper;
		this.request = request;
	}

	
	@Override
	public ResponseEntity<Object> productGet(
			@ApiParam(value = "Identifier of the Product ", required = true) @PathVariable("productId") final String productId,
			@ApiParam(value = "Response configuration. BASIC/FULL/DEFAULT") @Valid @RequestParam(value = "fields", required = false) final String fields,
			@ApiParam(value = "Identifier of the BaseSite") @RequestParam(required = false) final String baseSiteId)
	{

		if (getAcceptHeader().isPresent())
		{
			if (getAcceptHeader().get().contains("application/xml") || getAcceptHeader().get().contains("application/json"))
			{
				try
				{
					final TmaSubscribedProductData tmaSubscribedProductData = tmaSubscribedProductFacade
							.getSubscriptionsById(productId);
					final TmaProductWsDto productWsDto = getDataMapper().map(tmaSubscribedProductData, TmaProductWsDto.class,
							fields);
					final String href = getRequestUrl(request);
					setProductHref(href, productWsDto);
					return new ResponseEntity<>(productWsDto, HttpStatus.OK);
				}
				catch (final UnknownIdentifierException | ConversionException e)
				{
					log.error("Error occured due to bad request", e);
					final TmaErrorRepresentationWsDto error = new TmaErrorRepresentationWsDto();
					error.setCode(TmaApiResponseMessage.ERROR);
					error.setMessage(YSanitizer.sanitize(e.getMessage()));
					return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
				}
			}
		}
		else
		{
			log.warn("ObjectMapper or HttpServletRequest not configured in default ProductApi interface so no example is generated");
		}
		return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
	}

	protected void setProductHref(final String href, final TmaProductWsDto productWsDto)
	{
		if (StringUtils.isNotEmpty(href))
		{
			productWsDto.setHref(href);
		}
	}


	@Override
	public Optional<HttpServletRequest> getRequest()
	{
		return Optional.ofNullable(request);
	}
}
