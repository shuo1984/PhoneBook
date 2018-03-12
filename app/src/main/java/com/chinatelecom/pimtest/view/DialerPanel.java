package com.chinatelecom.pimtest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.utils.BaseIntentUtil;
import com.chinatelecom.pimtest.utils.StringUtils;
import com.chinatelecom.pimtest.utils.TelephonyUtil;

import java.util.ArrayList;
import java.util.List;

import cn.richinfo.dualsim.TelephonyManagement;

/**
 * Created by Shuo on 2018/1/19.
 */

public class DialerPanel extends LinearLayout implements View.OnClickListener{

    private ImageView toggleDown;
    private Context context;
    private Log logger = Log.build(DialerPanel.class);
    private LayoutInflater inflater;
    private EditText dialerOutput;
    private List<ImageView> dialerButtons;
    private ImageView delBtn;
    private ImageView callBtn;
    private static StringBuilder dialnum = new StringBuilder();
    private Button sim1Call, sim2Call;
    private TelephonyManagement.TelephonyInfo telephonyInfo;
    public static final String[] dualSimTypes = { "subscription", "Subscription",
            "com.android.phone.extra.slot",
            "phone", "com.android.phone.DialingMode",
            "simId", "simnum", "phone_type",
            "simSlot" };
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //请求父窗体放行
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onInterceptTouchEvent(ev);
    }

    public DialerPanel(Context context) {
        super(context);
        this.context = context;
        telephonyInfo = TelephonyManagement.getInstance().updateTelephonyInfo(context).getTelephonyInfo(context);
        setupViews();
        setupListeners();
    }



    public DialerPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        telephonyInfo = TelephonyManagement.getInstance().updateTelephonyInfo(context).getTelephonyInfo(context);
        setupViews();
        setupListeners();
    }

    public DialerPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        telephonyInfo = TelephonyManagement.getInstance().updateTelephonyInfo(context).getTelephonyInfo(context);
        setupViews();
        setupListeners();
    }


    private void setupViews() {
        inflater = LayoutInflater.from(context);
        View panelView = inflater.inflate(R.layout.dial_symbols_layout,this);
        toggleDown = (ImageView)panelView.findViewById(R.id.toggle_dialer_down);
        dialerOutput = (EditText)panelView.findViewById(R.id.dial_output);
        dialerButtons = new ArrayList<>();
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_0));
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_1));
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_2));
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_3));
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_4));
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_5));
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_6));
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_7));
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_8));
        dialerButtons.add((ImageView)panelView.findViewById(R.id.dial_number_9));

        delBtn = (ImageView)panelView.findViewById(R.id.dial_del_btn);
        callBtn = (ImageView)panelView.findViewById(R.id.dialer_button);
        sim1Call = (Button) panelView.findViewById(R.id.call_sim1);
        sim2Call = (Button) panelView.findViewById(R.id.call_sim2);
        sim1Call.setText(TelephonyUtil.strSimOperatorInfo(telephonyInfo.getOperatorSIM1()));
        sim2Call.setText(TelephonyUtil.strSimOperatorInfo(telephonyInfo.getOperatorSIM2()));
    }



    public ImageView getToggleDown(){
        return toggleDown;
    }

    private void setupListeners() {
      //  toggleDown.setOnClickListener(this);
        for(ImageView dialerBtn : dialerButtons){
            dialerBtn.setOnClickListener(this);
        }
        delBtn.setOnClickListener(this);
        callBtn.setOnClickListener(this);
        sim1Call.setOnClickListener(this);
        sim2Call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dial_number_0:
                dialnum.append("0") ;
                dialerOutput.setText(dialnum.toString());
                dialerOutput.requestFocus();
                hideSoftInputFromWindow();
                break;
            case R.id.dial_number_1:
                dialnum.append("1");
                dialerOutput.setText(dialnum.toString());
                dialerOutput.requestFocus();
                hideSoftInputFromWindow();
                break;
            case R.id.dial_number_2:
                dialnum.append("2");
                dialerOutput.setText(dialnum.toString());
                dialerOutput.requestFocus();
                hideSoftInputFromWindow();
                break;
            case R.id.dial_number_3:
                dialnum.append("3");
                dialerOutput.setText(dialnum.toString());
                dialerOutput.requestFocus();
                hideSoftInputFromWindow();
                break;
            case R.id.dial_number_4:
                dialnum.append("4");
                dialerOutput.setText(dialnum.toString());
                dialerOutput.requestFocus();
                hideSoftInputFromWindow();
                break;
            case R.id.dial_number_5:
                dialnum.append("5");
                dialerOutput.setText(dialnum.toString());
                dialerOutput.requestFocus();
                hideSoftInputFromWindow();
                break;
            case R.id.dial_number_6:
                dialnum.append("6");
                dialerOutput.setText(dialnum.toString());
                hideSoftInputFromWindow();
                break;
            case R.id.dial_number_7:
                dialnum.append("7");
                dialerOutput.setText(dialnum.toString());
                dialerOutput.requestFocus();
                hideSoftInputFromWindow();
                break;
            case R.id.dial_number_8:
                dialnum.append("8");
                dialerOutput.setText(dialnum.toString());
                dialerOutput.requestFocus();
                hideSoftInputFromWindow();
                break;
            case R.id.dial_number_9:
                dialnum.append("9");
                dialerOutput.setText(dialnum.toString());
                dialerOutput.requestFocus();
                hideSoftInputFromWindow();
                break;
            case R.id.toggle_dialer_down:
                DialerPanel.this.setVisibility(GONE);
                break;
            case R.id.dial_del_btn:
                if(dialnum.length()>0) {
                    dialnum.deleteCharAt(dialnum.length() - 1);
                    dialerOutput.setText(dialnum.toString());
                    dialerOutput.requestFocus();
                    hideSoftInputFromWindow();
                }
                break;
            case R.id.dialer_button:
                doCall();
                break;
            case R.id.call_sim1:
                doDualSimCall(0);
                break;
            case R.id.call_sim2:
                doDualSimCall(1);
                break;
        }
    }

    private void doCall() {
        BaseIntentUtil.createCallIntent(context, dialnum.toString());
    }

    private void doDualSimCall(int index){
        logger.debug("#### telephonyInfo.getSlotIdSIM1()==" + telephonyInfo.getSlotIdSIM1());
        logger.debug("#### telephonyInfo.getSlotIdSIM2()==" + telephonyInfo.getSlotIdSIM2());
        logger.debug("#### telephonyInfo.getSubIdSIM1()==" + telephonyInfo.getSubIdSIM1());
        logger.debug("#### telephonyInfo.getSubIdSIM2()==" + telephonyInfo.getSubIdSIM2());

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            call(context,index,dialnum.toString());
        }else {
            if(index == 0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" + dialnum.toString()));
                for (int i = 0; i < dualSimTypes.length; i++) {
                    callIntent.putExtra(dualSimTypes[i],telephonyInfo.getSubIdSIM1() );
                }
                context.startActivity(callIntent);
            }else{
                Intent callIntent = new Intent(Intent.ACTION_CALL)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" + dialnum.toString()));
                for (int i = 0; i < dualSimTypes.length; i++) {
                    callIntent.putExtra(dualSimTypes[i],telephonyInfo.getSubIdSIM2() );
                }
                context.startActivity(callIntent);
            }
        }

    }

    private void hideSoftInputFromWindow() {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(dialerOutput.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    public static void call(Context context, int id, String telNum){
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        if(telecomManager != null){
            List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telNum));
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandleList.get(id));
            context.startActivity(intent);
        }
    }

}
