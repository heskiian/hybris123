/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcowebservices.exceptions;

import de.hybris.platform.core.model.c2l.LanguageModel;

import javax.servlet.ServletException;


/**
 * Thrown when language is not supported for current base store configuration.
 * 
 * @since 1810
 */
public class TmaUnsupportedLanguageException extends ServletException
{

	private final LanguageModel language;

	/**
	 * @param languageToSet
	 */
	public TmaUnsupportedLanguageException(final LanguageModel languageToSet)
	{
		super("Language " + languageToSet + " is not supported by the current base store");
		this.language = languageToSet;
	}

	public TmaUnsupportedLanguageException(final LanguageModel languageToSet, final Throwable rootCause)
	{
		super("Language " + languageToSet + " is not supported by the current base store", rootCause);
		this.language = languageToSet;
	}

	/**
	 * @param msg
	 */
	public TmaUnsupportedLanguageException(final String msg)
	{
		super(msg);
		language = null;
	}

	public TmaUnsupportedLanguageException(final String msg, final Throwable rootCause)
	{
		super(msg, rootCause);
		language = null;
	}

	/**
	 * @return the language
	 */
	public LanguageModel getLanguage()
	{
		return language;
	}
}
