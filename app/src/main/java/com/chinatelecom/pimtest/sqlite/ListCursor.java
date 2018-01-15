package com.chinatelecom.pimtest.sqlite;

import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;

import java.util.List;

/**
 * @author owensun
 * @since 29/11/2014
 */
public class ListCursor<T> implements Cursor {

    private List<T> list;

    private int position = 0;

    public ListCursor(List<T> list) {
        this.list = list;
    }

    public T getItem(){
        return list.get(position);
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public boolean move(int offset) {
        return moveToPosition(offset);
    }

    @Override
    public boolean moveToPosition(int position) {
        if(position < 0 || position > getCount()){
            return false;
        }
        this.position = position;
        return true;
    }

    @Override
    public boolean moveToFirst() {
        position = 0;
        return true;
    }

    @Override
    public boolean moveToLast() {
        position = list.size() - 1;
        return true;
    }

    @Override
    public boolean moveToNext() {
        position++;
        if (position < list.size()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean moveToPrevious() {
        position--;
        return true;
    }

    @Override
    public boolean isFirst() {
        return position == 0;
    }

    @Override
    public boolean isLast() {
        return position == list.size() - 1;
    }

    @Override
    public boolean isBeforeFirst() {
        return false;
    }

    @Override
    public boolean isAfterLast() {
        return false;
    }

    @Override
    public int getColumnIndex(String columnName) {
        return 0;
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return null;
    }

    @Override
    public String[] getColumnNames() {
        return new String[0];
    }

    @Override
    public int getColumnCount() {
        return 0;
    }

    @Override
    public byte[] getBlob(int columnIndex) {
        return new byte[0];
    }

    @Override
    public String getString(int columnIndex) {
        return null;
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

    }

    @Override
    public short getShort(int columnIndex) {
        return 0;
    }

    @Override
    public int getInt(int columnIndex) {
        return 0;
    }

    @Override
    public long getLong(int columnIndex) {
        return 0;
    }

    @Override
    public float getFloat(int columnIndex) {
        return 0;
    }

    @Override
    public double getDouble(int columnIndex) {
        return 0;
    }

    @Override
    public int getType(int columnIndex) {
        return 0;
    }

    @Override
    public boolean isNull(int columnIndex) {
        return false;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public boolean requery() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {

    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {

    }

    public Uri getNotificationUri() {
        return null;
    }


    @Override
    public boolean getWantsAllOnMoveCalls() {
        return false;
    }

    public void setExtras(Bundle extras) {

    }

    @Override
    public Bundle getExtras() {
        return null;
    }

    @Override
    public Bundle respond(Bundle extras) {
        return null;
    }
}
