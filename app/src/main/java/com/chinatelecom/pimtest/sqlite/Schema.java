package com.chinatelecom.pimtest.sqlite;


/**
 * @author owensun
 * @since 01/12/2014
 */
public class Schema {

    public interface Environment {
        /*   String DATABASE_NAME = "ctpim.db";
     int DATABASE_VERSION = 2014070400;*/

        /* String DATABASE_HCODE_NAME = "attribution.db";
     int DATABASE_HCODE_VERSION = 2014101600;*/
    }

    public interface Master {
        public final static String DATABASE_NAME = "ctpim.db";
        public final static int DATABASE_VERSION = 2017122614;

        //映射
        public interface Mapping {
            public final static String TABLE_NAME = "mappings";

            public interface Columns {
                public final static String ID = "raw_contact_id";
                public final static String CLIENT_ID = "client_id";
                public final static String CLIENT_VERSION = "client_version";
                public final static String CLIENT_PHOTO_ID = "client_photo_id";
                public final static String SERVER_ID = "server_id";
                public final static String SERVER_VERSION = "server_version";
                public final static String SERVER_PORTRAIT_VERSION = "server_portrait_version";
                public final static String DATA_TYPE = "data_type";
            }

            public interface Indexs {
                public final static String CLIENT_ID = "idx_clientid";
                public final static String SERVER_ID = "idx_serverid";
            }

            public interface Sql {
                public static final String CREATE =
                        "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                "(" + Columns.ID + " INTEGER PRIMARY KEY," +
                                Columns.CLIENT_ID + " INTEGER," +
                                Columns.CLIENT_VERSION + " INTEGER," +
                                Columns.CLIENT_PHOTO_ID + " INTEGER," +
                                Columns.SERVER_ID + " INTEGER," +
                                Columns.SERVER_VERSION + " INTEGER," +
                                Columns.SERVER_PORTRAIT_VERSION + " INTEGER," +
                                Columns.DATA_TYPE + " varchar" +
                                ")";

                public static final String CREATE_INDEX =
                        "CREATE INDEX IF NOT EXISTS " + Indexs.CLIENT_ID + " ON " + TABLE_NAME + " (" + Columns.CLIENT_ID + ");" +
                                "CREATE INDEX IF NOT EXISTS " + Indexs.SERVER_ID + " ON " + TABLE_NAME + " (" + Columns.SERVER_ID + ");";

                public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

                public static final String DROP_INDEX =
                        "DROP INDEX IF EXISTS " + Indexs.CLIENT_ID + ";" +
                                "DROP INDEX IF EXISTS " + Indexs.SERVER_ID + ";";
            }
        }

        //联系人缓存
        public interface ContactCache {
            public final static String TABLE_NAME = "contact_caches";

            public interface Columns {
                public final static String ID = "raw_contact_id";
                public final static String CONTACT_ID = "contact_id";
                public final static String STARRED = "stared";
                public final static String VERSION = "version";
                public final static String ACCOUNT_TYPE = "account_type";
                public final static String ACCOUNT_NAME = "account_name";
                public final static String DISPLAY_NAME = "display_name";
                public final static String PHONES = "phones";
                public final static String GROUPS = "groups";
            }

            public interface Indexs {
            }

            public interface Sql {
                public static final String CREATE =
                        "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                "(" + Columns.ID + " INTEGER PRIMARY KEY," +
                                Columns.CONTACT_ID + " INTEGER," +
                                Columns.STARRED + " INTEGER," +
                                Columns.VERSION + " INTEGER," +
                                Columns.ACCOUNT_TYPE + " TEXT," +
                                Columns.ACCOUNT_NAME + " TEXT," +
                                Columns.DISPLAY_NAME + " TEXT," +
                                Columns.GROUPS + " TEXT," +
                                Columns.PHONES + " TEXT" +
                                ")";

                public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
            }
        }

        //短信缓存
        public interface MessageCache {
            public final static String TABLE_NAME = "message_caches";

            public interface Columns {
                public final static String _ID = "_id";
                public final static String THREAD_ID = "thread_id";
                public final static String ADDRESS = "address";
                public final static String DATE = "date";
                public final static String BODY = "body";
                public final static String READ = "read";
                public final static String TYPE = "type";
                public final static String LOCKED = "locked";
                public final static String SNIPPET = "snippet";
                public final static String RECIPIENT_IDS = "recipient_ids";
                public final static String HAS_DRAFT = "has_draft";
            }

            public interface Sql {
                String CREATE =
                        "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                "(" + Columns._ID + " INTEGER PRIMARY KEY," +
                                Columns.THREAD_ID + " INTEGER," +
                                Columns.ADDRESS + " TEXT," +
                                Columns.BODY + " TEXT," +
                                Columns.SNIPPET + " TEXT," +
                                Columns.DATE + " INTEGER," +
                                Columns.READ + " INTEGER DEFAULT 0," +
                                Columns.LOCKED + " INTEGER DEFAULT 0," +
                                Columns.RECIPIENT_IDS + " TEXT," +
                                Columns.HAS_DRAFT + " INTEGER DEFAULT 0 " +
                                ")";

                String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;
            }
        }

        //统计缓存
        public interface ILogEvent {

            public final static String TABLE_NAME = "log_event";

