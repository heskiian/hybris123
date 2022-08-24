/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */

package de.hybris.platform.upilintegrationservices.odata.feed.edmx.generate.impl;



import de.hybris.platform.upilintegrationservices.odata.feed.edmx.UpilEdmxMetadataElementParser;
import de.hybris.platform.upilintegrationservices.odata.feed.edmx.parser.UpilEdmxMetadataParser;

import java.io.File;
import java.io.FileInputStream;


/**
 * Stand alone class for converting EDMX to POJO classes
 *
 * @{since 1911}
 */
public class UpilEdmxPojoGenerateService
{

	/**
	 * This is a main method get's triggered during build to generate EDMX pojo's from provided EDMX meta data file to
	 * given directory.
	 *
	 * @param args
	 *           Arguments array of String,with arguments as metadata xml file and directory path for pojo's
	 * @throws Exception
	 *            Throws Exception
	 */
	public static void main(final String[] args) throws Exception
	{

		final File sourceDir = new File(args[3]);

		final File metadataFile = new File(args[4]);

		if (!metadataFile.exists())
		{
			throw new IllegalArgumentException(
					"Metadata file does not exist: " + "metadataFilePath");
		}
		final UpilEdmxMetadataParser parser = new UpilEdmxMetadataParser();
		final UpilEdmxMetadataElementParser metadata = parser.parseXml(new FileInputStream(metadataFile));
		new UpilEdmxPojoGenerator(sourceDir).upilGenerate(metadata);

	}

}
