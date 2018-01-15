package com.chinatelecom.pimtest.manager;

import android.provider.ContactsContract;

import com.chinatelecom.pimtest.model.SmsItem;
import com.chinatelecom.pimtest.utils.RexseeSMS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shuo on 2017/12/18.
 */

public class MessageCacheManager implements CacheManager<SmsItem> {

    private static List<SmsItem> cacheList;
    private String[] contentProjection = new String[] { ContactsContract.PhoneLookup._ID,
            ContactsContract.PhoneLookup.DISPLAY_NAME };
    private RexseeSMS rsms;

    private MessageCacheManager(){

    }

    public static synchronized List<SmsItem> getDefaultCacheList(){
        if(cacheList==null){
            cacheList = new ArrayList<>();
        }
        return cacheList;
    }

    public static synchronized  void updateCache(List<SmsItem> messages){
        getDefaultCacheList();
        cacheList = messages;
    }
}
