package com.setiawanpaiman.sunnyreader.util;

import android.content.Context;
import android.net.ConnectivityManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;

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

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
