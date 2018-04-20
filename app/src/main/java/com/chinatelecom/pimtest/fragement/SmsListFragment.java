package com.chinatelecom.pimtest.fragement;

import android.app.AlertDialog;
import android.content.AsyncQueryHandler;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.activity.MessageBoxListActivity;
import com.chinatelecom.pimtest.activity.NotificationMessageActivity;
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
import com.chinatelecom.pimtest.utils.StringUtils;

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
    private View notificationMsgLabelView;
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
        init();
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

                //if(notificationMsgList.size()>0){
                    addNotificationMsgGroup();
                //}
            }

            private void classifyMsgs(List<ThreadItem> messageList) {
                for(ThreadItem item : messageList){
                    if(item.getAddress().size()==1){

                        if(item.getAddress().get(0).startsWith("106") ||
                                item.getAddress().get(0).startsWith("95") ||
                                item.getAddress().get(0).startsWith("96") ||
                                item.getAddress().get(0).startsWith("10001") ||
                            (item.getAddress().get(0).startsWith("1") && item.getAddress().get(0).length()==5)) {
                            notificationMsgList.add(item);
                        }else{
                            generalMsgList.add(item);
                        }
                    }else{
                        generalMsgList.add(item);
                    }
                }

                MessageCacheManager.updateNotificationMessages(notificationMsgList);
            }
        }.execute();


    }

    private void addNotificationMsgGroup() {
        if(notificationMsgLabelView!=null) {
            smsList.removeHeaderView(notificationMsgLabelView);
        }

        notificationMsgLabelView = LayoutInflater.from(getActivity()).inflate(R.layout.sms_list_item, null, false);
        TextView titleTxt = (TextView) notificationMsgLabelView.findViewById(R.id.tv_phone_number);
        TextView msgTxt = (TextView) notificationMsgLabelView.findViewById(R.id.tv_snippet);
        TextView newMsgCountTxt = (TextView)notificationMsgLabelView.findViewById(R.id.tv_new_msg_count);
        TextView dateTxt = (TextView)notificationMsgLabelView.findViewById(R.id.tv_date);
        TextView totalMsgCount = (TextView)notificationMsgLabelView.findViewById(R.id.tv_total_count);

        titleTxt.setText("通知类短信");
        if(notificationMsgList.size()>0) {
            msgTxt.setText(notificationMsgList.get(0).getSnippet());
        }
        int unReadMsgCount = messageManager.ComputeUnreadNotificationMsgs(notificationMsgList);
        newMsgCountTxt.setText(unReadMsgCount>9 ? "9+":String.valueOf(unReadMsgCount));
        newMsgCountTxt.setVisibility( unReadMsgCount>0 ? View.VISIBLE : View.INVISIBLE);
        dateTxt.setVisibility(View.GONE);
        totalMsgCount.setVisibility(View.GONE);
        smsList.addHeaderView(notificationMsgLabelView);



    }



    private void setAdapter(final List<ThreadItem> list) {
        adapter = new MessageListAdapter(getActivity(), list);
        smsList.setAdapter(adapter);
        smsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = new HashMap<String, String>();
                if(notificationMsgList.size()==0 && position==0) {
                    Toast.makeText(getActivity(),"无通知类短信",Toast.LENGTH_SHORT).show();
                }else if(notificationMsgList.size()>0 && position==0){
                    Toast.makeText(getActivity(),"跳转通知类短信",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), NotificationMessageActivity.class);
                    startActivity(intent);
                }else {
                    ThreadItem item = (ThreadItem) adapter.getItem(position - 1);
                    String address = StringUtils.join(item.getAddress(),",");
                    map.put("phoneNumber", address);
                    map.put("threadId", String.valueOf(item.getThreadId()));
                    BaseIntentUtil.intentSysDefault(getActivity(), MessageBoxListActivity.class, map);
                }

            }
        });

        smsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showListDialog(newtan, list.get(position-1));
                return true;
            }
        });
    }

    private String[] newtan = new String[] { "删除", "转发" };

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
