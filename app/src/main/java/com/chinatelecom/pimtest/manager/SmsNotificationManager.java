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
        if(listenerColloections==null){
            logger.debug("listenerColloections init!");
            listenerColloections = new HashMap<>();
        }
        if(!listenerColloections.containsKey(event)){
            logger.debug("listenerColloections contain event!");
            List<NotificationListener> listeners = new ArrayList<>();
            listeners.add(listener);
            listenerColloections.put(event,listeners);
        }else{
            logger.debug("listenerColloections not contain event!");
            listenerColloections.get(event).add(listener);
        }
        logger.debug("registerListener done!");
    }

    @Override
    public void unregisterListener(Notification.Event event, NotificationListener listener) {
        if(listenerColloections!=null && listenerColloections.containsKey(event)){
            listenerColloections.get(event).remove(listener);
        }
    }

    @Override
    public void notifyChange(Notification.Event event, SmsItem smsItem) {
        if(listenerColloections!=null && listenerColloections.size()>0 &&listenerColloections.containsKey(event)){
            for(NotificationListener listener : listenerColloections.get(event)){
                Notification notification = new Notification(event,smsItem);
                listener.onChange(notification);
            }
        }
    }


}
