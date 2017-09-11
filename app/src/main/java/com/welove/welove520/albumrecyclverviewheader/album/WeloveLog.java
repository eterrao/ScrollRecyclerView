package com.welove.welove520.albumrecyclverviewheader.album;

import android.util.Log;

/**
 * Created by Raomengyang on 17-9-11.
 * Email    : ericrao@welove-inc.com
 * Desc     :
 * Version  : 1.0
 */

public class WeloveLog {
    /**
     * 为了DEBUG使用,除调试之外不应使用该方法
     *
     * @param msg
     */
    public static void debug(String msg) {
        debug("", msg);
    }

    public static void debug(String tag, String msg) {
        e(" xxxxxxxx===> " + tag, msg);
    }

    private static void e(String s, String msg) {
        Log.e(s, msg);
    }


}
