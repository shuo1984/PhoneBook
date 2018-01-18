package com.chinatelecom.pimtest.activity;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.utils.DeviceUtils;
import com.chinatelecom.pimtest.utils.StringUtils;

public class AboutActivity extends AppCompatActivity {

    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setupViews();
    }

    private void setupViews() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("关于");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        version = (TextView)findViewById(R.id.app_version);
        try {
            version.setText(DeviceUtils.getVersionName(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