            public interface Columns {
                public final static String _ID = "_id";
                public final static String EVENT_TYPE = "event_type";
                public final static String EVENT_SOURCE = "event_source";
                public final static String CLIENT_VERSION = "client_version";
                public final static String EVENT_TIME = "event_time";
                public final static String IMSI = "imsi";

            }

            interface Sql {
                String CREATE =
                        "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                "(" + Columns._ID + " INTEGER PRIMARY KEY," +
                                Columns.EVENT_TYPE + " INTEGER," +
                                Columns.EVENT_SOURCE + " INTEGER," +
                                Columns.CLIENT_VERSION + " TEXT," +
                                Columns.EVENT_TIME + " TEXT," +
                                Columns.IMSI + " TEXT" +
                                ")";

                String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

            }

        }


        public interface Snapshot {
            public final static String TABLE_NAME = "snapshot";

            public interface Columns {
                public final static String ID = "raw_contact_id";
                public final static String NAME = "display_name";
                public final static String VERSION = "version";
                public final static String DATA = "contact_data";
                public final static String FAVORITE = "favorite";
                public final static String PHOTOID = "PHOTOID";
                public final static String CONTACTID = "CONTACTID";
                public final static String MD5SUMMARY = "MD5SUMMARY";

            }

            public interface Sql {
                public static final String CREATE =
                        "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                "(" + Columns.ID + " INTEGER PRIMARY KEY," +
                                Columns.NAME + " varchar," +
                                Columns.VERSION + " INTEGER," +
                                Columns.FAVORITE + " INTEGER," +
                                Columns.PHOTOID + " INTEGER," +
                                Columns.CONTACTID + " INTEGER," +
                                Columns.DATA + " blob," +
                                Columns.MD5SUMMARY + " varchar" +
                                ")";

                public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

                public static final String INSERT_OR_UPDATE = "insert or replace into " + TABLE_NAME + "(" + Columns.ID + "," + Columns.NAME + "," + Columns.VERSION + "," + Columns.DATA + "," + Columns.FAVORITE + "," + Columns.PHOTOID + "," + Columns.CONTACTID + "," + Columns.MD5SUMMARY + ") values (?,?,?,?,?,?,?,?)";

                public static final String DELETE_BY_NAME = "delete from " + TABLE_NAME + " where " + Columns.NAME + "=?";

                public static final String DELETE_BY_ID = "delete from " + TABLE_NAME + " where " + Columns.ID + "=?";

                public static final String SELECT_SIMPLE = "select " + Columns.ID + "," + Columns.NAME + "," + Columns.VERSION + "," + Columns.FAVORITE + "," + Columns.MD5SUMMARY + " from " + TABLE_NAME + " order by " + Columns.ID + " asc";

                public static final String SELECT_All = "select " + Columns.ID + "," + Columns.NAME + "," + Columns.VERSION + "," + Columns.DATA + "," + Columns.FAVORITE + "," + Columns.PHOTOID + "," + Columns.CONTACTID + "," + Columns.MD5SUMMARY + " from " + TABLE_NAME + " order by " + Columns.ID + " asc";

                public static final String SELECT_BY_NAME = "select " + Columns.ID + "," + Columns.NAME + "," + Columns.VERSION + "," + Columns.DATA + "," + Columns.FAVORITE + " from " + TABLE_NAME + " where " + Columns.NAME + "=?";

                public static final String SELECT_BY_ID = "select " + Columns.ID + "," + Columns.NAME + "," + Columns.VERSION + "," + Columns.DATA + "," + Columns.FAVORITE + " from " + TABLE_NAME + " where " + Columns.ID + "=?";

            }

        }


        public interface PublicTelephones {
            public final static String TABLE_NAME = "public_telephones";

            public interface Columns {
                public final static String _ID = "_id";
                public final static String CP_TEL = "CP_TEL";
                public final static String CP_NAME = "CP_NAME";
            }

            public interface Indexs {
                public final static String CP_TEL = "idx_cptel";
            }

            public interface Sql {
                public final static String CREATE =
                        "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                "(" + Columns._ID + " INTEGER PRIMARY KEY," +
                                Columns.CP_TEL + " TEXT," +
                                Columns.CP_NAME + " TEXT" +
                                ")";

                public final static String CREATE_INDEX = "CREATE INDEX " + Indexs.CP_TEL +
                        " ON " + TABLE_NAME + " (" + Columns.CP_TEL + ")";

                public final static String DROP = "DROP TABLE IF EXISTS " + PublicTelephones.TABLE_NAME;

                public final static String DROP_INDEX =
                        "DROP INDEX IF EXISTS " + Indexs.CP_TEL;
            }
        }

        public interface Hcodes {
            public final static String TABLE_NAME = "hcodes";

            public interface Columns {
                public final static String _ID = "_id";
                public final static String NUMBER = "number";
                public final static String AREA_CODE = "area_code";
            }

            public interface Indexs {
                public final static String NUMBER = "idx_number";
            }

            interface Sql {
                String CREATE =
                        "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                                "(" + Columns._ID + " INTEGER PRIMARY KEY," +
                                Columns.NUMBER + " TEXT," +
                                Columns.AREA_CODE + " TEXT" +
                                ")";
                String CREATE_INDEX = "CREATE INDEX " + Indexs.NUMBER +
                        " ON " + TABLE_NAME + " (" + Columns.NUMBER + ")";

                String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

                public final static String DROP_INDEX =
                        "DROP INDEX IF EXISTS " + Indexs.NUMBER;
            }
        }

    }


}
