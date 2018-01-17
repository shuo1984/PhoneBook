package com.chinatelecom.pimtest.utils;

import android.app.Service;
import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangmingfan
 * @since 15/12/2014
 */
public class PhoneUtils {

    private static final int MOBILE_LEN = 11;

    public static final char[] DIGITS = "0123456789".toCharArray();

    public static int getPhoneType(Context context) {
        int simType = 0;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            simType = 0;
        } else {
            simType = 1;
        }
        return simType;
    }

   //此处replace的不是一般的空格哦，表问我，我也不知道它是神马哦，不可删除修改哦~ ^o^
   public static String getNumber(String phone) {
     String address =  DeviceUtils.phoneReplaceAll(phone);
       if (StringUtils.startsWith(address, "+86")) {
           address = StringUtils.substring(address, 3).trim().replaceAll(" ", "");
           address = NumberUtils.filterNumber(address);
       }
       return address;
   }


    public static boolean isNotificationNumber(String number) {
        //呼叫中心号码
        if (StringUtils.startsWith(number, "95") || StringUtils.startsWith(number, "96")) {
            return true;
        }
        //SP短信服务号
        if (StringUtils.startsWith(number, "106")) {
            return true;
        }
        //运营商专用号码
        if (StringUtils.startsWith(number, "1") && StringUtils.length(number) == 5) {
            return true;
        }

        if (StringUtils.startsWith(number, "10001")) {
            return true;
        }
        return false;
    }

    public static boolean checkPhoneNumber(Context context, String phoneNumber) {
        boolean flag = false;
        if (StringUtils.isBlank(phoneNumber)) {
            // toastTool.showMessage(context.getResources().getString(R.string.message_none_phone));
            String regPattern = "^1\\d{10}$|^(0\\d{2,3}-?|\\(0\\d{2,3}\\))?[1-9]\\d{4,7}(-\\d{1,8})?$";
            Pattern regex = Pattern.compile(regPattern);
            Matcher matcher = regex.matcher(phoneNumber);
            flag = matcher.matches();
            return false;
        }
        return true;
    }

    public static boolean checkPhoneCanMessage(String phone) {
        String str = phone;
        if (StringUtils.isNotEmpty(str)) {
            //各种类型的空格，替换
            str = DeviceUtils.phoneReplaceAll(str);
        } else {
            //toastTool.showMessage("无效收件人");
            return false;
        }
        if (StringUtils.startsWith(str, "+86")) {
            str = StringUtils.substring(str, 3);
        }
        if (!NumberUtils.isDigits(DeviceUtils.phoneReplaceAll(str))) {
            //toastTool.showMessage("无效收件人");
            return false;
        }
        return true;
    }

}
