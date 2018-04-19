package com.chinatelecom.pimtest.activity;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.adapter.MessageListAdapter;
import com.chinatelecom.pimtest.manager.MessageCacheManager;
import com.chinatelecom.pimtest.model.ThreadItem;
import com.chinatelecom.pimtest.utils.BaseIntentUtil;
import com.chinatelecom.pimtest.view.HeaderView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationMessageActivity extends AppCompatActivity {

    private ListView smsList;
    private MessageListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        setupViews();
        bindData();
    }


    private void setupViews() {
        HeaderView headerView = (HeaderView)findViewById(R.id.header_view);
        headerView.setMiddleView("通知类短信");
        headerView.getLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        smsList = (ListView)findViewById(R.id.sms_list);

    }

    private void bindData() {
        List<ThreadItem> datalist = MessageCacheManager.getNotificationMsgsCache();
        adapter = new MessageListAdapter(NotificationMessageActivity.this, datalist);
        smsList.setAdapter(adapter);

        smsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = new HashMap<String, String>();
                ThreadItem item = (ThreadItem) adapter.getItem(position);
                map.put("phoneNumber", item.getAddress());
                map.put("threadId", String.valueOf(item.getThreadId()));
                BaseIntentUtil.intentSysDefault(NotificationMessageActivity.this, MessageBoxListActivity.class, map);
            }
        });

        smsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
    }


}
