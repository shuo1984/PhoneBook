package com.chinatelecom.pimtest.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.log.Log;

/**
 * Created by Shuo on 2018/1/19.
 */

public class DialerPanel extends LinearLayout{

    private ImageView toggleDown;
    private Context context;
    private Log logger = Log.build(DialerPanel.class);
    private LayoutInflater inflater;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //请求父窗体放行
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

    public DialerPanel(Context context) {
        super(context);
        this.context = context;
        setupViews();
        setListeners();
    }

    public DialerPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setupViews();
        setListeners();
    }

    public DialerPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setupViews();
        setListeners();
    }


    private void setupViews() {
        inflater = LayoutInflater.from(context);
        View panelView = inflater.inflate(R.layout.dial_symbols_layout,this);
        toggleDown = (ImageView)panelView.findViewById(R.id.toggle_dialer_down);
    }

    private void setListeners(){

    }

    public ImageView getToggleDown(){
        return toggleDown;
    }

}
