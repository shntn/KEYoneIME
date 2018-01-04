package com.zemle.keyoneime;

import android.util.Log;


/**
 * Created by shntn on 2018/01/04.
 */

public class KoimeLog {
    private static final String LOGTAG = "Koime";
    private static final int LOGGABLE = Log.VERBOSE;

    private KoimeLog() { }

    public static boolean isLoggable(int logLevel) {
        // 端末のROOT権限がないと、"/data/local.prop" を変更できない？
        // return Log.isLoggable(LOGTAG, logLevel);
        return logLevel >= LOGGABLE;
    }

    public static void v(String msg) {
        if (isLoggable(Log.VERBOSE)) {
            Log.v(LOGTAG, msg);
        }
    }

    public static void v(String msg, Throwable e) {
        if (isLoggable(Log.VERBOSE)) {
            Log.v(LOGTAG, msg, e);
        }
    }

    public static void d(String msg) {
        if (isLoggable(Log.DEBUG)) {
            Log.v(LOGTAG, msg);
        }
    }

    public static void d(String msg, Throwable e) {
        if (isLoggable(Log.DEBUG)) {
            Log.v(LOGTAG, msg, e);
        }
    }

    public static void i(String msg) {
        if (isLoggable(Log.INFO)) {
            Log.v(LOGTAG, msg);
        }
    }

    public static void i(String msg, Throwable e) {
        if (isLoggable(Log.INFO)) {
            Log.v(LOGTAG, msg, e);
        }
    }

    public static void w(String msg) {
        if (isLoggable(Log.WARN)) {
            Log.v(LOGTAG, msg);
        }
    }

    public static void w(String msg, Throwable e) {
        if (isLoggable(Log.WARN)) {
            Log.v(LOGTAG, msg, e);
        }
    }

    public static void e(String msg) {
        if (isLoggable(Log.ERROR)) {
            Log.v(LOGTAG, msg);
        }
    }

    public static void e(String msg, Throwable e) {
        if (isLoggable(Log.ERROR)) {
            Log.v(LOGTAG, msg, e);
        }
    }
}
