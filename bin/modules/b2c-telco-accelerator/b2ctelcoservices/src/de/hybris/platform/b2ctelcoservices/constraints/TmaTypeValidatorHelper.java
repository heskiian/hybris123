/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.constraints;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.validation.messages.ResourceBundleProvider;
import de.hybris.platform.webservicescommons.util.YSanitizer;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Resource;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * A helper class for validators to construct multiple error messages using same validator class.
 *
 * @since 18.05
 */
public class TmaTypeValidatorHelper
{
	private static final Logger LOG = LoggerFactory.getLogger(TmaTypeValidatorHelper.class);

	private static final Pattern MESSAGE_PARAMETER_PATTERN = Pattern.compile("\\{([^\\}]+?)\\}");

	@Resource(name = "i18nService")
	private I18NService i18nService;

	@Resource(name = "resourceBundleProvider")
	private ResourceBundleProvider resourceBundleProvider;

	protected void buildErrorMessage(@Nonnull final ConstraintValidatorContext context,
			String template, final Object... args)
	{
		validateParameterNotNull(context, "Validation context can not be null");
		final Matcher matcher = MESSAGE_PARAMETER_PATTERN.matcher(template);
		if (matcher.matches())
		{
			final String resourceId = matcher.group(1);
			template = getLocalizedString(resourceId, getCurrentLocale());
		}
		else
		{
			template = template
					.replace("^\\{\\{", "{")
					.replace("\\}\\}$", "}");
		}
		String message;
		if (args.length > 0)
		{
			message = MessageFormat.format(template, args);
		}
		else
		{
			message = template;
		}

		final ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder = context
				.buildConstraintViolationWithTemplate(YSanitizer.sanitize(message));
		context.disableDefaultConstraintViolation();
		constraintViolationBuilder.addConstraintViolation();
	}

	protected String getLocalizedString(final String key, final Locale locale)
	{
		final ResourceBundleProvider bundleProvider = getResourceBundleProvider();
		if (bundleProvider == null)
		{
			throw new IllegalStateException(MessageFormat.format(
					"Field {0}#resourceBundleProvider has not been initialized. Probably forgot to autowire the instance?",
					getClass().getName()
			));
		}
		ResourceBundle bundle = bundleProvider.getResourceBundle(locale);
		if (bundle == null)
		{
			bundle = bundleProvider.getResourceBundle(Locale.ENGLISH);
			if (bundle == null)
			{
				LOG.warn(MessageFormat.format("String with id '{0}' has no localization for locale {1}", key, locale.toString()));
				return MessageFormat.format("#{0}", key);
			}
		}
		return bundle.getString(key);
	}

	protected Locale getCurrentLocale()
	{
		final I18NService localizationService = getI18nService();
		if (localizationService == null)
		{
			throw new IllegalStateException(MessageFormat.format(
					"Field {0}#i18nService has not been initialized. Probably forgot to autowire the instance?",
					getClass().getName()));
		}
		return localizationService.getCurrentLocale();
	}

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	protected ResourceBundleProvider getResourceBundleProvider()
	{
		return resourceBundleProvider;
	}
}
