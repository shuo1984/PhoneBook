package com.chinatelecom.pimtest.fragement;


import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.activity.AboutActivity;
import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.interfaces.Closure;
import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.model.DataFormat;
import com.chinatelecom.pimtest.model.HTTPMethod;
import com.chinatelecom.pimtest.model.HTTPParams;
import com.chinatelecom.pimtest.net.HttpHelper;
import com.chinatelecom.pimtest.utils.DownloadAppUtils;
import com.chinatelecom.pimtest.utils.UpdateAppUtils;
import com.zhy.m.permission.MPermissions;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import org.json.JSONException;
import org.json.JSONObject;



/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalCenterFragment extends Fragment {

    private TextView aboutUs, checkUpdate;
    private Log logger = Log.build(PersonalCenterFragment.class);

    public PersonalCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);

        aboutUs = (TextView) view.findViewById(R.id.tv_about);
        checkUpdate = (TextView)view.findViewById(R.id.tv_check_update);
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });

        checkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.READ_EXTERNAL_STORAGE};
                MPermissions.requestPermissions(PersonalCenterFragment.this,
                        IConstant.Permissions.REQ_CODE_EXTERNAL_STORAGE,
                        permissions
                        );

            }
        });
        return view;
    }

    private void checkVersion() {
        new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                HTTPParams params = new HTTPParams(HTTPMethod.GET, DataFormat.Normal);
                HttpHelper.sendHttpRequest(IConstant.API.updateApi, params, new Closure() {
                    @Override
                    public boolean execute(Object input) {
                        String jsonResult = input.toString();
                        try {
                            JSONObject jo = new JSONObject(jsonResult);
                            int rescode = jo.getInt("res_code");
                            String resMsg = jo.getString("res_msg");
                            if(rescode==0){
                                final int versionCode = jo.getJSONObject("updateInfo").getInt("versionCode");
                                final String versionName = jo.getJSONObject("updateInfo").getString("versionName");
                                String appName = jo.getJSONObject("updateInfo").getString("appName");
                                final String updateUrl = jo.getJSONObject("updateInfo").getString("updateUrl");
                                logger.debug("VersionCode=" + versionCode + "|VersionName=" + versionName);
                                logger.debug("appName=" + appName + "|updateUrl=" + updateUrl);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        doUpdate(versionCode,versionName,updateUrl);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        return true;
                    }
                });

                return null;
            }
        }.execute();


    }

    private void doUpdate(int versionCode,String versionName,String downloadPath){
        UpdateAppUtils.from(getActivity())
                .checkBy(UpdateAppUtils.CHECK_BY_VERSION_CODE)
                .serverVersionCode(versionCode)
                .serverVersionName(versionName)
                .apkPath(downloadPath)
                .showNotification(true)
                .updateInfo("本次更新修复了部分短信问题")
                .downloadBy(UpdateAppUtils.DOWNLOAD_BY_APP)
                .isForce(false)
                .needFitAndroidN(true)
                .update();
    }


    @PermissionGrant(IConstant.Permissions.REQ_CODE_EXTERNAL_STORAGE)
    public void requestContactSuccess()
    {
        Toast.makeText(getActivity(), "申请外部存储权限成功！", Toast.LENGTH_SHORT).show();
        checkVersion();
    }

    @PermissionDenied(IConstant.Permissions.REQ_CODE_EXTERNAL_STORAGE)
    public void requestContactFailed()
    {
        Toast.makeText(getActivity(), "申请外部存储权限失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
