package com.chinatelecom.pimtest.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.model.ContactItem;

public class ContactDetailActivity extends AppCompatActivity {

    private TextView nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        setupViews();
        initData();

    }

    private void setupViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("联系人详情");
        setSupportActionBar(toolbar);
        nameView = (TextView)findViewById(R.id.name);


    }

    private void initData() {
        ContactItem contact = (ContactItem)getIntent().getSerializableExtra(IConstant.Extra.Contact);
        if(contact!=null){
            nameView.setText(contact.getDesplayName());
        }
    }
}
