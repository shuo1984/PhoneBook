package com.chinatelecom.pimtest.manager;

import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;

import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.interfaces.Transformer;
import com.chinatelecom.pimtest.model.SmsItem;
import com.chinatelecom.pimtest.utils.CursorTemplate;
import com.chinatelecom.pimtest.utils.CursorUtils;
import com.chinatelecom.pimtest.utils.PhoneUtils;

import java.util.ArrayList;

/**
 * Created by Shuo on 2018/1/15.
 */

public class MessageManager extends BaseManager{

    private SmsManager smsManager;

    public long insertSendSms(SmsItem info) {
        ContentValues values = new ContentValues();

        // 发送号码
        values.put("address", info.getAddress());
        // 发送内容
        values.put("body", info.getBody());
        // 发送时间
        values.put("date", System.currentTimeMillis());
        // 阅读状态   0:未读， 1:已读
        values.put("read", info.getRead().equals("1") ? 1 : 0);

        //ThreadID
        values.put("thread_id", info.getThreadId());
      /*  if (Device.isDualSimSupport()) {
            //双卡标识
            values.put(deviceFactory.GetSmsSubIdColumnName(), info.getSubId());
        }*/
        //protocol => 协议 0 SMS_RPOTO, 1 MMS_PROTO
        // values.put("protocal",info.getType());
        long shortMessageId = ContentUris.parseId(contentResolver.insert(IConstant.Message.SENT_MESSAGE_URI, values));
        logger.debug("新建短信shortMessageId=%d", shortMessageId);
        info.setMessageId(String.valueOf(shortMessageId));

        return Long.valueOf(info.getMessageId());
    }


    public void sendTextMessage(Context context, String address, long messageId, String content, int subId) {
       /* if (DualSimTelephonyUtils.isDoubleSim(context) != null) {
            DualSimTelephonyUtils.doMessage(context, address, messageId, content, subId);
        } else {*/
            smsManager = android.telephony.SmsManager.getDefault();
            Bundle bundle = new Bundle();
            bundle.putString(IConstant.Params.NUMBER, address);
            bundle.putLong(IConstant.Params.MESSAGE_ID, messageId);
            Intent sentIntent = new Intent(IConstant.Action.SMS_SENT);
            Intent deliverIntent = new Intent(IConstant.Action.SMS_DELIVER);
            sentIntent.putExtras(bundle);
            PendingIntent sentPendingIntent = PendingIntent.getBroadcast(context, 0, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent deliveredPendingIntent = PendingIntent.getBroadcast(context, 0, deliverIntent, 0);

            ArrayList<String> parts = smsManager.divideMessage(content);
            ArrayList<PendingIntent> sentIntents = new ArrayList<PendingIntent>();
            sentIntents.add(sentPendingIntent);
            ArrayList<PendingIntent> deliveredIntents = new ArrayList<PendingIntent>();
            deliveredIntents.add(deliveredPendingIntent);
            address = PhoneUtils.getNumber(address);
            smsManager.sendMultipartTextMessage(address, null, parts, sentIntents, deliveredIntents);
       // }

    }

    public long findThreadIdByMessageId(long messageId) {
        Cursor cursor = contentResolver.query(IConstant.Message.MESSAGE_URI,
                new String[]{IConstant.Message.Sms._ID, IConstant.Message.Sms.THREAD_ID},
                "_id=?",
                new String[]{Long.toString(messageId)},
                null
        );
        return CursorTemplate.one(cursor, new Transformer<Cursor, Long>() {
            @Override
            public Long transform(Cursor input) {
                return CursorUtils.getLong(input, IConstant.Message.Sms.THREAD_ID);
            }
        });
    }


    public int getThreadId(final String number) {
        Uri.Builder uriBuilder = Uri.parse("content://mms-sms/threadID").buildUpon();
        uriBuilder.appendQueryParameter("recipient", number);
        return CursorTemplate.one(contentResolver.query(uriBuilder.build(), new String[]{"_id"}, null, null, null), new Transformer<Cursor, Integer>() {
            @Override
            public Integer transform(Cursor input) {
                int threadId = CursorUtils.getInt(input, "_id");
                return threadId;
            }
        });
    }

    public long updateSMSBox(SmsItem info) {
    /*    logger.debug("update messagebox with info Msgid: %d, content:%s, Stauts:%d, Type:%d",
                info.getId(), info.getBody(), info.getSmsStatus().getValue(), info.getSmsType().getValue());*/
        try {
            ContentValues values = new ContentValues();

            // 插入或更新短信库
            if ("0".equals(info.getMessageId())) {
                // 发送号码
                values.put("address", info.getAddress());
                // 发送内容
                values.put("body", info.getBody());
                // 发送时间
                values.put("date", System.currentTimeMillis());
                // 阅读状态   0:未读， 1:已读
                values.put("read", "1".equals(info.getRead()) ? 1 : 0);
                // 类型：0:All 1:INBOX，2:SENT 3:DRAFT 4:OUTBOX 5:FAILED 6:QUEUED
                values.put("type", info.getType());
                //状态: -1:接收，0:complete, 32:pending, 64:failed
                values.put("status", info.getSmsStatus());
               /* if (Device.isDualSimSupport()) {
                    //双卡标识
                    values.put(deviceFactory.GetSmsSubIdColumnName(), info.getSubId());
                }*/
                //protocol => 协议 0 SMS_RPOTO, 1 MMS_PROTO
                // values.put("protocal",info.getType());
                long shortMessageId = ContentUris.parseId(contentResolver.insert(IConstant.Message.MESSAGE_URI, values));
                logger.debug("新建短信shortMessageId=%d", shortMessageId);
                info.setMessageId(String.valueOf(shortMessageId));
            } else {
                // 类型：0:All 1:INBOX，2:SENT 3:DRAFT 4:OUTBOX 5:FAILED 6:QUEUED
                values.put("type", info.getType());
                //状态: -1:接收，0:complete, 32:pending, 64:failed
                values.put("status", info.getSmsStatus());
                contentResolver.update(ContentUris.withAppendedId(IConstant.Message.MESSAGE_URI, Long.parseLong(info.getMessageId())),
                        values, null, null);
            }

        } catch (Exception e) {
            logger.error("%s", e.getMessage());
        }
        return Long.parseLong(info.getMessageId());
    }


}
