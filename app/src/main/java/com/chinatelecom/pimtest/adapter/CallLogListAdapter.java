package com.chinatelecom.pimtest.adapter;

import android.content.Context;
import android.provider.CallLog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.model.CallLogItem;
import com.chinatelecom.pimtest.utils.GeoUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Shuo on 2018/1/9.
 */

public class CallLogListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<CallLogItem> list;
    private Context ctx; // 上下文

    private PhoneNumberOfflineGeocoder geocoder;
    private PhoneNumberUtil phoneNumberUtil;
    private final static int COUNTRY_CODE = 86;

    public CallLogListAdapter(Context context, List<CallLogItem> callLogs){
        this.ctx = context;
        this.list = callLogs;
        this.inflater = LayoutInflater.from(context);
       // this.hcodeManager = new DefaultHcodeManager(IApplication.getSqliteTemplate());
        geocoder = PhoneNumberOfflineGeocoder.getInstance();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.call_log_list_item, null);
                holder = new ViewHolder();

                holder.callTypeIcon = (ImageView) convertView.findViewById(R.id.call_type_icon);
                holder.location = (TextView) convertView.findViewById(R.id.location);
                holder.number = (TextView) convertView.findViewById(R.id.number);
                holder.callTime = (TextView) convertView.findViewById(R.id.callTime);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CallLogItem callLogItem = list.get(position);

            String name = callLogItem.getName();
            String number = callLogItem.getNumber();
            holder.number.setText(name==null?number:name);
            String carrier = GeoUtil.getCarrier(this.ctx ,number);
            String carrierZh = "";
            switch (carrier){
                case "China Mobile":
                    carrierZh += "移动";
                    break;
                case "China Unicom":
                    carrierZh += "联通";
                    break;
                case "China Telecom":
                    carrierZh += "电信";
                    break;
                default:
                    break;
            }


            Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
            pn.setCountryCode(86);
            pn.setNationalNumber(Long.parseLong(number));
            String location = geocoder.getDescriptionForNumber(pn, Locale.CHINESE);
            holder.location.setText(location + carrierZh);

            switch (callLogItem.getCallType()){
                case CallLog.Calls.INCOMING_TYPE:
                    holder.callTypeIcon.setImageResource(R.mipmap.incoming_call);
                    break;
                case  CallLog.Calls.OUTGOING_TYPE:
                    holder.callTypeIcon.setImageResource(R.mipmap.outgoing_call);
                    break;
                case  CallLog.Calls.MISSED_TYPE:
                    holder.callTypeIcon.setImageResource(R.mipmap.missed_call);
                    break;
            }

            holder.callTime.setText(callLogItem.getCallDateStr());
        }catch (Exception e){
            e.printStackTrace();
        }


        return convertView;
    }


    class ViewHolder{
            ImageView callTypeIcon;
            TextView number;
            TextView location;
            TextView callTime;
    }
}
