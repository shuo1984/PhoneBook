package com.chinatelecom.pimtest.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Shuo on 2017/12/29.
 */

public class MmsReceiver extends BroadcastReceiver {
    private Context context;
    public static final String  MMS_RECEIVE_ACTION = "android.provider.Telephony.WAP_PUSH_RECEIVED";
    public static long date = 0;
    byte[] TransactionId;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String action = intent.getAction();
        if (action.equals(MMS_RECEIVE_ACTION)) {

        }
    }
}
