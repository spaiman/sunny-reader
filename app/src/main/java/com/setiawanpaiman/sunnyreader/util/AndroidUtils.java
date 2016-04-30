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

    public static CharSequence trim(CharSequence s) {
        return trim(s, 0, s.length());
    }

    public static CharSequence trim(CharSequence s, int start, int end) {
        while (start < end && Character.isWhitespace(s.charAt(start))) {
            start++;
        }

        while (end > start && Character.isWhitespace(s.charAt(end - 1))) {
            end--;
        }

        return s.subSequence(start, end);
    }
}
