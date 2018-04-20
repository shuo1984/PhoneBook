package com.chinatelecom.pimtest.manager;

import android.database.Cursor;
import android.provider.ContactsContract;

import com.chinatelecom.pimtest.interfaces.Closure;
import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.model.SmsItem;
import com.chinatelecom.pimtest.model.ThreadItem;
import com.chinatelecom.pimtest.sqlite.CursorTemplate;
import com.chinatelecom.pimtest.utils.CursorUtils;
import com.chinatelecom.pimtest.utils.RexseeSMS;
import com.chinatelecom.pimtest.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shuo on 2017/12/18.
 */

public class MessageCacheManager implements CacheManager<SmsItem> {

    private static List<SmsItem> cacheList;
    private String[] contentProjection = new String[] { ContactsContract.PhoneLookup._ID,
            ContactsContract.PhoneLookup.DISPLAY_NAME };
    private RexseeSMS rsms;
    private static MessageManager messageManager = new MessageManager();
    private static Map<Long,String> recipientMap;
    private static Log logger = Log.build(MessageCacheManager.class);
    private static List<ThreadItem> notificationMsgsCache;
    private MessageCacheManager(){

    }

    public static void updateRecipientsCache(){
        if(recipientMap == null){
            recipientMap = new HashMap<>();
        }
        final Cursor cursor = messageManager.findAllRecipients();
        CursorTemplate.each(cursor, new Closure<Cursor>() {
            @Override
            public boolean execute(Cursor input) {
                long _id = CursorUtils.getLong(cursor,"_id");
                String address = CursorUtils.getString(cursor,"address");
                if(!recipientMap.containsKey(_id)){
                    recipientMap.put(_id,address);
                    logger.debug("#### update RecipientCache Id:%d, address:%s", _id, address);
                }
                return true;
            }
        });
    }

    /**
     * @param recipientId
     * @return address
     */
    public static String getRecipientAddressByRecipientId(long recipientId){
          String address = "";
          if(StringUtils.isNotEmpty(recipientMap.get(recipientId))){
              address = recipientMap.get(recipientId);
          }else{
              address = messageManager.findRecipientsByRecipientId(recipientId);
          }
          return address;
    }


    public static Map<Long,String> getRecipientMap(){
        return recipientMap;
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

    public static void updateNotificationMessages(List<ThreadItem> messages){
        notificationMsgsCache = messages;
    }

    public static List<ThreadItem> getNotificationMsgsCache(){
        return notificationMsgsCache;
    }



}
