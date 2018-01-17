package com.chinatelecom.pimtest.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.service.PhoneBookService;
import com.chinatelecom.pimtest.sqlite.SqliteTemplateFactory;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.util.List;

public class LaunchActivity extends Activity {
    private SqliteTemplateFactory sqliteTemplateFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        requestPermission();
        if (!isServiceRunning(this, PhoneBookService.class.getName())) {
            startService(new Intent(LaunchActivity.this, PhoneBookService.class));
        }
    }

    private void requestPermission() {
       String[] permissions = new String[]{Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG,
                Manifest.permission.READ_PHONE_STATE
        };

        MPermissions.requestPermissions(LaunchActivity.this, IConstant.Permissions.REQ_CODE_ALL,permissions);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(IConstant.Permissions.REQ_CODE_ALL)
    public void requestAllPermissionSuccess(){
        Toast.makeText(LaunchActivity.this,"权限申请成功!",Toast.LENGTH_SHORT).show();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LaunchActivity.this,HomeActivity.class));
            }
        };

        new Handler().postDelayed(runnable,3000);
    }

    @PermissionDenied(IConstant.Permissions.REQ_CODE_ALL)
    public void requestAllPermissionFail(){
        Toast.makeText(LaunchActivity.this,"权限申请失败!",Toast.LENGTH_SHORT).show();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


    public boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService
                (ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices
                (Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
}
