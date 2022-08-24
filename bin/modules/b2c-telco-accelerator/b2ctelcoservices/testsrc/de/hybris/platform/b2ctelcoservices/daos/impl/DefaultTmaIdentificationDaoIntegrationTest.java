/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.daos.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.b2ctelcoservices.daos.TmaIdentificationDao;
import de.hybris.platform.b2ctelcoservices.model.TmaIdentificationModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Test class for {@link DefaultTmaIdentificationDao}
 *
 * @since 1911
 */

@IntegrationTest
public class DefaultTmaIdentificationDaoIntegrationTest  extends ServicelayerTransactionalTest {

    private static final String DRIVING_LICENSE = "DRIVING_LICENSE";
    private static final String IDENTIFICATION_NUMBER = "NHL12506878";
    private static final String IDENTIFICATION_NUMBER_NON_EXISTENT = "999999";


    @Resource
    private TmaIdentificationDao tmaIdentificationDao;


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/test/impex/test_identification.impex", "utf-8");
    }

    @Test
    public void testFindIdentification()
    {
        TmaIdentificationModel tmaIdentificationModel = tmaIdentificationDao.getTmaIdentificationModelForTypeAndNumber(DRIVING_LICENSE, IDENTIFICATION_NUMBER);
        assertNotNull("The tmaIdentificationModel is not null", tmaIdentificationModel);
        assertEquals("The tmaIdentificationModel have the expected id", tmaIdentificationModel.getIdentificationNumber(), IDENTIFICATION_NUMBER);
    }

    @Test
    public void testFindNoIdentification()
    {
        TmaIdentificationModel tmaIdentificationModel = tmaIdentificationDao.getTmaIdentificationModelForTypeAndNumber(DRIVING_LICENSE, IDENTIFICATION_NUMBER_NON_EXISTENT);
        assertNull("The tmaIdentificationModel is null", tmaIdentificationModel);
    }
}
