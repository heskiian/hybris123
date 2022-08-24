/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.agreementtmfwebservices.swagger;

import java.util.Map;
import java.util.regex.Pattern;

import io.swagger.codegen.languages.SpringCodegen;



/**
 * Swagger code generation customization
 *
 * @since 2108
 */
public class AgreementsCodegen extends SpringCodegen
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
