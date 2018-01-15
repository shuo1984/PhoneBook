package com.chinatelecom.pimtest.manager;

import com.chinatelecom.pimtest.model.ContactItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shuo on 2017/12/18.
 */

public class ContactCacheManager implements CacheManager<ContactItem> {

    private static List<ContactItem> cache;
    private static Map<Integer, ContactItem> cacheMap;
    private static Map<String,ContactItem> numberContactMap;

    private ContactCacheManager(){
        numberContactMap = new HashMap<>();
    }


    public static synchronized  List<ContactItem> getDefaultCache() {
        if(cache==null){
            cache = new ArrayList<>();
        }
        return cache;
    }

    public static synchronized  Map<Integer,ContactItem> getDefaultCacheMap(){
        if(cacheMap==null){
            cacheMap = new HashMap<Integer, ContactItem>();
        }
        return cacheMap;
    }

    public static synchronized Map<String,ContactItem> getNumberContactMap(){
        return numberContactMap;
    }
}
