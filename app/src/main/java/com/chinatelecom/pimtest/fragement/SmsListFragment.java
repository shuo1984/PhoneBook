package com.chinatelecom.pimtest.fragement;

import android.content.AsyncQueryHandler;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.activity.MessageBoxListActivity;
import com.chinatelecom.pimtest.adapter.MessageListAdapter;
import com.chinatelecom.pimtest.manager.MessageCacheManager;
import com.chinatelecom.pimtest.model.SmsItem;
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
    private List<SmsItem> dataList;
    private MessageListAdapter adapter;
    private RexseeSMS rsms;

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
        init();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * 初始化数据库查询参数
     */
    private void init() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                dataList = rsms.getThreadsNum(rsms.getThreads(0));
                MessageCacheManager.updateCache(dataList);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(dataList.size()>0){
                    setAdapter(dataList);
                }
            }
        }.execute();


    }

    private void setAdapter(List<SmsItem> list) {
        adapter = new MessageListAdapter(getActivity(), list);
        smsList.setAdapter(adapter);
        smsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, String> map = new HashMap<String, String>();
                SmsItem sb = (SmsItem) adapter.getItem(position);
                map.put("phoneNumber", sb.getAddress());
                map.put("threadId", sb.getThreadId());
                BaseIntentUtil.intentSysDefault(getActivity(),MessageBoxListActivity.class, map);
            }
        });
    }

}
