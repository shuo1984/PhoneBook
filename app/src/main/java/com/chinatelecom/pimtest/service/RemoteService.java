package com.chinatelecom.pimtest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.chinatelecom.pim.IMyAidlInterface;

/**
 * Created by Shuo on 2017/5/10.
 */

public class RemoteService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this,RemoteService.class);
        this.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private IMyAidlInterface.Stub stub = new IMyAidlInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getCount() throws RemoteException {
            return 111;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }


}
