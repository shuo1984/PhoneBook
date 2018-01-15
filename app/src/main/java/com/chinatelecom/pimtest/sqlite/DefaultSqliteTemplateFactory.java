package com.chinatelecom.pimtest.sqlite;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author snowway
 * @since 4/21/11
 */
public class DefaultSqliteTemplateFactory implements SqliteTemplateFactory {

    protected SQLiteOpenHelper sqliteOpenHelper;

    private SqliteTemplate sqliteTemplate;

    public DefaultSqliteTemplateFactory(SQLiteOpenHelper sqliteOpenHelper) {
        this.sqliteOpenHelper = sqliteOpenHelper;
    }

    @Override
    public SqliteTemplate getSqliteTemplate() {
        if (sqliteTemplate == null || !sqliteTemplate.isOpen()) {
            createSqliteTemplate();
        }
        return sqliteTemplate;
    }

    private void createSqliteTemplate() {
        SQLiteDatabase database = sqliteOpenHelper.getWritableDatabase();
        sqliteTemplate = new SqliteTemplate(database);
    }


}
