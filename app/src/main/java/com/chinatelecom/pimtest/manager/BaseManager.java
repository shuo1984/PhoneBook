package com.chinatelecom.pimtest.manager;

import android.content.ContentResolver;
import android.content.Context;

import com.chinatelecom.pimtest.IApplication;
import com.chinatelecom.pimtest.log.Log;

/**
 * Created by Shuo on 2018/1/15.
 */

public class BaseManager {
    protected Log logger = Log.build(BaseManager.class);
    protected Context context = IApplication.getContext();
    protected ContentResolver contentResolver = IApplication.getContext().getContentResolver();
}
