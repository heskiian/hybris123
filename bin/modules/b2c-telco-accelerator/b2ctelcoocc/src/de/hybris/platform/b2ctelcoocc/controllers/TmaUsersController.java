/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoocc.controllers;

import de.hybris.platform.b2ctelcofacades.data.TmaIdentificationData;
import de.hybris.platform.b2ctelcocommercewebservicescommons.dto.TmaIdentificationWsDTO;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.request.mapping.annotation.ApiVersion;
import de.hybris.platform.commerceservices.request.mapping.annotation.RequestMappingOverride;
import de.hybris.platform.commercewebservicescommons.dto.user.UserSignUpWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.UserWsDTO;
import de.hybris.platform.webservicescommons.cache.CacheControl;
import de.hybris.platform.webservicescommons.cache.CacheControlDirective;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdParam;
import de.hybris.platform.webservicescommons.swagger.ApiFieldsParam;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



/**
 * Controller for user related requests such as create/update a user.
 *
 * @since 1911
 */

@Controller
@RequestMapping(value = "/{baseSiteId}/users")
@CacheControl(directive = CacheControlDirective.PRIVATE)
@ApiVersion("v2")
@Api(tags = "Users")
public class TmaUsersController extends BaseController
{
	private static final Logger LOG = LoggerFactory.getLogger(TmaUsersController.class);
	private static final String IDENTIFICATION = "identification";

	@Resource(name = "tmaCustomerFacade")
	private CustomerFacade customerFacade;
	@Resource(name = "userSignUpDTOValidator")
	private Validator userSignUpDTOValidator;
	@Resource(name = "putUserDTOValidator")
	private Validator putUserDTOValidator;
	@Resource(name = "tmaIdentificationValidator")
	private Validator identificationValidator;
	
	private final String[] DISALLOWED_FIELDS = new String[] {};

