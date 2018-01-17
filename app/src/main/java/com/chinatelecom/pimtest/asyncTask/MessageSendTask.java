package com.chinatelecom.pimtest.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.chinatelecom.pimtest.log.Log;
import com.chinatelecom.pimtest.manager.MessageManager;
import com.chinatelecom.pimtest.model.SmsItem;
import com.chinatelecom.pimtest.utils.StringUtils;

import java.util.List;

/**
 * @author zhangshuo
 * @since 2015/1/5.
 */
public class MessageSendTask extends AsyncTask {
    private Context context;
    private SmsItem messageInfo;

    private MessageManager msgManager = new MessageManager();
    long messageID = 0L;
    private Log logger = Log.build(MessageSendTask.class);

    public MessageSendTask(Context context, SmsItem messageInfo){
        this.context = context;
        this.messageInfo = messageInfo;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            boolean hasIntertDB = false;
            String[] addresses = messageInfo.getAddress().split(",");
            String messageContent = messageInfo.getBody();

            for (int i = 0; i < addresses.length; i++) {
                if (StringUtils.isNotEmpty(addresses[i])) {

                    if (!hasIntertDB) {
                        messageID = msgManager.insertSendSms(messageInfo);
                        messageInfo.setMessageId(String.valueOf(messageID));
                        logger.debug("MessageSend insert messageID: %d", messageID);
                        hasIntertDB = true;
                    }
                    msgManager.sendTextMessage(context, addresses[i], Long.valueOf(messageInfo.getMessageId()), messageContent,
                            messageInfo.getSubId());
                }
            }
            long threadId = 0;
            if (messageID > 0) {
                threadId = msgManager.findThreadIdByMessageId(messageID);
            } else {
                threadId = msgManager.getThreadId(addresses[0]);
            }
            messageInfo.setThreadId(String.valueOf(threadId));
        }catch (Exception e){
            e.printStackTrace();
        }
        return messageInfo;

    }
}
