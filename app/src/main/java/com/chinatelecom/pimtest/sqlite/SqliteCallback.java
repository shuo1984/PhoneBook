package com.chinatelecom.pimtest.sqlite;

import android.database.sqlite.SQLiteDatabase;

/**
 * SqliteCallback
 *
 * @author snowway
 * @since 2/24/11
 */
public interface SqliteCallback<T> {

    T doInSqlite(SQLiteDatabase database);
}
