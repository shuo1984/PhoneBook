package com.chinatelecom.pimtest.activity;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.adapter.MessageBoxListAdapter;
import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.model.Message;
import com.chinatelecom.pimtest.utils.DateUtils;
import com.google.i18n.phonenumbers.Phonenumber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageBoxListActivity extends AppCompatActivity {

    private ListView talkList;
    private List<Message> messages;
    private AsyncQueryHandler asyncQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_box_list);
        String threadID = getIntent().getStringExtra("threadId");
        String phoneNum = getIntent().getStringExtra("phoneNumber");

        setupViews(threadID,phoneNum);
        init(threadID);
    }

    private void setupViews(String threadID, String phoneNum) {
        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle(phoneNum);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
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

}
