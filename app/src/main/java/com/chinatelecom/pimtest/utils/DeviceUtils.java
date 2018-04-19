package com.chinatelecom.pimtest.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.chinatelecom.pimtest.log.Log;

/**
 * Created by Shuo on 2017/12/29.
 */

public class DeviceUtils {

    private static Log logger = Log.build(DeviceUtils.class);


    public static float getDisplayDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getWindowHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getWindowWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }


    /*
    检测是否是默认的短信息应用SDK 4.4
     */
    public static boolean isDefaultMessageApp(Context context) {
        if (Build.VERSION.SDK_INT < 19) {
            return true;
        }

        String myPackageName = context.getPackageName();
        String defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(context);
        logger.debug("isDefaultMessageApp myPackageName: %s ", "" + myPackageName);
        logger.debug("isDefaultMessageApp defaultSmsApp: %s ", "" + defaultSmsApp);

        if (StringUtils.isBlank(defaultSmsApp)) {
            return true;
        }
        if (StringUtils.equals(myPackageName, defaultSmsApp)) {
            return true;
        }
        return false;
    }

    public static void setDefaultMessageApp(Context context) {

        try {
            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context,"该手机不支持设置默认短信应用",Toast.LENGTH_SHORT).show();
        }
    }

    //识别手机号码运营商
    public static String getProvidersName(String mobile, boolean des) {
        String returnString = "";
        if (mobile == null || mobile.trim().length() != 11) {
            return "";
        }
        if (mobile.trim().substring(0, 3).equals("134") || mobile.trim().substring(0, 3).equals("135") ||
                mobile.trim().substring(0, 3).equals("136") || mobile.trim().substring(0, 3).equals("137")
                || mobile.trim().substring(0, 3).equals("138") || mobile.trim().substring(0, 3).equals("139")
                || mobile.trim().substring(0, 3).equals("150") || mobile.trim().substring(0, 3).equals("151")
                || mobile.trim().substring(0, 3).equals("152")
                || mobile.trim().substring(0, 3).equals("157") || mobile.trim().substring(0, 3).equals("158")
                || mobile.trim().substring(0, 3).equals("159")
                || mobile.trim().substring(0, 3).equals("187") || mobile.trim().substring(0, 3).equals("188")) {
            returnString = (des ? "中国" : "") + "移动";
        }
        if (mobile.trim().substring(0, 3).equals("130") || mobile.trim().substring(0, 3).equals("131") ||
                mobile.trim().substring(0, 3).equals("132") || mobile.trim().substring(0, 3).equals("156")
                || mobile.trim().substring(0, 3).equals("185") || mobile.trim().substring(0, 3).equals("186")
                || mobile.trim().substring(0, 3).equals("155")) {
            returnString = (des ? "中国" : "") + "联通";
        }
        if (mobile.trim().substring(0, 3).equals("133") || mobile.trim().substring(0, 3).equals("153") ||
                mobile.trim().substring(0, 3).equals("180") || mobile.trim().substring(0, 3).equals("189") ||
                mobile.trim().substring(0, 3).equals("177")|| mobile.trim().substring(0, 3).equals("199") ||
                mobile.trim().substring(0, 3).equals("173")||mobile.trim().substring(0,3).equals("181")){
            returnString = (des ? "中国" : "") + "电信";
        }
        if (returnString.trim().equals("")) {
            returnString = "";   //未知运营商
        }
        return returnString;
    }

    public static String phoneReplaceAll(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        str = str.trim().replaceAll(" ", "");
        str = str.replaceAll(" ","");
        str = str.replaceAll(" ","");
        str = str.replaceAll(" ","");
        str = str.replaceAll("  ", "");
        str = StringUtils.replaceChars(str, "-", "");
        str = StringUtils.replaceChars(str, "(", "");
        str = StringUtils.replaceChars(str, ")", "");
        return str;
    }

    //收缩软键盘
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void hideKeyBoard(Context context, EditText editText) {
        if (editText == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static void showKeyBoard(Context context, EditText editText) {
        if (editText == null) {
            return;
        }
        InputMethodManager in = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static String getVersionName(Context context) throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    public static boolean isUIMABSENT(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); //获得手机SIMType
        int state = telephonyManager.getSimState();
        return state == TelephonyManager.SIM_STATE_ABSENT;
    }

}
