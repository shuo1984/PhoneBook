package com.chinatelecom.pimtest.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Shuo on 2018/1/9.
 * 通话记录模型类
 */

public class CallLogItem implements Comparable<CallLogItem>{

    private String name;
    private String number;
    private int callType;
    private int callDuration;
    private long callDate;
    private String callDateStr;
    private List<CallLogItem> details;
    private String dualSimCardStr;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public int getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(int callDuration) {
        this.callDuration = callDuration;
    }

    public long getCallDate() {
        return callDate;
    }

    public void setCallDate(long callDate) {
        this.callDate = callDate;
    }

    public String getCallDateStr() {
        return callDateStr;
    }

    public void setCallDateStr(String callDateStr) {
        this.callDateStr = callDateStr;
    }

    public List<CallLogItem> getDetails() {
        return details;
    }

    public String getDualSimCardStr() {
        return dualSimCardStr;
    }

    public void setDualSimCardStr(String dualSimCardStr) {
        this.dualSimCardStr = dualSimCardStr;
    }

    public void addSubCallLog(CallLogItem item){
        if(details==null){
            details = new ArrayList<>();
        }
        this.details.add(item);
        Collections.sort(details);
    }

    @Override
    public int compareTo(@NonNull CallLogItem item) {
        if(this.callDate<item.getCallDate()){
            return 1;
        }else if(this.callDate==item.getCallDate()){
            return 0;
        }else{
            return -1;
        }
    }
}
