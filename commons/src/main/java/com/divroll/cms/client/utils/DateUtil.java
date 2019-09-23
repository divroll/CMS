/**
 * Copyright 2017 Dotweblabs Web Technologies
 *
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or or EPL 1.0 (the "Licenses"). You can
 * select the license that you prefer but you may not use this file except in
 * compliance with one of these Licenses.
 *
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 *
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 *
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 *
 */
package com.divroll.cms.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.i18n.client.TimeZoneInfo;
import com.google.gwt.i18n.client.constants.TimeZoneConstants;
import com.google.gwt.i18n.shared.DateTimeFormat;

import java.util.Date;

public class DateUtil {

    public static String getStringBasicFormat(Date inputDate){
        String strFormat = null;
        try{
            DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss");
            strFormat = dateTimeFormat.format(inputDate);
        }catch(Exception e){
            e.printStackTrace();
        }
        return strFormat;
    }

    public static String getStringSimpleFormat(Date inputDate){
        String strFormat = null;
        try{
            DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.MONTH_ABBR_DAY);
            strFormat = dateTimeFormat.format(inputDate);
        }catch(Exception e){
            e.printStackTrace();
        }
        return strFormat;
    }

    // Format: 2017-01-08T04:00:00.000Z
    public static String getStringFormat(Date inputDate){
        final TimeZoneConstants timeZoneConstants = GWT.create(TimeZoneConstants.class);
        final TimeZone latinAmerica = TimeZone.createTimeZone(TimeZoneInfo.buildTimeZoneData(timeZoneConstants.americaSantoDomingo()));
        String strFormat = null;
        try{
            DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.ISO_8601);
            strFormat = dateTimeFormat.format(inputDate, latinAmerica);
            Date d = new Date(strFormat);
            strFormat = dateTimeFormat.format(d, TimeZone.createTimeZone(0));
            String[] s = strFormat.split("\\+");
            strFormat = s[0];
        }catch(Exception e){
            e.printStackTrace();
        }
        return strFormat;
    }
}
