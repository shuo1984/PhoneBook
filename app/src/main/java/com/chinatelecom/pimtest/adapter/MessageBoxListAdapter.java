package com.chinatelecom.pimtest.adapter;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.model.Message;

import java.util.List;

/**
 * Created by Shuo on 2016/10/10.
 */

public class MessageBoxListAdapter extends BaseAdapter{
    private Context ctx;
    private List<Message> mbList;
    private LinearLayout layout_father;
    private LayoutInflater vi;
    private LinearLayout layout_child;
    private TextView tvDate;
    private TextView tvText;

    public MessageBoxListAdapter(Context context, List<Message> coll) {
        ctx = context;
        vi = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mbList = coll;
    }

    @Override
    public int getCount() {
        return mbList.size();
    }

    @Override
    public Object getItem(int position) {
        return mbList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message mb = mbList.get(position);
        int itemLayout = mb.getLayoutID();
        layout_father = new LinearLayout(ctx);
        vi.inflate(itemLayout, layout_father, true);

        layout_father.setBackgroundColor(Color.TRANSPARENT);
        layout_child = (LinearLayout) layout_father
                .findViewById(R.id.layout_bj);

        tvText = (TextView) layout_father
                .findViewById(R.id.messagedetail_row_text);
        tvText.setText(mb.getText());

        tvDate = (TextView) layout_father
                .findViewById(R.id.messagedetail_row_date);
        tvDate.setText(mb.getDate());

        addListener(tvText, tvDate, layout_child, mb);

        return layout_father;

    }

    public void addListener(final TextView tvText, final TextView tvDate,
                            LinearLayout layout_bj, final Message mb) {

        layout_bj.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        layout_bj.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                tvText.setTextColor(0xffffffff);
                showListDialog(newtan, mb);
                return true;
            }
        });

        layout_bj.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:

                    case MotionEvent.ACTION_MOVE:
                        tvText.setTextColor(0xffffffff);
                        break;

                    default:
                        tvText.setTextColor(Color.BLACK);
                        break;
                }
                return false;
            }
        });
    }

    private String[] newtan = new String[] { "转发", "复制文本内容", "删除", "查询信息详情" };

    private void showListDialog(final String[] arg, final Message mb) {
        new AlertDialog.Builder(ctx).setTitle("信息选项")
                .setItems(arg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {

                            case 0:
                                break;

                            case 1:
                                ClipboardManager cmb = (ClipboardManager) ctx
                                        .getSystemService(ctx.CLIPBOARD_SERVICE);
                                cmb.setText(mb.getText());
                                break;
                            case 2:

                                break;
                            case 3:
                                break;
                        }
                        ;
                    }
                }).show();
    }
}
