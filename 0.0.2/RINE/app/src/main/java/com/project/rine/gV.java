package com.project.rine;

import android.util.Log;

import java.math.BigInteger;

/*
* gV: global variables and functions
*
* verbose: boolean, do more print in debug mode
*
* xLog: if verbose, log message
* */
public class gV {
    public static boolean verbose = true;

    public static int userId = 0;

    public static int dstId = 0;

    public static BigInteger dstKeyN;
    public static BigInteger dstKeyE;

    public static void xLog(String tag, String msg) {
        /*
        * Using Log.d to do verbose debug log.
        * */
        if (verbose) {
            Log.d(tag, msg);
        }
    }

}
