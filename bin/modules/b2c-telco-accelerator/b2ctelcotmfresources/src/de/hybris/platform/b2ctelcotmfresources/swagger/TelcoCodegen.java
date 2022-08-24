/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.b2ctelcotmfresources.swagger;

import java.util.Map;
import java.util.regex.Pattern;

import io.swagger.codegen.languages.SpringCodegen;


/**
 * Swagger code generation customization
 *
 * @since 2102
 */
public class TelcoCodegen extends SpringCodegen
{

	@Override
	public String sanitizeName(String name)
	{

		for (Map.Entry<String, String> specialCharacter : specialCharReplacements.entrySet())
		{
			name = name.replaceAll(Pattern.quote(specialCharacter.getKey()), specialCharacter.getValue());
		}

		return name;
	}
}
