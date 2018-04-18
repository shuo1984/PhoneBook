package com.chinatelecom.pimtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.manager.MessageManager;
import com.chinatelecom.pimtest.model.SmsItem;
import com.chinatelecom.pimtest.model.ThreadItem;
import com.chinatelecom.pimtest.utils.DateUtils;
import com.chinatelecom.pimtest.utils.StringUtils;

import java.util.List;

/**
 * Created by Shuo on 2016/10/8.
 */

public class MessageListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<ThreadItem> list;
    private Context ctx; // 上下文
    private MessageManager messageManager;

    public MessageListAdapter(Context context, List<ThreadItem> messageList){
        this.ctx = context;
        this.inflater = LayoutInflater.from(context);
        this.list = messageList;
        if(messageManager==null) {
            this.messageManager = new MessageManager();
        }
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.sms_list_item, null);
            holder = new ViewHolder();
            holder.phoneNum = (TextView) convertView.findViewById(R.id.tv_phone_number);
            holder.date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.messageCount = (TextView) convertView.findViewById(R.id.tv_total_count);
            holder.snippet = (TextView) convertView.findViewById(R.id.tv_snippet);
            holder.newMsgCount = (TextView)convertView.findViewById(R.id.tv_new_msg_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ThreadItem threadItem = list.get(position);
        String phoneNum = threadItem.getAddress();
        String contactName = null;
  /*      if(ContactCacheManager.getNumberContactMap().containsKey(phoneNum)){
            contactName = ContactCacheManager.getNumberContactMap().get(phoneNum).getDesplayName();
        }*/
        String date = DateUtils.format(threadItem.getDate());
        String snippet = threadItem.getSnippet();
        String count = threadItem.getMessageCount();
        int newMsgCount = messageManager.getNewSmsCountByThreadId(threadItem.getThreadId());

        holder.phoneNum.setText(StringUtils.isNotEmpty(contactName)?contactName:phoneNum);
        holder.snippet.setText(snippet);
        holder.date.setText(date);
        if(newMsgCount>0) {
            holder.newMsgCount.setVisibility(View.VISIBLE);
            holder.newMsgCount.setText(String.valueOf(newMsgCount));
        }else{
            holder.newMsgCount.setVisibility(View.GONE);
        }
        if(StringUtils.isNotEmpty(count)) {
            holder.messageCount.setText("(" + String.valueOf(count) + ")");
        }
        return convertView;
    }

    private static class ViewHolder {
         TextView phoneNum;
         TextView date;
         TextView snippet;
         TextView messageCount;
         TextView newMsgCount;

    }
}
