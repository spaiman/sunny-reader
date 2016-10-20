package com.setiawanpaiman.sunnyreader.util;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by Setiawan Paiman on 20/10/16.
 */

public class ActivityUtil {

    public static void unlockScreen(final Activity activity) {
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }
}
