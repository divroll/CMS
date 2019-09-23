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

import com.divroll.backend.sdk.Divroll;
import com.google.gwt.user.client.Timer;
import com.divroll.cms.client.local.resource.UIKit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ioc.client.api.AfterInitialization;
import org.jboss.errai.ioc.client.api.EntryPoint;
import org.jboss.errai.ui.nav.client.local.Navigation;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Application bootstrap
 * 
 * @author kerbymart
 * @version 0.0.1
 * @since 0.0.1
 */
@ApplicationScoped
@EntryPoint
@Templated("/com/divroll/cms/client/local/resource/templates/Bootstrap.html#main")
public class Bootstrap extends ComponentBase {

    @Inject Navigation navigation;
    @Inject @DataField SimplePanel content;

    @AfterInitialization
    public void afterInit() {
    }
    
    @PostConstruct
    public void buildUI() {
        UIKit.inject();
        content.add(navigation.getContentPanel());
        Divroll.initialize(getDivrollServerUrlFromJs(), getDivrollAppIdFromJs(), getDivrollApiKeyFromJs(), null);
        RootPanel.get("rootPanel").add(this);
        Timer timer = new Timer() {
            @Override
            public void run() {
                Console.log("Open connections: " + getAllOpenXHR());
                if(getAllOpenXHR() > 0) {
                    UIKit.notif("Loading...", "INFO", 3000L, "top-right");
                }
            }
        };
        timer.scheduleRepeating(1000);
    }

    public static native int getAllOpenXHR()/*-{
        return $wnd.openHTTPs;
    }-*/;

    private static native String getDivrollServerUrlFromJs() /*-{
        return $wnd.divrollServerUrl;
    }-*/;

    private static native String getDivrollAppIdFromJs() /*-{
        return $wnd.divrollAppId;
    }-*/;

    private static native String getDivrollApiKeyFromJs() /*-{
        return $wnd.divrollApiKey;
    }-*/;

}
