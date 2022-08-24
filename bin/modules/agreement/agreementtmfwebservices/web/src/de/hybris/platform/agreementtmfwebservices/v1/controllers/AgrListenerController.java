/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.v1.controllers;

import de.hybris.platform.agreementservices.enums.AgrAgreementLifecycleStatus;
import de.hybris.platform.agreementservices.enums.AgrAgreementStatus;
import de.hybris.platform.agreementservices.model.AgrAgreementModel;
import de.hybris.platform.agreementservices.model.AgrAgreementSpecificationModel;
import de.hybris.platform.agreementservices.services.AgrAgreementSpecificationService;
import de.hybris.platform.agreementservices.services.AgrAgreementsService;
import de.hybris.platform.agreementtmfwebservices.v1.api.ListenerApi;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Agreement;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementAttributeValueChangeEvent;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementCreateEvent;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementDeleteEvent;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecification;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationAttributeValueChangeEvent;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationCreateEvent;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationDeleteEvent;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementSpecificationStateChangeEvent;
import de.hybris.platform.agreementtmfwebservices.v1.dto.AgreementStateChangeEvent;
import de.hybris.platform.agreementtmfwebservices.v1.dto.Error;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementSpecCreateEventValidator;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementDeleteEventValidator;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementCreateEventValidator;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementSpecDeleteEventValidator;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementSpecStateChangeEventValidator;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementSpecUpdateEventValidator;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementStateChangeEventValidator;
import de.hybris.platform.agreementtmfwebservices.v1.validators.AgrAgreementUpdateEventValidator;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link ListenerApi}.
 *
 * @since 2111
 */
@Controller
@Secured("ROLE_TRUSTED_CLIENT")
@Api(tags = "Agreements Notification Listeners")
public class AgrListenerController extends AgrBaseController implements ListenerApi
{

	@Resource(name = "agrAgreementsService")
	private AgrAgreementsService agreementsService;

	@Resource(name = "agrAgreementSpecificationService")
	private AgrAgreementSpecificationService agreementSpecificationService;

	@Resource(name = "agrAgreementCreateEventValidator")
	private AgrAgreementCreateEventValidator agreementCreateEventValidator;

	@Resource(name = "agrAgreementUpdateEventValidator")
	private AgrAgreementUpdateEventValidator agreementUpdateEventValidator;

	@Resource(name = "agrAgreementStateChangeEventValidator")
	private AgrAgreementStateChangeEventValidator agreementStateChangeEventValidator;

	@Resource(name = "agrAgreementDeleteEventValidator")
	private AgrAgreementDeleteEventValidator agreementDeleteEventValidator;

	@Resource(name = "agrAgreementSpecCreateEventValidator")
	private AgrAgreementSpecCreateEventValidator agreementSpecCreateEventValidator;

	@Resource(name = "agrAgreementSpecUpdateEventValidator")
	private AgrAgreementSpecUpdateEventValidator agreementSpecUpdateEventValidator;

	@Resource(name = "agrAgreementSpecStateChangeEventValidator")
	private AgrAgreementSpecStateChangeEventValidator agreementSpecStateChangeEventValidator;

	@Resource(name = "agrAgreementSpecDeleteEventValidator")
	private AgrAgreementSpecDeleteEventValidator agreementSpecDeleteEventValidator;

	@Resource(name = "transactionTemplate")
	private TransactionTemplate txTemplate;


