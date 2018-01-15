package com.chinatelecom.pimtest.utils;

/**
 * Created by Shuo on 2016/10/8.
 */


import java.util.Date;

/**
 * 日期范围类
 *
 * @author snowway
 * @since Jul 2, 2010
 */
public final class DateRange {

    private Date start;

    private Date end;

    public DateRange(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "start:" + start.toString() + ",end:" + end.toString();
    }
}
