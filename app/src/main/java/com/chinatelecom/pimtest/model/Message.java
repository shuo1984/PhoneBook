package com.chinatelecom.pimtest.model;

/**
 * Created by Shuo on 2016/10/9.
 */

public class Message {
    private String name;
    private String date;
    private String text;
    private int layoutID;

    public Message(){
    }

    public Message(String name, String date, String text, int layoutID){
        super();
        this.name = name;
        this.date = date;
        this.text = text;
        this.layoutID = layoutID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public int getLayoutID(){
        return layoutID;
    }

    public void setLayoutID(int layoutID){
        this.layoutID = layoutID;
    }
}
