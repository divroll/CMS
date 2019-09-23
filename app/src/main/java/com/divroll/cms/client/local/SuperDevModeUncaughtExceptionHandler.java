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
package com.divroll.cms.client.local;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.event.shared.UmbrellaException;

public class SuperDevModeUncaughtExceptionHandler implements GWT.UncaughtExceptionHandler {

    @Override
    public void onUncaughtException(final Throwable t) {
        logException(t, false);
    }

    private void logException(Throwable t, boolean isCause) {
        String msg = t.toString();
        if(isCause) {
            msg = "caused by: " + msg;
        }
        groupStart(msg);
        log(t);
        if(t instanceof UmbrellaException) {
            UmbrellaException umbrella = (UmbrellaException) t;
            for(Throwable cause : umbrella.getCauses()) {
                logException(cause, true);
            }
        } else if (t.getCause() != null) {
            logException(t.getCause(), true);
        }
        groupEnd();
    }

    private native void groupStart(String msg) /*-{
        var groupStart = console.groupCollapsed || console.group || console.error || console.log;
        groupStart.call(console, msg);
    }-*/;

    private native void groupEnd() /*-{
        var groupEnd = console.groupEnd || function(){};
        groupEnd.call(console);
    }-*/;

    private native void log(Throwable t) /*-{
        var logError = console.error || console.log;
        var backingError = t.__gwt$backingJsError;
        logError.call(console, backingError && backingError.stack);
    }-*/;

}