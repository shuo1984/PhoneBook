package com.chinatelecom.pimtest.fragement;


import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chinatelecom.pimtest.R;
import com.chinatelecom.pimtest.activity.ContactDetailActivity;
import com.chinatelecom.pimtest.adapter.ContactListAdapter;
import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.manager.ContactCacheManager;
import com.chinatelecom.pimtest.model.ContactItem;
import com.chinatelecom.pimtest.view.QuickAlphabeticBar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactListFragment extends Fragment {

    private ContactListAdapter adapter;
    private ListView contactList;
    private List<ContactItem> list;
    private AsyncQueryHandler asyncQueryHandler; // 异步查询数据库类对象
    private QuickAlphabeticBar alphabeticBar; // 快速索引条

    private Map<Integer, ContactItem> contactIdMap = null;
    private Map<String,ContactItem> numberContactMap;
    private View mView;


    public ContactListFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        contactList = (ListView)mView.findViewById(R.id.contact_list);
        alphabeticBar = (QuickAlphabeticBar)mView.findViewById(R.id.fast_scroller);

        // 实例化
        asyncQueryHandler = new ContactAsyncQueryHandler(getActivity().getContentResolver());
        init();
        return mView;
    }

    /**
     * 初始化数据库查询参数
     */
    private void init() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI; // 联系人Uri；
        // 查询的字段
        String[] projection = { ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
                "phonebook_label"};


        // 按照sort_key升序查詢
        asyncQueryHandler.startQuery(0, null, uri, projection, null, null,
                "sort_key COLLATE LOCALIZED asc");

    }


    /**
     *
     * @author Administrator
     *
     */
    private class ContactAsyncQueryHandler extends AsyncQueryHandler {

        public ContactAsyncQueryHandler(ContentResolver cr) {
            super(cr);
        }

        @Override
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
            if (cursor != null && cursor.getCount() > 0) {
                contactIdMap = ContactCacheManager.getDefaultCacheMap();
                list = new ArrayList<ContactItem>();
                cursor.moveToFirst(); // 游标移动到第一项
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String name = cursor.getString(1);
                    String number = cursor.getString(2);
                    String sortKey = cursor.getString(3);
                    int contactId = cursor.getInt(4);
                    Long photoId = cursor.getLong(5);
                    String lookUpKey = cursor.getString(6);
                    String firstLetter = cursor.getString(7);

                    if (contactIdMap.containsKey(contactId)) {
                        // 无操作
                    } else {
                        // 创建联系人对象
                        ContactItem contact = new ContactItem();
                        contact.setDesplayName(name);
                        contact.setPhoneNum(number);
                        contact.setSortKey(sortKey);
                        contact.setPhotoId(photoId);
                        contact.setLookUpKey(lookUpKey);
                        contact.setFirstLetter(firstLetter);

                        list.add(contact);

                        if(!contactIdMap.containsKey(contactId)) {
                            contactIdMap.put(contactId, contact);
                        }
                    }
                }
                if (list.size() > 0) {
                    setAdapter(list);
                }
            }

            super.onQueryComplete(token, cookie, cursor);
            if(cursor!=null && !cursor.isClosed()) {
                cursor.close();
            }
        }



    }

    private void setAdapter(List<ContactItem> list) {
        adapter = new ContactListAdapter(getActivity(), list, alphabeticBar);
        contactList.setAdapter(adapter);
        contactList.setOnItemClickListener(new ContactItemClickListener(list));
        alphabeticBar.init(getActivity());
        alphabeticBar.setListView(contactList);
        alphabeticBar.setHight(alphabeticBar.getHeight());
        alphabeticBar.setVisibility(View.VISIBLE);
    }

    class ContactItemClickListener implements AdapterView.OnItemClickListener {

        private List<ContactItem> contactList;

        public ContactItemClickListener(List<ContactItem> contacts){
            this.contactList = contacts;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                ContactItem contact = this.contactList.get(position);
                Intent intent = new Intent();
                intent.setClass(getActivity(), ContactDetailActivity.class);
                intent.putExtra(IConstant.Extra.Contact, (Serializable) contact);
                startActivity(intent);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
