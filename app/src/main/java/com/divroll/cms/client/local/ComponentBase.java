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

import com.divroll.cms.client.local.producers.CommandProducer;
import com.divroll.cms.client.local.producers.ConfigProducer;
import com.divroll.cms.client.local.producers.UserProducer;
import com.divroll.cms.client.local.resource.Messages;
import com.divroll.cms.client.local.resource.UIKit;
import com.divroll.cms.client.services.ContentService;
import com.divroll.cms.client.services.LogService;
import com.divroll.cms.client.services.SchemaService;
import com.divroll.cms.client.services.UserService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.nav.client.local.TransitionTo;

import javax.inject.Inject;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
public class ComponentBase extends Composite {


    @Inject protected TransitionTo<WelcomePage> welcomePage;
    @Inject protected TransitionTo<ContentTypesPage> contentTypesPage;
    @Inject protected TransitionTo<ContentTypePage> contentTypePage;
    @Inject protected TransitionTo<ContentsPage> contentsPage;
    @Inject protected TransitionTo<ContentPage> contentPage;
    @Inject protected TransitionTo<LoginPage> loginPage;

    @Inject protected UserProducer userProducer;
    @Inject protected ConfigProducer configProducer;
    @Inject protected CommandProducer commandProducer;

    @Inject protected SchemaService schemaService;
    @Inject protected ContentService contentService;
    @Inject protected UserService userService;
    @Inject protected LogService logService;

    Messages messages = GWT.create(Messages.class);

    protected void info(String message) {
        UIKit.notif(message, "info", 3000L, "top-right");
    }

    protected void redirect() {
        String redirect = Window.Location.getProtocol() + "//" + Window.Location.getHost() + Window.Location.getPath();
    }

    protected void removeSessionTokenInURL() {
        String newURL = Window.Location.createUrlBuilder().removeParameter("authToken").buildString();
        updateURLWithoutReloading(newURL);
    }

    protected static native void updateURLWithoutReloading(String newUrl) /*-{
        $wnd.history.pushState(newUrl, "", newUrl);
    }-*/;

    public String stackTraceToString(Throwable exception) {
        String string = "";
        for (StackTraceElement element : exception.getStackTrace()) {
            string += element + "\n";
        }
        return string;
    }
}
