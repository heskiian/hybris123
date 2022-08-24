/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2ctelcoservices.interceptors;


import static org.mockito.MockitoAnnotations.initMocks;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.b2ctelcoservices.model.TmaBusinessPartnerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

/**
 * Unit test for {@link TmaBusinessPartnerPrepareInterceptor}.
 *
 * @since 1911
 */
@UnitTest
public class TmaBusinessPartnerPrepareInterceptorTest {

    private static final String TEST_BUSINESS_PARTNER_ID = "testBusinessPartnerId";

    private TmaBusinessPartnerPrepareInterceptor tmaBusinessPartnerPrepareInterceptor;

    @Mock
    private InterceptorContext interceptorContext;

    @Mock
    private KeyGenerator tmaBusinessPartnerIdGenerator;

    @Before
    public void setUp()
    {
		initMocks(this);
		tmaBusinessPartnerIdGenerator = Mockito.mock(KeyGenerator.class);
		this.tmaBusinessPartnerPrepareInterceptor = new TmaBusinessPartnerPrepareInterceptor(tmaBusinessPartnerIdGenerator);
		Mockito.when(tmaBusinessPartnerIdGenerator.generate()).thenReturn(TEST_BUSINESS_PARTNER_ID);
    }

    @Test
    public void testBusinessPartnerForNewModel() throws InterceptorException
    {
        final TmaBusinessPartnerModel tmaBusinessPartnerModel = new TmaBusinessPartnerModel();
        Mockito.when(interceptorContext.isNew(Mockito.any())).thenReturn(true);

        tmaBusinessPartnerPrepareInterceptor.onPrepare(tmaBusinessPartnerModel, interceptorContext);
        Assert.assertNotNull(tmaBusinessPartnerModel.getCommBusinessPartnerId());
    }

    @Test
    public void testBusinessPartnerForModifiedModel() throws InterceptorException
    {
        final TmaBusinessPartnerModel tmaBusinessPartnerModel = new TmaBusinessPartnerModel();
        Mockito.when(interceptorContext.isNew(Mockito.any())).thenReturn(false);

        tmaBusinessPartnerPrepareInterceptor.onPrepare(tmaBusinessPartnerModel, interceptorContext);
        Assert.assertNull(tmaBusinessPartnerModel.getCommBusinessPartnerId());
    }
}
