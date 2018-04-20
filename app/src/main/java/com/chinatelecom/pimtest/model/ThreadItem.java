package com.chinatelecom.pimtest.model;

import java.util.List;

/**
 * Created by Shuo on 2018/4/4.
 */

public class ThreadItem {
    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
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

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private long threadId;
    private long date;
    private String messageCount;
    private String recipient_ids;
    private String snippet;
    private String snippet_cs;
    private String read;
    private String error;
    private String has_attachment;
    private List<String> address;
    private int type;
}
