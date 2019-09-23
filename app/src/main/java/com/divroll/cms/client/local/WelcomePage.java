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

import com.divroll.cms.client.local.widgets.Navbar;
import com.divroll.cms.client.model.Schema;
import com.divroll.cms.client.model.User;
import com.divroll.cms.client.services.LocalStorage;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import io.reactivex.Single;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.json.JSONObject;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.LinkedList;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Dependent
@Templated("/com/divroll/cms/client/local/resource/templates/WelcomePage.html")
@Page(path = "home")
public class WelcomePage extends ComponentBase {

    @Inject @DataField Navbar navbar;
    @Inject @DataField SpanElement logout;

    @Inject @DataField("create-button") Button create;

    @EventHandler("create-button")
    public void create(ClickEvent event) {
        event.preventDefault();
        contentTypesPage.go();
    }

    @EventHandler("logout")
    public void logout(ClickEvent event) {
        event.preventDefault();
        redirect();
    }

    @PageShowing
    public void showing() {
        User loggedInUser = userProducer.getUser();
        if(loggedInUser != null) {
            removeSessionTokenInURL();
            navbar.setModel(loggedInUser);
            schemaService.listSchema().subscribe(schemas -> {
                if(schemas != null && !schemas.isEmpty()) {
                    contentTypesPage.go();
                }
            }, error -> Console.error(error.getMessage()));
        } else {
            String userJSONString = LocalStorage.getKey("user");
            JSONObject userJSON = new JSONObject(userJSONString);

            String authToken = userJSON.getString("authToken");
            if(authToken == null) {
                loginPage.go();
            } else {
                userService.getUser(authToken).flatMap(user -> {
                    removeSessionTokenInURL();
                    navbar.setModel(user);
                    return schemaService.listSchema();
                }).subscribe(schemas -> {
                    if(schemas != null && !schemas.isEmpty()) {
                        contentTypesPage.go();
                    }
                }, error -> {
                    Console.error(error.getMessage());
                });
            }

        }

    }



}
