package com.chinatelecom.pimtest.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.manager.MessageManager;
import com.chinatelecom.pimtest.manager.NotificationManager;
import com.chinatelecom.pimtest.manager.SmsNotificationManager;
import com.chinatelecom.pimtest.model.Notification;
import com.chinatelecom.pimtest.model.SmsItem;
import com.chinatelecom.pimtest.utils.DeviceUtils;

/**
 * Created by Shuo on 2017/12/29.
 */

public class ShortMessageReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String SMS_DELIVER = "android.provider.Telephony.SMS_DELIVER";
    private Log logger = Log.build(ShortMessageReceiver.class);
    private NotificationManager notificationManager;
    private MessageManager messageManager = new MessageManager();
    @Override
    public void onReceive(Context context, Intent intent) {
        logger.debug("ShortMessageReceiver onReceive==== action:" + intent.getAction());
        logger.debug("API Version===" + Build.VERSION.SDK_INT);
        logger.debug("IsDefaultSmsApp:" + DeviceUtils.isDefaultMessageApp(context));
        Cursor cursor = null;
        try {
            if (SMS_RECEIVED.equals(intent.getAction())) {
                if(Build.VERSION.SDK_INT>=19) {
                    if (DeviceUtils.isDefaultMessageApp(context)) {
                        logger.debug("Message received.......");
                        receiveMsg(context,intent,cursor);
                    }
                }else{
                    receiveMsg(context,intent,cursor);
                }
            }else if(IConstant.Action.SMS_SENT.equals(intent.getAction())){
                String number = intent.getStringExtra(IConstant.Params.NUMBER);
                Long id = intent.getLongExtra(IConstant.Params.MESSAGE_ID, -1);
                if(getResultCode()== Activity.RESULT_OK){
                    SmsItem smsItem = new SmsItem();
                    smsItem.setMessageId(String.valueOf(id));
                    smsItem.setAddress(number);
                    smsItem.setType(IConstant.Message.SMS_MESSAGE_TYPE_SENT);

                    notificationManager = SmsNotificationManager.getInstance();
                    notificationManager.notifyChange(Notification.Event.SMS_SENT,smsItem);

                }else if(getResultCode() == SmsManager.RESULT_ERROR_GENERIC_FAILURE ||
                        getResultCode() == SmsManager.RESULT_ERROR_NO_SERVICE ||
                        getResultCode() == SmsManager.RESULT_ERROR_RADIO_OFF ||
                        getResultCode() == SmsManager.RESULT_ERROR_NULL_PDU  ){

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Exception : " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }


    private void receiveMsg(Context context, Intent intent,Cursor cursor){
        logger.debug("sms received!");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            final SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            if (messages.length > 0) {
                String msgBody = messages[0].getMessageBody();
                String msgAddress = messages[0].getOriginatingAddress();
                long msgDate = messages[0].getTimestampMillis();
                String smsToast = "New SMS received from : "
                        + msgAddress + "\n'"
                        + msgBody + "'";
                Toast.makeText(context, smsToast, Toast.LENGTH_LONG).show();
                logger.debug("message from: " + msgAddress + ", message body: " + msgBody
                        + ", message date: " + msgDate);
                popupMsgWindow();
                long id = addSmsToDB(msgAddress, msgBody, msgDate);
                abortBroadcast();
            }
        }


       /* cursor = context.getContentResolver().query(Uri.parse("content://sms"), new String[] { "_id", "address", "read", "body", "date" }, "read = ? ", new String[] { "0" }, "date desc");
        if (null == cursor){
            return;
        }*/
    }

    private void popupMsgWindow() {

    }

    private long addSmsToDB(String number, String content, Long time) {
        SmsItem mi = new SmsItem();
        mi.setMessageId("0");
        mi.setRead("0");
        mi.setAddress(number);
        mi.setBody(content);
        mi.setDate(time);
        mi.setType(IConstant.Message.SMS_MESSAGE_TYPE_INBOX);
        mi.setSmsStatus(IConstant.Message.SMS_STATUS_COMPLETE);

      /*  if(Device.isDualSimSupport()){
            mi.setSubId(smsSubId);
        }*/

        long id = messageManager.updateSMSBox(mi);
        return id;
    }

}
