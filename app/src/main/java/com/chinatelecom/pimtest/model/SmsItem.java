package com.chinatelecom.pimtest.model;

/**
 * Created by Shuo on 2016/10/7.
 * 短信模型类
 */

public class SmsItem {

    private String threadId;
    private String messageId;
    private long date;
    private String messageCount;
    private String recipient_ids;
    private String snippet;
    private String snippet_cs;
    private String read;
    private String error;
    private String has_attachment;
    private String address;
    private int type;
    private int smsStatus;

    private String body;
    private int subId;
    private int conversationLayoutID;

    public final static int SMS_MESSAGE_TYPE_INBOX = 1;
    public final static int SMS_MESSAGE_TYPE_SENT = 2;
    public final static int SMS_MESSAGE_TYPE_DRAFT = 3;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(String messageCount) {
        this.messageCount = messageCount;
    }

    public String getRecipient_ids() {
        return recipient_ids;
    }

    public void setRecipient_ids(String recipient_ids) {
        this.recipient_ids = recipient_ids;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getSnippet_cs() {
        return snippet_cs;
    }

    public void setSnippet_cs(String snippet_cs) {
        this.snippet_cs = snippet_cs;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getHas_attachment() {
        return has_attachment;
    }

    public void setHas_attachment(String has_attachment) {
        this.has_attachment = has_attachment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(int smsStatus) {
        this.smsStatus = smsStatus;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }


    public int getConversationLayoutID() {
        return conversationLayoutID;
    }

    public void setConversationLayoutID(int conversationLayoutID) {
        this.conversationLayoutID = conversationLayoutID;
    }


    public SmsItem(){

    }

    public SmsItem(String threadId, String msgCount, String msgSnippet) {
        this.threadId = threadId;
        this.messageCount = msgCount;
        this.snippet = msgSnippet;
    }

}
