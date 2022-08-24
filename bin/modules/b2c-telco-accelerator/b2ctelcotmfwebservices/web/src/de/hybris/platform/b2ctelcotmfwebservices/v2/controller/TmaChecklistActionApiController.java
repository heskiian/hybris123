/*
 *   Copyright (c) 2019 SAP SE or an SAP affiliate company.  All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfwebservices.v2.controller;

import de.hybris.platform.b2ctelcofacades.checklist.TmaChecklistFacade;
import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionData;
import de.hybris.platform.b2ctelcofacades.data.TmaChecklistActionParamData;
import de.hybris.platform.b2ctelcofacades.purchaseflow.TmaProcessTypeFacade;
import de.hybris.platform.b2ctelcotmfwebservices.v2.api.ChecklistActionApi;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.ChecklistAction;
import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Error;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Default implementation of {@link ChecklistActionApi}
 *
 * @since 1907
 */
@Controller
@SuppressWarnings("unused")
@Api(value = "checklistAction", description = "the checklistAction API", tags = { "ChecklistAction" })
public class TmaChecklistActionApiController extends TmaBaseController implements ChecklistActionApi
{
	@Resource(name = "tmaChecklistFacade")
	private TmaChecklistFacade tmaChecklistFacade;

	@Resource(name = "tmaProcessTypeFacade")
	private TmaProcessTypeFacade tmaProcessTypeFacade;

	@ApiOperation(value = "Retrieve the Checklist Actions for the given context", nickname = "getChecklistActions", notes = "", response = ChecklistAction.class, responseContainer = "List"
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok", response = ChecklistAction.class, responseContainer = "List"),
			@ApiResponse(code = 206, message = "Partial Content", response = ChecklistAction.class, responseContainer = "List"),
			@ApiResponse(code = 400, message = "Bad Request", response = Error.class),
			@ApiResponse(code = 500, message = "Internal Server Error", response = Error.class) })
	@RequestMapping(value = "/checklistAction",
			produces = { "application/json;charset=utf-8" },
			method = RequestMethod.GET)
	@Override
	public ResponseEntity<List<ChecklistAction>> getChecklistActions(
			@ApiParam(value = "Identifier of the BaseSite") @Valid @RequestParam(value = "baseSiteId", required = false) String baseSiteId,
			@ApiParam(value = "Product Offering IDs for which to retrieve the Checklist Actions") @Valid @RequestParam(value = "productOffering.id", required = false) List<String> productOfferingPeriodid,
			@ApiParam(value = "Purchase Flow for which to retrieve the Checklist Actions") @Valid @RequestParam(value =
					"processType.id", required = false) String processTypePeriodid,
			@ApiParam(value = "Comma separated properties to display in response") @Valid @RequestParam(value = "fields", required = false) String fields)
	{
		try
		{
			final TmaChecklistActionParamData tmaChecklistActionParamData = new TmaChecklistActionParamData();

			if (CollectionUtils.isNotEmpty(productOfferingPeriodid))
			{
				tmaChecklistActionParamData.setProductCodes(productOfferingPeriodid);
			}
			tmaChecklistActionParamData.setProcessType(StringUtils.isNotEmpty(processTypePeriodid) ?
					tmaProcessTypeFacade.getProcessType(processTypePeriodid) : null);
			final List<TmaChecklistActionData> tmaChecklistActionDatas =
					new ArrayList<>(tmaChecklistFacade.findActions(tmaChecklistActionParamData));
			final List<ChecklistAction> checklistActions = new ArrayList<>();
			for (TmaChecklistActionData tmaChecklistActionData : tmaChecklistActionDatas)
			{
				checklistActions.add(getDataMapper().map(tmaChecklistActionData, ChecklistAction.class, fields));
			}
			return new ResponseEntity(getObjectMapper().writeValueAsString(checklistActions), HttpStatus.OK);
		}
		catch (final JsonProcessingException e)
		{
			return getUnsuccessfulResponse(BAD_REQUEST, e, HttpStatus.BAD_REQUEST);
		}
		catch (final ModelNotFoundException e)
		{
			return getUnsuccessfulResponse(NOT_FOUND, e, HttpStatus.NOT_FOUND);
		}
	}
}
