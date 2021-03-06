package com.chinatelecom.pimtest.fragement;


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.adapter.CallLogListAdapter;
import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.model.CallLogItem;
import com.chinatelecom.pimtest.utils.DateUtils;
import com.chinatelecom.pimtest.view.DialerPanel;

import java.util.ArrayList;
import java.util.List;

import cn.richinfo.dualsim.TelephonyManagement;


/**
 * A simple {@link Fragment} subclass.
 */
public class CallLogListFragment extends Fragment {

    private View mView;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
    private ListView callLogList;
    private RelativeLayout noCallLogLayout,singleSimLayout,dualSimLayout;
    private CallLogListAdapter adapter;
    private Log logger = Log.build(CallLogListFragment.class);
    private DialerPanel dialerPanel;
    private ImageButton dialerToggleUp;
    private TelephonyManagement.TelephonyInfo telephonyInfo;
    private boolean isDualSim = false;
    private Button sim1Call, sim2Call;

    public CallLogListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_call_log_list, container, false);
        asyncQueryHandler = new CallLogAsyncQueryHandler(getActivity().getContentResolver());
        callLogList = (ListView) mView.findViewById(R.id.call_log_list);
        noCallLogLayout = (RelativeLayout)mView.findViewById(R.id.no_callLog_layout);
        dialerPanel = (DialerPanel)mView.findViewById(R.id.dialer_panel);
        dialerToggleUp = (ImageButton)mView.findViewById(R.id.dialer_toggle_up);
        singleSimLayout = (RelativeLayout)mView.findViewById(R.id.single_sim_call_layout);
        dualSimLayout = (RelativeLayout)mView.findViewById(R.id.dual_sim_call_layout);

        telephonyInfo = TelephonyManagement.getInstance().updateTelephonyInfo(getActivity()).getTelephonyInfo(getActivity());

        initData();
        setupDualSimInfo();
        setupListeners();
        return mView;
    }


    private void setupListeners() {
        dialerToggleUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialerPanel.setVisibility(View.VISIBLE);
                dialerToggleUp.setVisibility(View.GONE);
            }
        });

        dialerPanel.getToggleDown().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialerPanel.setVisibility(View.GONE);
                dialerToggleUp.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initData() {
        Uri uri =CallLog.Calls.CONTENT_URI;
        String[] projection =   new String[]{
                CallLog.Calls.CACHED_NAME,  //姓名
                CallLog.Calls.NUMBER,    //号码
                CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                CallLog.Calls.DATE,  //拨打时间
                CallLog.Calls.DURATION,   //通话时长
                CallLog.Calls.PHONE_ACCOUNT_ID
        };

        asyncQueryHandler.startQuery(0,null,uri,projection,null,
                null,CallLog.Calls.DEFAULT_SORT_ORDER);
    }


    private class CallLogAsyncQueryHandler extends AsyncQueryHandler{

        public CallLogAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            super.onQueryComplete(token, cookie, cursor);
            try {
                List<CallLogItem> callLogGroups = new ArrayList<>();
                if (cursor != null && cursor.getCount() > 0) {
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        String callName = cursor.getString(0);
                        String callNumber = cursor.getString(1);
                        //通话类型
                        int callType = Integer.parseInt(cursor.getString(2));
                    /*String callTypeStr="";
                    switch (callType) {
                        case CallLog.Calls.INCOMING_TYPE:
                            callTypeStr="呼入";
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            callTypeStr="呼出";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            callTypeStr="未接";
                            break;
                    }*/
                        //拨打时间
                        long callDate = Long.parseLong(cursor.getString(3));
                        String callDateStr = DateUtils.format(callDate);
                        //通话时长
                        int callDuration = Integer.parseInt(cursor.getString(4));
                        int min = callDuration / 60;
                        int sec = callDuration % 60;

                        //通话卡
                        int subId = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.PHONE_ACCOUNT_ID));



                        CallLogItem callLogItem = new CallLogItem();
                        callLogItem.setName(callName);
                        callLogItem.setNumber(callNumber);
                        callLogItem.setCallDate(callDate);
                        callLogItem.setCallDateStr(callDateStr);
                        callLogItem.setCallDuration(callDuration);
                        callLogItem.setCallType(callType);
                        if(telephonyInfo.getSubIdSIM1() == subId) {
                            callLogItem.setDualSimCardStr("1");
                        }else{
                            callLogItem.setDualSimCardStr("2");
                        }
                        //callLogs.add(callLogItem);
                        boolean isSubItem = false;
                        for (CallLogItem item : callLogGroups) {
                            if (item.getNumber().equals(callNumber)) {
                                item.addSubCallLog(callLogItem);
                                isSubItem = true;
                            }
                        }

                        if (!isSubItem) {
                            callLogGroups.add(callLogItem);
                        }
                    }

                } else {
                    noCallLogLayout.setVisibility(View.VISIBLE);
                }

                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
                setAdapter(callLogGroups);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void setAdapter(List<CallLogItem> callLogs) {
        adapter = new CallLogListAdapter(getActivity(),callLogs);
        callLogList.setAdapter(adapter);
        noCallLogLayout.setVisibility(View.GONE);
    }

    private void setupDualSimInfo() {
        isDualSim = telephonyInfo.isDualSIM();
        singleSimLayout.setVisibility(isDualSim ? View.GONE : View.VISIBLE);
        dualSimLayout.setVisibility(isDualSim ? View.VISIBLE : View.GONE);
    }

}
