package com.chinatelecom.pimtest.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.sqlite.SqliteTemplateFactory;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

public class LaunchActivity extends Activity {
    private SqliteTemplateFactory sqliteTemplateFactory;
    private static final int REQ_PERMISSIONS_CODE = 21;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        requestPermission();
    }

    private void requestPermission() {
       String[] permissions = new String[]{Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_CALL_LOG,
                Manifest.permission.WRITE_CALL_LOG
        };

        MPermissions.requestPermissions(LaunchActivity.this,REQ_PERMISSIONS_CODE,permissions);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @PermissionGrant(REQ_PERMISSIONS_CODE)
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

    @PermissionDenied(REQ_PERMISSIONS_CODE)
    public void requestAllPermissionFail(){
        Toast.makeText(LaunchActivity.this,"权限申请失败!",Toast.LENGTH_SHORT).show();
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
