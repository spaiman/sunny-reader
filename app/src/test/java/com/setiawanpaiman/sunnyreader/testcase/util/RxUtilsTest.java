package com.setiawanpaiman.sunnyreader.testcase.util;

import com.setiawanpaiman.sunnyreader.testcase.BaseTest;
import com.setiawanpaiman.sunnyreader.util.RxUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricGradleTestRunner;

import rx.Subscription;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricGradleTestRunner.class)
public class RxUtilsTest extends BaseTest {

    @Rule
    public MockitoRule mMockitoRule = MockitoJUnit.rule();

    @Mock
    Subscription mSubscription1;

    @Mock
    Subscription mSubscription2;

    @Test
    public void testIsSubscriptionActive() throws Exception {
        when(mSubscription1.isUnsubscribed()).thenReturn(true);
        when(mSubscription2.isUnsubscribed()).thenReturn(false);

        boolean result = RxUtils.isSubscriptionActive(mSubscription1);
        assertEquals(false, result);

        result = RxUtils.isSubscriptionActive(mSubscription2);
        assertEquals(true, result);

        result = RxUtils.isSubscriptionActive(null);
        assertEquals(false, result);
    }

    @Test
    public void testUnsubscribeAll() throws Exception {
        RxUtils.unsubscribeAll(mSubscription1, mSubscription2);
        verify(mSubscription1, times(1)).unsubscribe();
        verify(mSubscription2, times(1)).unsubscribe();
    }

    @Test
    public void testUnsubscribeAllNullValues() throws Exception {
        RxUtils.unsubscribeAll(null, mSubscription2);
        verify(mSubscription1, times(0)).unsubscribe();
        verify(mSubscription2, times(1)).unsubscribe();
    }
}
