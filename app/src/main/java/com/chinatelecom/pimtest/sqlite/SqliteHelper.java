package com.chinatelecom.pimtest.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author owensun
 * @since 01/12/2014
 */
public class SqliteHelper extends SQLiteOpenHelper {

    //private ContentResolver contentResolver = CoreManagerFactory.getInstance().getContentResolver();
    //private Log logger = Log.build(SqliteHelper.class);

    public SqliteHelper(Context context) {
        super(context, Schema.Master.DATABASE_NAME, null, Schema.Master.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Schema.Master.Mapping.Sql.CREATE);
        db.execSQL(Schema.Master.Mapping.Sql.CREATE_INDEX);
        db.execSQL(Schema.Master.ContactCache.Sql.CREATE);
        db.execSQL(Schema.Master.MessageCache.Sql.CREATE);
        db.execSQL(Schema.Master.ILogEvent.Sql.CREATE);
        db.execSQL(Schema.Master.Snapshot.Sql.CREATE);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL(Schema.Master.Mapping.Sql.DROP_INDEX);
            db.execSQL(Schema.Master.Mapping.Sql.DROP);
            db.execSQL(Schema.Master.ContactCache.Sql.DROP);
            db.execSQL(Schema.Master.MessageCache.Sql.DROP);
            db.execSQL(Schema.Master.ILogEvent.Sql.DROP);
            db.execSQL(Schema.Master.Snapshot.Sql.DROP);


        }
        onCreate(db);
    }
}
