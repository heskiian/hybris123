/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.user.TmaCustomerFacade;
import de.hybris.platform.b2ctelcotmfwebservices.security.IsAuthorizedResourceOwnerOrAdmin;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.PaymentMethodApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.PaymentMethodType;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import de.hybris.platform.servicelayer.dto.converter.ConversionException;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link PaymentMethodApi}
 *
 * @since 1907
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "paymentMethod", description = "Payment Methods API", tags = { "Payment Methods" })
public class TmaPaymentMethodApiController extends TmaBaseController implements PaymentMethodApi
{
	@Resource(name = "customerFacade")
	private TmaCustomerFacade customerFacade;

	@ApiOperation(value = "Retrieve a list of payment methods", nickname = "retrievePaymentMethods", response = PaymentMethodType.class, responseContainer = "List", tags = {
			"Payment Methods", })
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Payment methods retrieved successfully", response = PaymentMethodType.class, responseContainer = "List"),
			@ApiResponse(code = 204, message = "No Content"),
			@ApiResponse(code = 400, message = "Invalid Token"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 405, message = "Method not Allowed"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/paymentMethod",
			produces = { "application/json" },
			method = RequestMethod.GET)
	@Override
	@SuppressWarnings("unchecked")
	@IsAuthorizedResourceOwnerOrAdmin
	public ResponseEntity<List<PaymentMethodType>> retrievePaymentMethods(
			@ApiParam(value = "To retrieve methods of a specific type", allowableValues = "cash, digitalWallet, tokenizedCard, bankAccountTransfer, bankAccountDebit, bankCard, account, bucket, voucher, check, loyaltyAccount, creditCard") @Valid @RequestParam(value = "type", required = false) String type,
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) String baseSiteId,
			@ApiParam(value = "To retrieve the accounts of a specific bank") @Valid @RequestParam(value = "bankAccount.BIC", required = false) String bankAccountPeriodBIC,
			@ApiParam(value = "To retrieve a specific account") @Valid @RequestParam(value = "bankAccount.accountNumber", required = false) String bankAccountPeriodaccountNumber,
			@ApiParam(value = "To retrieve the accounts of a specific bank") @Valid @RequestParam(value = "bankAccount.bank", required = false) String bankAccountPeriodbank,
			@ApiParam(value = "To retrieve a specific bank card") @Valid @RequestParam(value = "bankCard.cardNumber", required = false) String bankCardPeriodcardNumber,
			@ApiParam(value = "To retrieve cards with a specific name") @Valid @RequestParam(value = "bankCard.nameOnCard", required = false) String bankCardPeriodnameOnCard,
			@ApiParam(value = "To retrieve cards of a specific type") @Valid @RequestParam(value = "bankCard.type", required = false) String bankCardPeriodtype,
			@ApiParam(value = "To retrieve a specific check") @Valid @RequestParam(value = "check.checkId", required = false) String checkPeriodcheckId,
			@ApiParam(value = "To retrieve checks of a specific bank") @Valid @RequestParam(value = "check.bank", required = false) String checkPeriodbank,
			@ApiParam(value = "To retrieve methods from a specific related party") @Valid @RequestParam(value = "relatedParty.id", required = false) String relatedPartyId,
			@ApiParam(value = "To retrieve methods from a specific related party") @Valid @RequestParam(value = "relatedParty.type", required = false) String relatedPartyPeriodtype,
			@ApiParam(value = "To retrieve a specific loyalty account") @Valid @RequestParam(value = "loyaltyAccount", required = false) String loyaltyAccount,
			@ApiParam(value = "To retrieve digital wallets of a specific service") @Valid @RequestParam(value = "digitalWallet.service", required = false) String digitalWalletPeriodservice,
			@ApiParam(value = "To retrieve a specific digital wallet") @Valid @RequestParam(value = "digitalWallet.id", required = false) String digitalWalletPeriodid,
			@ApiParam(value = "To retrieve a specific bucket") @Valid @RequestParam(value = "bucket", required = false) String bucket,
			@ApiParam(value = "To retrieve a specific voucher") @Valid @RequestParam(value = "voucher.id", required = false) String voucherPeriodid,
			@ApiParam(value = "To retrieve voucher with a specific code") @Valid @RequestParam(value = "voucher.code", required = false) String voucherPeriodcode,
			@ApiParam(value = "To apply a filter on every resource included in the response. It's value is a list of comma separated values of the different fields that are requested.") @Valid @RequestParam(value = "fields", required = false) String fields,
			@ApiParam(value = "To limit the maximum number of results to be included in the response. The name of query parameter is ‘limit’ and its value is an integer indicating the maximum number of elements to be included in the response.") @Valid @RequestParam(value = "limit", required = false) Integer limit,
			@ApiParam(value = "To apply an offset in the results to be included in the response. The name of query parameter is ‘offset’ and its value is an integer indicating the offset to be applied.") @Valid @RequestParam(value = "offset", required = false) Integer offset)
	{

		try
		{
			if (type != null && !PaymentMethodType.AttypeEnum.CREDITCARD.name().equalsIgnoreCase(type))
			{
				return getUnsuccessfulResponseWithErrorRepresentation("Incorrect payment method type.", null, 22,
						"Payment method type '" + type + "' is not supported.", HttpStatus.BAD_REQUEST);
			}

			if (customerFacade.getUserForUID(relatedPartyId) == null)
			{
				return getUnsuccessfulResponseWithErrorRepresentation("Incorrect user.", null, 22,
						"Related party '" + relatedPartyId + "' does not exist.", HttpStatus.BAD_REQUEST);
			}

			final List<CCPaymentInfoData> ccPaymentInfoDataList = customerFacade.getCcPaymentInfosForUser(relatedPartyId);

			if (ccPaymentInfoDataList == null)
			{
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			final List<PaymentMethodType> paymentMethodTypeList = new ArrayList<>();

			for (CCPaymentInfoData ccPaymentInfoData : ccPaymentInfoDataList)
			{
				final PaymentMethodType paymentMethodType = getDataMapper().map(ccPaymentInfoData, PaymentMethodType.class, fields);
				paymentMethodTypeList.add(paymentMethodType);
			}

			return new ResponseEntity(getObjectMapper().writeValueAsString(paymentMethodTypeList), HttpStatus.OK);

		}
		catch (final UnknownIdentifierException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
		catch (final ConversionException | JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
	}
}
