package com.chinatelecom.pimtest.manager;

import com.chinatelecom.pimtest.interfaces.NotificationListener;
import com.chinatelecom.pimtest.model.Notification;

import java.util.List;
import java.util.Map;

/**
 * Created by Shuo on 2018/1/17.
 */

public abstract class NotificationManager<T> {

    protected Map<Notification.Event,List<NotificationListener>> listenerCollections;

    public abstract void registerListener(Notification.Event event, NotificationListener listener);

    public abstract void unregisterListener(Notification.Event event, NotificationListener listener);

    public abstract void notifyChange(Notification.Event event, T t);

    public Map<Notification.Event,List<NotificationListener>> getListenerColloections(){
        return listenerCollections;
    }

    public int getListenerCollectionCapacity(){
        return listenerCollections.size();
    }
}
