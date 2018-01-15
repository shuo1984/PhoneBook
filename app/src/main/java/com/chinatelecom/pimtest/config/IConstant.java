package com.chinatelecom.pimtest.config;

import android.net.Uri;
import android.provider.ContactsContract;

import com.chinatelecom.pimtest.model.APPChannel;

/**
 * Created by Shuo on 2016/10/7.
 */

public class IConstant {


    public final static APPChannel APP_CHANNEL = APPChannel.OFFICIAL;

    public interface Contact{
        Uri ContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    }

    public interface Message {
        static final Uri MMSSMS_FULL_CONVERSATION_URI = Uri.parse("content://mms-sms/conversations");

        static final Uri CONVERSATION_URI = MMSSMS_FULL_CONVERSATION_URI.buildUpon().

        appendQueryParameter("simple", "true").build();

        static final Uri MESSAGE_URI = Uri.parse("content://sms/").buildUpon().build();

        static final Uri THREADS_URI = Uri.parse("content://mms-sms/conversations?simple=true");


        public final static int SMS_MESSAGE_TYPE_ALL = 0;
        public final static int SMS_MESSAGE_TYPE_INBOX = 1;
        public final static int SMS_MESSAGE_TYPE_SENT = 2;
        public final static int SMS_MESSAGE_TYPE_DRAFT = 3;
        public final static int SMS_MESSAGE_TYPE_OUTBOX = 4;
        public final static int SMS_MESSAGE_TYPE_FAILED = 5;
        public final static int SMS_MESSAGE_TYPE_QUEUED = 6;

        public final static int SMS_STATUS_NONE = -1;
        public final static int SMS_STATUS_COMPLETE = 0;
        public final static int SMS_STATUS_PENDING = 32;
        public final static int SMS_STATUS_FAILED = 64;

        public final static int MMS_MESSAGE_BOX_ALL = 0;
        public final static int MMS_MESSAGE_BOX_INBOX = 1;
        public final static int MMS_MESSAGE_BOX_SENT = 2;
        public final static int MMS_MESSAGE_BOX_DRAFTS = 3;
        public final static int MMS_MESSAGE_BOX_OUTBOX = 4;

        public interface Sms {
            String _ID = "_id";
            String THREAD_ID = "thread_id";
            String TYPE = "type";
            String ADDRESS = "address";
            String DATE = "date";
            String READ = "read";
            String STATUS = "status";
            String BODY = "body";
            String LOCKED = "locked";
            String SNIPPET = "snippet";
            String RECIPIENT_IDS = "recipient_ids";
        }
    }

    public interface CallLog{
      public static final Uri CallLogUri = Uri.parse("content://call_log/calls");
    }

    public interface Extra{
        public final static String Prefix = "EXTRA_";
        public final static String Contact = Prefix + "CONTACT";

    }


}
