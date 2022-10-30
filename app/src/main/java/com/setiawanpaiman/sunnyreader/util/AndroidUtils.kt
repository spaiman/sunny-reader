package com.setiawanpaiman.sunnyreader.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlin.math.roundToInt

object AndroidUtils {
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .activeNetworkInfo != null
    }

    @JvmStatic
    fun setSwipeRefreshing(
        swipeRefreshLayout: SwipeRefreshLayout?,
        isRefreshing: Boolean
    ) {
        swipeRefreshLayout?.post { swipeRefreshLayout.isRefreshing = isRefreshing }
    }

    @JvmStatic
    @JvmOverloads
    fun trim(s: CharSequence, start: Int = 0, end: Int = s.length): CharSequence {
        var localStart = start
        var localEnd = end
        while (localStart < localEnd && Character.isWhitespace(s[localStart])) {
            localStart++
        }
        while (localEnd > localStart && Character.isWhitespace(s[localEnd - 1])) {
            localEnd--
        }
        return s.subSequence(localStart, localEnd)
    }

    @JvmStatic
    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }
}