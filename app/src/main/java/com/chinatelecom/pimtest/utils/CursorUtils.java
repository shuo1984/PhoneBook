package com.chinatelecom.pimtest.utils;

import android.database.Cursor;

/**
 * Created by Shuo on 2016/10/8.
 */

public class CursorUtils {

    public static String getString(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            String val = cursor.getString(cursor.getColumnIndexOrThrow(colName));
            return StringUtils.trimToEmpty(val);
        }
        return "";
    }

    public static String getStringAllowNullReturn(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            String val = cursor.getString(cursor.getColumnIndexOrThrow(colName));
            return val;
        }
        return null;
    }

    public static int getInt(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(colName));
        }
        return 0;
    }

    public static long getLong(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getLong(cursor.getColumnIndexOrThrow(colName));
        }
        return 0;
    }

    public static boolean getBoolean(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getInt(cursor.getColumnIndexOrThrow(colName)) > 0;
        }
        return false;
    }

    public static float getFloat(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getFloat(cursor.getColumnIndexOrThrow(colName));
        }
        return 0;
    }

    public static double getDouble(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getDouble(cursor.getColumnIndexOrThrow(colName));
        }
        return 0;
    }

    public static byte[] getByteArray(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getBlob(cursor.getColumnIndexOrThrow(colName));
        }
        return null;
    }

    public static byte[] getBlob(Cursor cursor, String colName) {
        if (cursor.getColumnIndex(colName) >= 0) {
            return cursor.getBlob(cursor.getColumnIndexOrThrow(colName));
        }
        return null;
    }

}