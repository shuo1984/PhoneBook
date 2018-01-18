package com.chinatelecom.pimtest.manager;

import com.chinatelecom.pimtest.interfaces.NotificationListener;
import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.model.Notification;
import com.chinatelecom.pimtest.model.SmsItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shuo on 2018/1/17.
 */

public class SmsNotificationManager extends NotificationManager<SmsItem> {

    private static final Log logger = Log.build(SmsNotificationManager.class);

    private static SmsNotificationManager instance;

    private SmsNotificationManager(){

    }

    public static SmsNotificationManager getInstance(){
        if(instance == null){
            instance = new SmsNotificationManager();
        }

        return instance;
    }

    @Override
    public void registerListener(Notification.Event event,NotificationListener listener) {
        logger.debug("registerListener for event:"+event.toString()+" start");
        if(listenerCollections ==null){
            logger.debug("listenerCollections init!");
            listenerCollections = new HashMap<>();
        }
        if(!listenerCollections.containsKey(event)){
            logger.debug("listenerCollections contain event!");
            List<NotificationListener> listeners = new ArrayList<>();
            listeners.add(listener);
            listenerCollections.put(event,listeners);
        }else{
            logger.debug("listenerCollections not contain event!");
            listenerCollections.get(event).add(listener);
        }
        logger.debug("registerListener done!");
    }

    @Override
    public void unregisterListener(Notification.Event event, NotificationListener listener) {
        if(listenerCollections !=null && listenerCollections.containsKey(event)){
            listenerCollections.get(event).remove(listener);
        }
    }

    @Override
    public void notifyChange(Notification.Event event, SmsItem smsItem) {
        logger.debug("notify change [" + event.toString() + "]");
        if(listenerCollections !=null && listenerCollections.size()>0 && listenerCollections.containsKey(event)){
            for(NotificationListener listener : listenerCollections.get(event)){
                Notification notification = new Notification(event,smsItem);
                listener.onChange(notification);
            }
        }
    }


}
