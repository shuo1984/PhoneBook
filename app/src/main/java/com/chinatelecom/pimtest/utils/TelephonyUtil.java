package com.chinatelecom.pimtest.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.chinatelecom.pimtest.log.Log;

import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Pan on 2017/3/2.
 */

public class TelephonyUtil {

    private Log logger = Log.build(TelephonyUtil.class);

    public static void printTelephonyManagerMethodNamesForThisDevice(Context context) {
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<?> telephonyClass;
        try {
            telephonyClass = Class.forName(telephony.getClass().getName());
            Method[] methods = telephonyClass.getMethods();
            for (int idx = 0; idx < methods.length; idx++) {
                System.out.println("\n" + methods[idx] + " declared by " + methods[idx].getDeclaringClass());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取网络运营商 1.移动流量 2.联通流量网络 3.电信流量网络
     *
     * @param context
     * @return
     */
    public static int getSimOperator(Context context, String operator) {
        int mode = Settings.System.getInt(context.getApplicationContext().getContentResolver(),
                "airplane_mode_on", 0);
        if (mode == 1) {
            return 0;
        }
        try {
            if (TextUtils.isEmpty(operator)) {
                TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                operator = manager.getSimOperator();
            }
            if (!TextUtils.isEmpty(operator)) {
                if (operator.equals("46000") || operator.equals("46002")
                        || operator.equals("46007") || operator.equals("46004")) {
                    // 中国移动
                    return 1;
                } else if ("46001".equals(operator) || operator.equals("46006")
                        || operator.equals("46009")) {
                    // 中国联通
                    return 2;
                } else if ("46003".equals(operator) || operator.equals("46005")
                        || operator.equals("46011")) {
                    // 中国电信
                    return 3;
                }
            }
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 获取短信上行端口号
     *
     * @param context
     * @return
     */
    public static String getSendNumber(Context context, String operator) {
        int operetor = getSimOperator(context, operator);
        if (1 == operetor) {
            return "106581021";
        } else if (2 == operetor) {
            return "1065553610039";
        } else if (3 == operetor) {
            return "1065987711";
        }
        return "";
    }

    public static String getSmsContent(Context context, String imsi, String operator) {
        String content = imsi;
        if (getSimOperator(context, operator) == 3) {
            content = "op#cx" + imsi;
        }
        return content;
    }

    public static String strSimOperatorInfo(String op){
        if("46000".equals(op) || "46002".equals(op) || "46007".equals(op)|| "46004".equals(op)) {
            return "移动运营商";
        } else if ("46001".equals(op)|| "46006".equals(op) || "46009".equals(op)) {
            return "联通运营商";
        } else if ("46003".equals(op)||"46005".equals(op)||"46011".equals(op)) {
            return "电信运营商";
        } else {
            return "未知运营商";
        }
    }

    /**
     这一块首先获取手机中所有sim卡 PhoneAccountHandle 每一个 PhoneAccountHandle 表示一个sim卡, 然后根据 slotId 判断所指定的sim卡并返回此 PhoneAccountHandle (这里5.1 和 6.0需要区分对待)
     *//*

    private PhoneAccountHandle getPhoneAccountHandle(Context context, int slotId) {
        TelecomManager tm = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        //PhoneAccountHandle api>5.1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<PhoneAccountHandle> handles = (List<PhoneAccountHandle>) ReflectUtil.invokeMethod(tm, "getCallCapablePhoneAccounts");
            SubscriptionManager sm = SubscriptionManager.from(context);
            if (handles != null) {
                for (PhoneAccountHandle handle : handles) {
                    SubscriptionInfo info = sm.getActiveSubscriptionInfoForSimSlotIndex(slotId);
                    if (info != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (TextUtils.equals(info.getIccId(), handle.getId())) {
                                logger.debug( "getPhoneAccountHandle for slot" + slotId + " " + handle);
                                return handle;
                            }
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            if (TextUtils.equals(info.getSubscriptionId() + "", handle.getId())) {
                                logger.debug("getPhoneAccountHandle for slot" + slotId + " " + handle);
                                return handle;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }*/

}
