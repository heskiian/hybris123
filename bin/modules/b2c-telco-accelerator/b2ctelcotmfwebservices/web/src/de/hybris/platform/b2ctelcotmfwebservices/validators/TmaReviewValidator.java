/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcotmfwebservices.validators;

import de.hybris.platform.b2ctelcotmfwebservices.v2.dto.Review;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static de.hybris.platform.customerreview.model.CustomerReviewModel.HEADLINE;
import static de.hybris.platform.customerreview.model.CustomerReviewModel.COMMENT;
import static de.hybris.platform.customerreview.model.CustomerReviewModel.RATING;

/**
 * Validator for {@link Review}.
 *
 * @since 1907
 */
public class TmaReviewValidator implements Validator
{
	private static final Logger LOGGER = Logger.getLogger(TmaReviewValidator.class);
	private static final String FIELD_REQUIRED_MESSAGE_ID = "field.required";
	private static final Double RATING_MIN = 0.5;
	private static final Double RATING_MAX = 5.0;

	@Override
	public boolean supports(final Class clazz)
	{
		return Review.class.equals(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors)
	{
		ValidationUtils.rejectIfEmpty(errors, HEADLINE, FIELD_REQUIRED_MESSAGE_ID);
		ValidationUtils.rejectIfEmpty(errors, COMMENT, FIELD_REQUIRED_MESSAGE_ID);
		ValidationUtils.rejectIfEmpty(errors, RATING, FIELD_REQUIRED_MESSAGE_ID);
		validateRating(errors);
	}

	protected void validateRating(final Errors errors)
	{
		final String rating = (String) errors.getFieldValue(RATING);

		if (rating == null)
		{
			errors.rejectValue(RATING, FIELD_REQUIRED_MESSAGE_ID);
		}
		else
		{
			try
			{
				Double ratingValue = Double.parseDouble(rating);

				if (ratingValue < RATING_MIN || ratingValue > RATING_MAX)
				{
					errors.rejectValue(RATING, "review.rating.invalid");
				}
			}
			catch (NumberFormatException e)
			{
				LOGGER.error("Number format exception, rating must be a number", e);
				errors.rejectValue(RATING, "review.rating.invalid");
			}
		}
	}

}
