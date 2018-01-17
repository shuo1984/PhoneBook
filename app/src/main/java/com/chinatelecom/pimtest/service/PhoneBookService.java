package com.chinatelecom.pimtest.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.manager.SmsNotificationManager;
import com.chinatelecom.pimtest.model.Notification;
import com.chinatelecom.pimtest.receiver.ShortMessageReceiver;

/**
 * Created by Shuo on 2018/1/17.
 */

public class PhoneBookService extends Service {

    private SmsNotificationManager smsNotificationManager = SmsNotificationManager.getInstance();

    private Handler handler = new Handler(Looper.getMainLooper());

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public enum ContentType {
        RAWCONTACT(ContactsContract.RawContacts.CONTENT_URI),
        CONTACT(ContactsContract.Contacts.CONTENT_URI),
        CALLLOG(CallLog.Calls.CONTENT_URI),
        MESSAGE(IConstant.Message.THREADS_URI);

        Uri uri;

        ContentType(Uri uri) {
            this.uri = uri;
        }

        public Uri getUri() {
            return uri;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerContentObserver();
        receiveMessage();
    }

    private void registerContentObserver() {
        for (final ContentType contentType : ContentType.values()) {
            getContentResolver().registerContentObserver(contentType.getUri(), true,
               new ContentObserver(handler) {
                   @Override
                   public void onChange(boolean selfChange) {
                       Notification.Event event = null;
                       switch (contentType) {
                           case CONTACT:
                               event = Notification.Event.CONTACT_CHANGED;
                               //setContactNotify();
                               break;
                           case RAWCONTACT:
                               event = Notification.Event.CONTACT_CHANGED;
                               //setContactNotify();
                               break;
                           case CALLLOG:
                               event = Notification.Event.CALLLOG_CHANGED;
                               break;
                           case MESSAGE:
                               event = Notification.Event.MESSAGE_CHANGED;
                               SmsNotificationManager.getInstance().notifyChange(event, null);
                               break;
                       }

                   }

                });
        }
    }

    public void receiveMessage() {
        ShortMessageReceiver shortMessageReceiver = new ShortMessageReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED2");
        intentFilter.addAction("android.provider.Telephony.SMS_DELIVER_ACTION");
        intentFilter.addAction("android.provider.Telephony.SMS_DELIVER");
        PhoneBookService.this.registerReceiver(shortMessageReceiver, intentFilter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class SmsObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);

        }
    }
}
