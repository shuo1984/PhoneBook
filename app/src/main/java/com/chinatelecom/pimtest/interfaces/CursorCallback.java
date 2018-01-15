package com.chinatelecom.pimtest.interfaces;

import android.database.Cursor;

/**
 * Created by Shuo on 2016/10/8.
 */
public interface CursorCallback {

    Object VETO = new Object();

    public Object doInCursor(Cursor cursor);
}
