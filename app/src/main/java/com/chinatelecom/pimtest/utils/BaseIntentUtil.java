package com.chinatelecom.pimtest.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BaseIntentUtil {
	public static int DEFAULT_ENTER_ANIM;
	public static int DEFAULT_EXIT_ANIM;

	public static Intent intent;

	public static void intentDIY(Activity activity, Class<?> classes) {
		IntentDIY(activity, classes, null, DEFAULT_ENTER_ANIM,
				DEFAULT_EXIT_ANIM);
	}

	public static void IntentDIY(Activity activity, Class<?> classes,
								 Map<String, String> paramMap, int enterAnim, int exitAnim) {
		intent = new Intent(activity, classes);
		organizeAndStart(activity, classes, paramMap);
		if (enterAnim != 0 && exitAnim != 0) {
			activity.overridePendingTransition(enterAnim, exitAnim);
		}
	}

	public static void intentSysDefault(Activity activity, Class<?> classes,
										Map<String, String> paramMap) {
		organizeAndStart(activity, classes, paramMap);
	}

	private static void organizeAndStart(Activity activity, Class<?> classes,
										 Map<String, String> paramMap) {
		intent = new Intent(activity, classes);
		if (null != paramMap) {
			Set<String> set = paramMap.keySet();
			for (Iterator<String> iterator = set.iterator(); iterator.hasNext(); ) {
				String key = iterator.next();
				intent.putExtra(key, paramMap.get(key));
			}
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
	}

	public static void createCallIntent(Context context, String number) {

		if (number.equals("122") || number.equals("110") || number.equals("120") || number.equals("119") ||
				number.equals("999") || number.equals("112") || number.equals("000")) {
                /*ToastTool.getToast(context).showMessage("紧急号码，调用系统默认拨号");
                Intent intent = new Intent();
                intent.setAction("com.android.phone.EmergencyDialer.DIAL");
                context.startActivity(intent);*/
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));    //直接拨打
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else {
			if (DeviceUtils.isUIMABSENT(context)) {
				Toast.makeText(context, "手机无卡，无法拨号!", Toast.LENGTH_SHORT).show();
			} else {
				context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getIpCallNumber(number))));
			}
		}

	}

	//去掉+86
	public static String getIpCallNumber(String number) {
		String callNumber = number;
		String phone = null;
		//String trimNumber = preferenceKeyManager.getDialSettings().ipPrefix().get();
		String trimNumber = null;
		if (StringUtils.isNotEmpty(trimNumber)) {
			if (StringUtils.startsWith(number, trimNumber)) {
				phone = StringUtils.substring(number, trimNumber.length());
				if (StringUtils.startsWith(phone, "+86")) {
					callNumber = trimNumber + StringUtils.substring(phone, 3);
				} else if (StringUtils.startsWith(phone, "+")) {
					callNumber = trimNumber + "00" + StringUtils.substring(phone, 1);
				} else {
					callNumber = number;
				}
			}
		}
		return callNumber;
	}
}