	@Override
	@ApiOperation(value = "Client listener for entity AgreementCreateEvent", nickname = "listenToAgreementCreateEvent", notes = "Example of a client listener for receiving the notification AgreementCreateEvent")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Notified"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listener/agreementCreateEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity<Void> listenToAgreementCreateEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody AgreementCreateEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getAgreementCreateEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final Agreement agreement = data.getEvent().getAgreement();
		final AgrAgreementModel agreementModel = getAgreementsService().createAgreement();

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(agreement, agreementModel);
				getAgreementsService().saveAgreement(agreementModel);
			}
		});

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@Override
	@ApiOperation(value = "Client listener for entity AgreementAttributeValueChangeEvent", nickname = "listenToAgreementAttributeValueChangeEvent", notes = "Example of a client listener for receiving the notification AgreementAttributeValueChangeEvent")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notified"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listener/agreementAttributeValueChangeEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity<Void> listenToAgreementAttributeValueChangeEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody AgreementAttributeValueChangeEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getAgreementUpdateEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final Agreement agreement = data.getEvent().getAgreement();
		final AgrAgreementModel agreementModel = getAgreementsService().getAgreement(agreement.getId());

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(agreement, agreementModel, false);
				getAgreementsService().saveAgreement(agreementModel);
			}
		});

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@Override
	@ApiOperation(value = "Client listener for entity AgreementStateChangeEvent", nickname = "listenToAgreementStateChangeEvent", notes = "Example of a client listener for receiving the notification AgreementStateChangeEvent")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Notified"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listener/agreementStateChangeEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity<Void> listenToAgreementStateChangeEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody AgreementStateChangeEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getAgreementStateChangeEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final Agreement agreement = data.getEvent().getAgreement();
		final AgrAgreementModel agreementModel = getAgreementsService().getAgreement(agreement.getId());

		agreementModel.setStatus(AgrAgreementStatus.valueOf(agreement.getStatus().toUpperCase()));
		getAgreementsService().saveAgreement(agreementModel);

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@Override
	@ApiOperation(value = "Client listener for entity AgreementDeleteEvent", nickname = "listenToAgreementDeleteEvent", notes = "Example of a client listener for receiving the notification AgreementDeleteEvent")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Notified"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listener/agreementDeleteEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity<Void> listenToAgreementDeleteEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody AgreementDeleteEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getAgreementDeleteEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final Agreement agreement = data.getEvent().getAgreement();
		final AgrAgreementModel agreementModel = getAgreementsService().getAgreement(agreement.getId());
		getAgreementsService().removeAgreement(agreementModel);

		return new ResponseEntity(HttpStatus.CREATED);
	}


	@ApiOperation(value = "Client listener for entity AgreementSpecificationCreateEvent", nickname = "listenToAgreementSpecificationCreateEvent", notes = "Example of a client listener for receiving the notification AgreementSpecificationCreateEvent")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Notified"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listener/agreementSpecificationCreateEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity<Void> listenToAgreementSpecificationCreateEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody AgreementSpecificationCreateEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getAgreementSpecCreateEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final AgreementSpecification agreementSpecification = data.getEvent().getAgreementSpecification();
		final AgrAgreementSpecificationModel agreementSpecificationModel = getAgreementSpecificationService()
				.createAgreementSpecification();

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(agreementSpecification, agreementSpecificationModel);
				getAgreementSpecificationService().saveAgreementSpecification(agreementSpecificationModel);
			}
		});

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@Override
	@ApiOperation(value = "Client listener for entity AgreementSpecificationAttributeValueChangeEvent", nickname = "listenToAgreementSpecificationAttributeValueChangeEvent", notes = "Example of a client listener for receiving the notification AgreementSpecificationAttributeValueChangeEvent")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listener/agreementSpecificationAttributeValueChangeEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity<Void> listenToAgreementSpecificationAttributeValueChangeEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody AgreementSpecificationAttributeValueChangeEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getAgreementSpecUpdateEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final AgreementSpecification agreementSpecification = data.getEvent().getAgreementSpecification();
		final AgrAgreementSpecificationModel agreementSpecificationModel = getAgreementSpecificationService()
				.getAgreementSpecification(agreementSpecification.getId());

		getTxTemplate().execute(new TransactionCallbackWithoutResult()
		{
			@Override
			protected void doInTransactionWithoutResult(final TransactionStatus status)
			{
				getDataMapper().map(agreementSpecification, agreementSpecificationModel, false);
				getAgreementSpecificationService().saveAgreementSpecification(agreementSpecificationModel);
			}
		});

		return new ResponseEntity(HttpStatus.CREATED);
	}

	@Override
	@ApiOperation(value = "Client listener for entity AgreementSpecificationStateChangeEvent", nickname = "listenToAgreementSpecificationStateChangeEvent", notes = "Example of a client listener for receiving the notification AgreementSpecificationStateChangeEvent")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listener/agreementSpecificationStateChangeEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity<Void> listenToAgreementSpecificationStateChangeEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody AgreementSpecificationStateChangeEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getAgreementSpecStateChangeEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final AgreementSpecification agreementSpecification = data.getEvent().getAgreementSpecification();
		final AgrAgreementSpecificationModel agreementSpecificationModel = getAgreementSpecificationService()
				.getAgreementSpecification(agreementSpecification.getId());

		agreementSpecificationModel
				.setLifecycleStatus(AgrAgreementLifecycleStatus.valueOf(agreementSpecification.getLifecycleStatus().toUpperCase()));
		getAgreementSpecificationService().saveAgreementSpecification(agreementSpecificationModel);

		return new ResponseEntity(HttpStatus.CREATED);
	}


	@Override
	@ApiOperation(value = "Client listener for entity AgreementSpecificationDeleteEvent", nickname = "listenToAgreementSpecificationDeleteEvent", notes = "Example of a client listener for receiving the notification AgreementSpecificationDeleteEvent")
	@ApiResponses(value = {
			@ApiResponse(code = 204, message = "Deleted"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
			@ApiResponse(code = 403, message = "Forbidden", response = Error.class),
			@ApiResponse(code = 404, message = "Not Found", response = Error.class),
			@ApiResponse(code = 405, message = "Method Not allowed", response = Error.class),
			@ApiResponse(code = 409, message = "Conflict", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/listener/agreementSpecificationDeleteEvent",
			produces = { "application/json;charset=utf-8" },
			consumes = { "application/json;charset=utf-8" },
			method = RequestMethod.POST)
	public ResponseEntity<Void> listenToAgreementSpecificationDeleteEvent(
			@ApiParam(value = "The event data", required = true) @Valid @RequestBody AgreementSpecificationDeleteEvent data)
	{
		if (data == null)
		{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		final String errorMessage = validate(data, "data", getAgreementSpecDeleteEventValidator());

		if (!StringUtils.isEmpty(errorMessage))
		{
			return getUnsuccessfulResponse(errorMessage, HttpStatus.BAD_REQUEST);
		}

		final AgreementSpecification agreementSpecification = data.getEvent().getAgreementSpecification();
		final AgrAgreementSpecificationModel agrAgreementSpecificationModel = getAgreementSpecificationService()
				.getAgreementSpecification(agreementSpecification.getId());
		getAgreementSpecificationService().removeAgreementSpecification(agrAgreementSpecificationModel);

		return new ResponseEntity(HttpStatus.CREATED);
	}


	protected AgrAgreementsService getAgreementsService()
	{
		return agreementsService;
	}

	protected AgrAgreementUpdateEventValidator getAgreementUpdateEventValidator()
	{
		return agreementUpdateEventValidator;
	}

	protected AgrAgreementCreateEventValidator getAgreementCreateEventValidator()
	{
		return agreementCreateEventValidator;
	}

	protected AgrAgreementStateChangeEventValidator getAgreementStateChangeEventValidator()
	{
		return agreementStateChangeEventValidator;
	}

	protected AgrAgreementDeleteEventValidator getAgreementDeleteEventValidator()
	{
		return agreementDeleteEventValidator;
	}

	protected AgrAgreementSpecificationService getAgreementSpecificationService()
	{
		return agreementSpecificationService;
	}

	protected AgrAgreementSpecCreateEventValidator getAgreementSpecCreateEventValidator()
	{
		return agreementSpecCreateEventValidator;
	}

	protected AgrAgreementSpecUpdateEventValidator getAgreementSpecUpdateEventValidator()
	{
		return agreementSpecUpdateEventValidator;
	}

	protected AgrAgreementSpecStateChangeEventValidator getAgreementSpecStateChangeEventValidator()
	{
		return agreementSpecStateChangeEventValidator;
	}

	protected AgrAgreementSpecDeleteEventValidator getAgreementSpecDeleteEventValidator()
	{
		return agreementSpecDeleteEventValidator;
	}

	protected TransactionTemplate getTxTemplate()
	{
		return txTemplate;
	}
}
