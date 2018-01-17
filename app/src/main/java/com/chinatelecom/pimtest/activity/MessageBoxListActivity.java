package com.chinatelecom.pimtest.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothClass;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.adapter.MessageBoxListAdapter;
import com.chinatelecom.pimtest.asyncTask.MessageSendTask;
import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.interfaces.NotificationListener;
import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.manager.MessageManager;
import com.chinatelecom.pimtest.manager.NotificationManager;
import com.chinatelecom.pimtest.manager.SmsNotificationManager;
import com.chinatelecom.pimtest.model.Message;
import com.chinatelecom.pimtest.model.Notification;
import com.chinatelecom.pimtest.model.SmsItem;
import com.chinatelecom.pimtest.utils.DateUtils;
import com.chinatelecom.pimtest.utils.DeviceUtils;
import com.chinatelecom.pimtest.utils.StringUtils;
import com.google.i18n.phonenumbers.Phonenumber;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageBoxListActivity extends AppCompatActivity {

    private ListView talkList;
    private List<Message> messages;
    private AsyncQueryHandler asyncQuery;
    private Button sendMsg;
    private EditText msg;
    private ImageView keyboard;
    private Context context;
    String threadID;
    String phoneNums;
    private NotificationManager notificationManager = SmsNotificationManager.getInstance();
    private MessageSentListener messageSentListener;
    private MessageChangeListener messageChangeListener;
    private Log logger = Log.build(MessageBoxListActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_box_list);
        context = MessageBoxListActivity.this;
        threadID = getIntent().getStringExtra("threadId");
        phoneNums = getIntent().getStringExtra("phoneNumber");
        messageSentListener = new MessageSentListener();
        messageChangeListener = new MessageChangeListener();
        setupViews();
        setupListeners();

        init(threadID);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(notificationManager!=null) {
            notificationManager.unregisterListener(Notification.Event.SMS_SENT,messageSentListener);
            notificationManager.unregisterListener(Notification.Event.MESSAGE_CHANGED,messageChangeListener);
        }

    }

    private void setupListeners() {
        sendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] smsPermissions= new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.SEND_SMS,
                        };
                MPermissions.requestPermissions(MessageBoxListActivity.this,IConstant.Permissions.REQ_CODE_SMS,smsPermissions);
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchKeyBoard();
            }
        });

        notificationManager.registerListener(Notification.Event.MESSAGE_CHANGED,messageChangeListener);
        notificationManager.registerListener(Notification.Event.SMS_SENT, messageSentListener);
    }

    private void setupViews() {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(StringUtils.join(phoneNums));
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeviceUtils.hideKeyBoard(MessageBoxListActivity.this,msg);
                    finish();
                }
            });

            sendMsg = (Button)findViewById(R.id.send_btn);
            msg = (EditText)findViewById(R.id.send_text);
            msg.requestFocus();
            keyboard = (ImageView)findViewById(R.id.keyboard);
        }catch (Exception e){
            e.printStackTrace();
        }


    }


    private void init(final String threadID) {
        asyncQuery = new MessageAsynQueryHandler(getContentResolver());
        talkList = (ListView) findViewById(R.id.message_list);
        messages = new ArrayList<Message>();

       /* String[] projection = new String[] { "date", "address", "person",
                "body", "type" };*/

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                String[] projection = new String[]{  IConstant.Message.Sms.DATE,
                        IConstant.Message.Sms.ADDRESS, IConstant.Message.Sms.BODY,
                        IConstant.Message.Sms.READ, IConstant.Message.Sms.TYPE
                };

                asyncQuery.startQuery(0, null, IConstant.Message.MESSAGE_URI, projection,
                        "thread_id = " + threadID  + " and type!=3" , null, "date asc");
                return null;
            }
        }.execute();

       /* try {
            List<Message> threadMessages = new ArrayList<>();
            for (SmsItem sms: MessageCacheManager.getDefaultCacheList()) {
                if(sms.getThreadId().equals(threadID)){
                    Message m = new Message();
                    m.setName(sms.getAddress());
                    m.setDate(DateUtils.format(sms.getDate()));
                    m.setText(sms.getBody());
                    if(sms.getType()==1){
                        m.setLayoutID(R.layout.list_say_he_item);
                    }else{
                        m.setLayoutID(R.layout.list_say_me_item);
                    }
                    threadMessages.add(m);
                }
            }
            if (threadMessages.size() > 0) {
                talkList.setAdapter(new MessageBoxListAdapter(
                        MessageBoxListActivity.this, threadMessages));
                talkList.setSelection(messages.size());
            } else {
                Toast.makeText(MessageBoxListActivity.this, "没有会话消息",
                        Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.d("####MessageBoxList", e.getMessage());
        }*/

    }

    private class MessageAsynQueryHandler extends AsyncQueryHandler {

        public MessageAsynQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    long millisec = cursor.getLong(cursor.getColumnIndex("date"));
                    String date = DateUtils.format(millisec);
                    if (cursor.getInt(cursor.getColumnIndex("type")) == 1) {
                        Message d = new Message(
                                cursor.getString(cursor
                                        .getColumnIndex("address")),
                                date,
                                cursor.getString(cursor.getColumnIndex("body")),
                                R.layout.list_say_he_item);
                        messages.add(d);
                    } else {
                        Message d = new Message(
                                cursor.getString(cursor
                                        .getColumnIndex("address")),
                                date,
                                cursor.getString(cursor.getColumnIndex("body")),
                                R.layout.list_say_me_item);
                        messages.add(d);
                    }
                }
                if (messages.size() > 0) {
                    talkList.setAdapter(new MessageBoxListAdapter(
                            MessageBoxListActivity.this, messages));
                    talkList.setSelection(messages.size());
                } else {
                    Toast.makeText(MessageBoxListActivity.this, "??????????????×÷",
                            Toast.LENGTH_SHORT).show();
                }
            }
            super.onQueryComplete(token, cookie, cursor);
        }
    }

    private void switchKeyBoard(){
        DeviceUtils.showKeyBoard(MessageBoxListActivity.this,msg);
    }


    private void sendTextMessage(final SmsItem messageInfo) {
        if (!DeviceUtils.isDefaultMessageApp(MessageBoxListActivity.this)) {
            DeviceUtils.setDefaultMessageApp(MessageBoxListActivity.this);
            return;
        }
        if (messageInfo.getAddress() == null) {
            Toast.makeText(context,"收信人不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isBlank(messageInfo.getBody())) {
            Toast.makeText(context,"信息内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        MessageSendTask sendTask = new MessageSendTask(this, messageInfo);
        sendTask.execute();
        msg.setText("");
        DeviceUtils.hideKeyBoard(context,msg);
    }





    private void buildAndSendMessage(){
        SmsItem mi = new SmsItem();
        mi.setAddress(phoneNums);
        mi.setBody(msg.getText().toString());
        mi.setThreadId(threadID);
        mi.setRead("1");
        mi.setType(IConstant.Message.SMS_MESSAGE_TYPE_OUTBOX);
        mi.setSmsStatus(IConstant.Message.SMS_STATUS_PENDING);
        //  mi.setSubId(adapter.getSubId());
        sendTextMessage(mi);
    }


    private class MessageSentListener implements NotificationListener{
        @Override
        public void onChange(Notification notification) {
            SmsItem smsItem = (SmsItem) notification.getData();
            String messageID = "";
            String address ="";
            if(smsItem!=null){
                messageID = smsItem.getMessageId();
                address = smsItem.getAddress();
            }
            logger.debug("短信id：" + messageID + "已发送至" + address);
            init(threadID);
        }
    }

    private class MessageChangeListener implements NotificationListener{

        @Override
        public void onChange(Notification notification) {
            init(threadID);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(MessageBoxListActivity.this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(IConstant.Permissions.REQ_CODE_SMS)
    public void requestAllPermissionSuccess(){
        Toast.makeText(MessageBoxListActivity.this,"权限申请成功!",Toast.LENGTH_SHORT).show();
        buildAndSendMessage();
    }

    @PermissionDenied(IConstant.Permissions.REQ_CODE_SMS)
    public void requestAllPermissionFail(){
        Toast.makeText(MessageBoxListActivity.this,"权限申请失败!",Toast.LENGTH_SHORT).show();
    }




}
