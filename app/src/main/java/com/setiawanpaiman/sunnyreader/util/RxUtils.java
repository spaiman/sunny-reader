package com.setiawanpaiman.sunnyreader.util;

import rx.Subscription;

public final class RxUtils {

    private RxUtils() {
    }

    public static void unsubscribeAll(Subscription... subscriptions) {
        for (Subscription subscription : subscriptions) {
            if (subscription != null) subscription.unsubscribe();
        }
    }

    public static boolean isSubscriptionActive(Subscription subscription) {
        return (subscription != null && !subscription.isUnsubscribed());
    }
}