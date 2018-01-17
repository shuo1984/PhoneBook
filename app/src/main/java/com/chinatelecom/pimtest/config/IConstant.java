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

        Uri SENT_MESSAGE_URI = Uri.parse("content://sms/sent").buildUpon().build();
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

    public interface Params {

        public final static String ID = "ID";
        public final static String TYPE = "TYPE";
        public final static String TIME = "TIME";
        public final static String FROM = "FROM";
        public final static String FLAG = "FLAG";
        public final static String MESSAGE_ID = "MID";
        public final static String CALLBOOK = "CALLBOOK";
        public final static String NUMBER = "NUMBER";
        public final static String NUMBERS = "NUMBERS";
        public final static String FILE = "FILE";
        public final static String MESSAGE_INFO = "MESSAGE_INFO";
        public final static String MESSAGE_THREAD = "MESSAGE_THREAD";
        public final static String CONTACT = "CONTACT";
        public final static String NAME_CARD_INDEX = "name_card_index";
        public final static String CONTACTS = "CONTACTS";
        public final static String CALLLOG = "CALLLOG";
        public final static String CALLLOGS = "CALLLOGS";
        public final static String CALLTYPE = "CALLTYPE";
        public final static String NAMECARD = "NAME_CARD";
        public final static String CALLNAMES = "CALLNAMES";
        public final static String LOCAL_RECOVERY = "LOCAL_RECOVERY";
        public final static String WEB_LIFE_UPDATE_VERSION = "web_life_update_version";
        public final static String COUNT_KEY = "getSelectContacts";
        public final static String IS_CALL_LOG = "isCalllog";
        public final static String NAME_RETURN = "return_name_from_edit";
        public final static String DISPLAYNAME_RETURN = "return_diplayname_from_edit";
        public final static String TELEPHONE = "yulore_recognitionTelephone";
        public final static String ACCOUNT_TOKEN = "account_token";
        public final static String SEND_ENCRYPTION_MESSAGE_ACTION = "com.chinatelecom.pim.sendencryptionmessage";
        public final static String USER_ID = "user_id";
        public final static String PAY_ID = "90216468";//计费点
        public final static String VCARD_FILES = "vcard_files";
        public final static String SIM_SLOT = "sim_slot";
        public final static int RESULT_TOKEN = 78;

        public final static String SHOW_LEFT_VIEW = "SHOW_LEFT_VIEW";
        public final static String SHOW_RIGHT_VIEW = "SHOW_RIGHT_VIEW";
        public final static String IS_GO_BACK = "IS_GO_BACK";
        public final static String FLAG_ENCRYPTION_MESSAGE_SMSSIGN = "flag_encryption_message_smssign";
        public final static String FLAG_ENCRYPTION_MESSAGE_PHONEN_NUMBER = "flag_encryption_message_phonen_number";
        public final static String SMSBODY = "smsbody";
        public final static String FLAG_WEB_MESSAGE_SMSSIGN = "flag_web_message_smssign";
        public final static String FLAG_WEB_MESSAGE_PHONENUMBER = "flag_web_message_phonenumber";
        public final static String LATELYCONTACTS = "lately_contacts";
        public final static String MARKER = "marker";

    }

    public interface Extra{
        public final static String Prefix = "EXTRA_";
        public final static String Contact = Prefix + "CONTACT";

    }

    public interface Action {
        public static final String BROADCAST_MISSED_CALLS = "com.chinatelecom.pim.activity.MissedCall_intent";
        public static final String BROADCAST_UNREAD_MSGS = "com.chinatelecom.pim.activity.UnReadMsg_intent";

        public static final String SMS_SENT = "ACTION.SMS_SENT";
        public static final String SMS_DELIVER = "ACTION.SMS_DELIVERED";
        public final static String CHOOSE_CARD_PHOTO = "ACTION.CHOOSE_CARD_PHOTO";
        /*  public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";*/
        public final static String CALL_SERVICE_STATE_ACTION = "com.chinatelecom.pim.CALL_SERVICE_STATE";
        public final static String PTASK_SERVICE_STATE_ACTION = "com.chinatelecom.pim.GTaskService";
        public final static String ACTION = "action";
        public final static String CALL_CONTENT = "call_content";

        public static final String PIM_CALL_WIGHT_PROVIDER = "com.chinatelecom.pim.appWidgetUpdate.CALL";
        public static final String PIM_DIAL_WIGHT_PROVIDER = "com.chinatelecom.pim.appWidgetUpdate.DIAL";
        public static final String PIM_MSG_WIGHT_PROVIDER = "com.chinatelecom.pim.appWidgetUpdate.MSG";
        public static final String PIM_CONTACT_WIGHT_PROVIDER = "com.chinatelecom.pim.appWidgetUpdate.CONTACT";
        public static final String PIM_CONTACT_DETAIL_CALL_WIGHT_PROVIDER = "com.chinatelecom.pim.appWidgetUpdate.CONTACT_DETAIL_CALL";
        public static final String FAST_REPLY_EDIT = "com.chinatelecom.pim.fastReplyEdit";
        public static final String NotificationMessageList = "android.intent.action.MeesageNotifications";
        public final static String WEB_LIFE_UPATE = "com.chinatelecom.pim.ACTION_WEB_LIFE_UPDATE";
        public static final String CALL_LOG = "content://call_log/calls";

        public interface Flag {
            public static final int ADD_TO_CONTACT = 2;
        }

        public interface Place {
            public final static int CONTACT_LIST = 1;
            public final static int CALLLOG_LIST = 2;
            public final static int CONTACT_EDIT = 3;
            public final static int PERSON_CENTER = 4;
            public final static int NAME_CARD_INFO = 5;
            public final static int SETTING_FAST_DIAL = 6;
            public final static int SETTING_GESTURE_DIAL = 8;
            public final static int MSG_LINK = 7;
            public final static int LOCAL_RECOVERY = 9;
            public final static int CONTACT_DETAIL = 10;
            public final static int CARD_EDIT = 11;
            public final static int FOUND_WEB = 12;
            public final static int CONTACT_SHARED = 13;
            public final static int MYCARDSHARED_CREATE = 14;
            public final static int SETTING_HOME = 15;
            public final static int CONTACT_BACKUP_SETING_PAGE = 16;
            public final static int CONTACT_SHARE_MENU = 20;
            public final static int CONTACT_DELETE_MENU = 21;
        }
    }

    public interface Permissions{
        public static final int REQ_CODE_ALL = 11;
        public static final int REQ_CODE_CAMERA = 22;
        public static final int REQ_CODE_CONTACT = 33;
        public static final int REQ_CODE_SMS = 44;
        public static final int REQ_CODE_CALL_LOG = 55;
    }


}
