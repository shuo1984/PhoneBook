package com.chinatelecom.pimtest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Shuo on 2017/12/29.
 */

public class HeadlessSmsSendService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
