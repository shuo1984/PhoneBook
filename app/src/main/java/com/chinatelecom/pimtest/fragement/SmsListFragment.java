package com.chinatelecom.pimtest.fragement;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.activity.MessageBoxListActivity;
import com.chinatelecom.pimtest.adapter.MessageListAdapter;
import com.chinatelecom.pimtest.interfaces.NotificationListener;
import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.manager.MessageCacheManager;
import com.chinatelecom.pimtest.manager.MessageManager;
import com.chinatelecom.pimtest.manager.SmsNotificationManager;
import com.chinatelecom.pimtest.model.Notification;
import com.chinatelecom.pimtest.model.SmsItem;
import com.chinatelecom.pimtest.model.ThreadItem;
import com.chinatelecom.pimtest.utils.BaseIntentUtil;
import com.chinatelecom.pimtest.utils.DeviceUtils;
import com.chinatelecom.pimtest.utils.RexseeSMS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SmsListFragment extends Fragment {

    private View mView;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
    private ListView smsList;
    private List<ThreadItem> dataList;
    private List<ThreadItem> notificationMsgList;
    private List<ThreadItem> generalMsgList;
    private MessageListAdapter adapter;
    private RexseeSMS rsms;
    private SmsChangeListener smsChangeListener;
    private MessageManager messageManager;
    private Log logger = Log.build(SmsListFragment.class);

    public SmsListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(!DeviceUtils.isDefaultMessageApp(getActivity())){
            DeviceUtils.setDefaultMessageApp(getActivity());
        }

        mView = inflater.inflate(R.layout.fragment_sms_list, container, false);
        smsList = (ListView)mView.findViewById(R.id.sms_list);
        dataList = new ArrayList<>();
        rsms = new RexseeSMS(getActivity());
        smsChangeListener = new SmsChangeListener();
        messageManager = new MessageManager();
        init();

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerSmsListener();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unregisterSmsListener();
    }

    /**
     * 初始化数据库查询参数
     */
    private void init() {

        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
              /*  dataList = rsms.getThreadsNum(rsms.getThreads(0));
                MessageCacheManager.updateCache(dataList);*/
                Cursor cursor = messageManager.findAllThreadCursor();
                dataList = messageManager.findAllThreads(cursor);
                generalMsgList = new ArrayList<>();
                notificationMsgList = new ArrayList<>();
                logger.debug("#### find Cursor count:" + cursor.getCount());
                classifyMsgs(dataList);
              /*  if(!cursor.isClosed()) {
                    cursor.close();
                }*/
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(dataList!=null && dataList.size()>0){
                    setAdapter(generalMsgList);
                }
            }

            private void classifyMsgs(List<ThreadItem> messageList) {
                for(ThreadItem item : messageList){
                    if(item.getAddress().startsWith("106") ||
                            item.getAddress().startsWith("95") ||
                            item.getAddress().startsWith("96") ||
                            item.getAddress().startsWith("10001") ||
                            (item.getAddress().startsWith("1") && item.getAddress().length()==5)){
                        notificationMsgList.add(item);
                    }else{
                        generalMsgList.add(item);
                    }
                }
            }
        }.execute();


    }

    private void setAdapter(final List<ThreadItem> list) {
        adapter = new MessageListAdapter(getActivity(), list);
        smsList.setAdapter(adapter);
        smsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = new HashMap<String, String>();
                ThreadItem item = (ThreadItem) adapter.getItem(position);
                map.put("phoneNumber", item.getAddress());
                map.put("threadId", String.valueOf(item.getThreadId()));
                BaseIntentUtil.intentSysDefault(getActivity(),MessageBoxListActivity.class, map);
            }
        });

        smsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showListDialog(newtan, list.get(position));
                return true;
            }
        });
    }

    private String[] newtan = new String[] { "删除", "查询信息详情" };

    private void showListDialog(final String[] arg, final ThreadItem threadItem) {
        new AlertDialog.Builder(getActivity()).setTitle("信息选项")
                .setItems(arg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                long threadId = threadItem.getThreadId();
                                List<Long> deleteThreads = new ArrayList<>();
                                deleteThreads.add(threadId);
                                messageManager.deleteMessageByThreadIds(deleteThreads);
                                break;
                            case 1:
                                break;
                        }
                        ;
                    }
                }).show();
    }

    private class SmsChangeListener implements NotificationListener{
        @Override
        public void onChange(Notification notification) {
            logger.debug("SmsList listen sms changed!");
            try {
                init();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void registerSmsListener(){
        SmsNotificationManager.getInstance().registerListener(Notification.Event.MESSAGE_CHANGED, smsChangeListener);
    }

    private void unregisterSmsListener(){
        SmsNotificationManager.getInstance().unregisterListener(Notification.Event.MESSAGE_CHANGED,smsChangeListener);
    }

}