	@Secured(
	{ "ROLE_CLIENT", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(method = RequestMethod.POST, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@RequestMappingOverride(priorityProperty = "b2ctelcoocc.TmaUsersController.createUser.priority")
	@ResponseStatus(value = HttpStatus.CREATED)
	@ResponseBody
	@ApiOperation(nickname = "createUser", value = "Registers a customer", notes = "Registers a customer. Captures customers identification details if provided. Requires the following "
			+ "parameters:login, password, firstName, lastName, titleCode, identifications")
	@ApiBaseSiteIdParam
	public UserWsDTO createUser(@ApiParam(value = "User's object.", required = true) @RequestBody final UserSignUpWsDTO user,
			@ApiFieldsParam @RequestParam(defaultValue = DEFAULT_FIELD_SET) final String fields,
			final HttpServletRequest httpRequest, final HttpServletResponse httpResponse)
	{
		validate(user, "user", userSignUpDTOValidator);
		final List<TmaIdentificationWsDTO> identificationList = user.getIdentifications();
		if (CollectionUtils.isNotEmpty(identificationList))
		{
			validate(identificationList, IDENTIFICATION, identificationValidator);
		}
		final RegisterData registerData = getDataMapper().map(user, RegisterData.class);
		boolean userExists = false;
		try
		{
			customerFacade.register(registerData);
		}
		catch (final DuplicateUidException ex)
		{
			userExists = true;
			LOG.debug("Duplicated UID", ex);
		}
		final String userId = user.getUid().toLowerCase(Locale.ENGLISH);
		httpResponse.setHeader(B2ctelcooccControllerConstants.LOCATION, getAbsoluteLocationURL(httpRequest, userId)); //NOSONAR
		final CustomerData customerData = getCustomerData(registerData, userExists, userId);
		return getDataMapper().map(customerData, UserWsDTO.class, fields);
	}


	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@RequestMappingOverride(priorityProperty = "b2ctelcoocc.TmaUsersController.createUser.priority")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(nickname = "replaceUser", value = "Updates customer profile", notes = "Updates customer profile and identification details. Attributes not provided in the request body will be defined again (set to null or default).")
	@ApiBaseSiteIdAndUserIdParam
	public void replaceUser(@ApiParam(value = "User's object", required = true) @RequestBody final UserWsDTO user)
			throws DuplicateUidException
	{
		validate(user, "user", putUserDTOValidator);
		final List<TmaIdentificationWsDTO> identificationList = user.getIdentifications();
		if (CollectionUtils.isNotEmpty(identificationList))
		{
			validate(identificationList, IDENTIFICATION, identificationValidator);
		}
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (LOG.isDebugEnabled())
		{
			LOG.debug("replaceUser: userId={}", customer.getUid());
		}
		getDataMapper().map(user, customer, true);
		customerFacade.updateFullProfile(customer);
	}

	@Secured(
	{ "ROLE_CUSTOMERGROUP", "ROLE_TRUSTED_CLIENT", "ROLE_CUSTOMERMANAGERGROUP" })
	@RequestMapping(value = "/{userId}", method = RequestMethod.PATCH, consumes =
	{ MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	@RequestMappingOverride(priorityProperty = "b2ctelcoocc.TmaUsersController.createUser.priority")
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(nickname = "updateUser", value = "Updates customer profile", notes = "Updates customer profile and identification details. Only attributes provided in the request body will be changed.")
	@ApiBaseSiteIdAndUserIdParam
	public void updateUser(@ApiParam(value = "User's object.", required = true) @RequestBody final UserWsDTO user)
			throws DuplicateUidException
	{
		final List<TmaIdentificationWsDTO> identificationList = user.getIdentifications();
		final CustomerData customer = customerFacade.getCurrentCustomer();
		if (CollectionUtils.isNotEmpty(identificationList))
		{
			validate(identificationList, IDENTIFICATION, identificationValidator);
			this.patchIdentificationDataForCustomer(user, customer);
		}
		if (LOG.isDebugEnabled())
		{
			LOG.debug("updateUser: userId={}", customer.getUid());
		}
		getDataMapper().map(user, customer, false);
		customerFacade.updateFullProfile(customer);
	}

	protected CustomerData getCustomerData(final RegisterData registerData, final boolean userExists, final String userId)
	{
		final CustomerData customerData;
		if (userExists)
		{
			customerData = customerFacade.nextDummyCustomerData(registerData);
		}
		else
		{
			customerData = customerFacade.getUserForUID(userId);
		}
		return customerData;
	}

	protected String getAbsoluteLocationURL(final HttpServletRequest httpRequest, final String uid)
	{
		final String requestURL = sanitize(httpRequest.getRequestURL().toString());
		final StringBuilder absoluteURLSb = new StringBuilder(requestURL);
		if (!requestURL.endsWith(B2ctelcooccControllerConstants.SLASH))
		{
			absoluteURLSb.append(B2ctelcooccControllerConstants.SLASH);
		}
		absoluteURLSb.append(UriUtils.encodePathSegment(uid, StandardCharsets.UTF_8.name()));
		return absoluteURLSb.toString();
	}

	private void patchIdentificationDataForCustomer(final UserWsDTO user, final CustomerData customer)
	{
		final List<TmaIdentificationWsDTO> mergedIdentificationWsDTO = new ArrayList<>();
		mergedIdentificationWsDTO.addAll(user.getIdentifications());
		final List<TmaIdentificationData> existingIdentificationData = customer.getIdentifications();
		existingIdentificationData.forEach(identification ->
		{
			mergedIdentificationWsDTO.add(getDataMapper().map(identification, TmaIdentificationWsDTO.class));
		});
		user.setIdentifications(mergedIdentificationWsDTO);
	}

	
	@InitBinder
	public void initBinder(final WebDataBinder binder)
	{
		binder.setDisallowedFields(DISALLOWED_FIELDS);
	}

}
