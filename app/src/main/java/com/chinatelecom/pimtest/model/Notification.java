package com.chinatelecom.pimtest.model;

/**
 * @author owensun
 * @since 18/03/2015
 */
public class Notification {

    public enum Mode {
        FILTER, NOT_FILTER
    }

    public enum Status {
        NORMAL, PAUSE
    }

    public enum Event {
        ACCOUNT_CHANGED("帐户改变"),
        CONTACT_CHANGED("联系人改变"),
        MESSAGE_CHANGED("消息改变"),
        CALLLOG_CHANGED("通话记录改变"),
        NETWORK_CHANGED("网络状态改变"),
        PHONE_STATE_CHANGED("手机状态改变"),
        CALL_STATE_RINGING("来电振铃"),
        CALL_STATE_IDLE("来电空闲"),
        CALL_STATE_OFFHOOK("来电摘机"),
        SMS_SENT("短信发送回执"),
        SMS_SEND_FAILURE("短信发送失败"),
        SMS_DELIVER("短信发送报告"),
        SMS_RECEIVED("短信已接收回执"),
        SYNC_START("同步开始"),
        SYNC_FINISH("同步结束"),
        CTPASS_ACTIVATED("CTPASS卡应用激活");

        String desc;

        Event(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return desc;
        }
    }

    private Event event;
    private Object data;

    public Notification(Event event) {
        this.event = event;
    }

    public Notification(Event event, Object data) {
        this.event = event;
        this.data = data;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
