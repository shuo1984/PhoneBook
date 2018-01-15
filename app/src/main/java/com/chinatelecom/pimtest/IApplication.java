package com.chinatelecom.pimtest;

import android.app.Application;
import android.content.Context;

import com.chinatelecom.pimtest.sqlite.DefaultSqliteTemplateFactory;
import com.chinatelecom.pimtest.sqlite.SqliteHelper;
import com.chinatelecom.pimtest.sqlite.SqliteTemplate;

/**
 * Created by Shuo on 2017/12/20.
 */

public class IApplication extends Application {
    private static Context context;
    private static SqliteTemplate sqliteTemplate;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        sqliteTemplate = new DefaultSqliteTemplateFactory(new SqliteHelper(context)).getSqliteTemplate();
    }

    public static Context getContext() {
        return context;
    }

    public static SqliteTemplate getSqliteTemplate(){
        return sqliteTemplate;
    }

}
