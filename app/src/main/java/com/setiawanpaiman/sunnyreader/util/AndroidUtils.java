package com.setiawanpaiman.sunnyreader.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.widget.SwipeRefreshLayout;

public final class AndroidUtils {

    public static boolean isNetworkAvailable(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo() != null;
    }

    public static void setSwipeRefreshing(final SwipeRefreshLayout swipeRefreshLayout,
                                          final boolean isRefreshing) {
        if(swipeRefreshLayout != null) {
            swipeRefreshLayout.post(new Runnable() {
                public void run() {
                    swipeRefreshLayout.setRefreshing(isRefreshing);
                }
            });
        }
    }
}
