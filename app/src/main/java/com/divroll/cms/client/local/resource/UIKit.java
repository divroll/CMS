/*
 * Divroll, Platform for Hosting Static Sites
 * Copyright 2018 to present, Divroll, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.divroll.cms.client.local.resource;

import com.google.gwt.core.client.ScriptInjector;

public class UIKit {

    public static void inject(){
        ScriptInjector.fromString(Resources.INSTANCE.uikit().getText())
                .setWindow(ScriptInjector.TOP_WINDOW)
                .inject();

        ScriptInjector.fromString(Resources.INSTANCE.slideShow().getText())
                .setWindow(ScriptInjector.TOP_WINDOW)
                .inject();

        ScriptInjector.fromString(Resources.INSTANCE.slideSet().getText())
                .setWindow(ScriptInjector.TOP_WINDOW)
                .inject();
        ScriptInjector.fromString(Resources.INSTANCE.accordion().getText())
                .setWindow(ScriptInjector.TOP_WINDOW)
                .inject();

        ScriptInjector.fromString(Resources.INSTANCE.notification().getText())
                .setWindow(ScriptInjector.TOP_WINDOW)
                .inject();
//        ScriptInjector.fromString(Resources.INSTANCE.datepicker().getText())
//                .setWindow(ScriptInjector.TOP_WINDOW)
//                .inject();
        ScriptInjector.fromString(Resources.INSTANCE.formPassword().getText())
                .setWindow(ScriptInjector.TOP_WINDOW)
                .inject();
    }

    public static native void notif(String message, String status, Long timeout, String position)/*-{
        var pos;
        if(position) {
            pos = position;
        }else {
            pos = 'top-center';
        }
        $wnd.UIkit.notify({
            message : message,
            status  : status,
            timeout : timeout,
            pos     : pos
        });
    }-*/;

}
