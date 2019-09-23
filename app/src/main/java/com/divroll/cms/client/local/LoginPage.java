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

import com.divroll.backend.sdk.DivrollUser;
import com.divroll.cms.client.local.widgets.Spinner;
import com.divroll.cms.client.model.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.nav.client.local.DefaultPage;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/LoginPage.html")
@Page(path = "login", role = DefaultPage.class)
public class LoginPage extends IsPage {

    @Inject
    @DataField
    TextBox username;

    @Inject
    @DataField
    TextBox password;

    @Inject
    @DataField("login-button")
    Button loginButton;

    @PostConstruct
    public void build() {
        Spinner.unspin(loginButton);
    }

    @PageShowing
    public void showing() {
        User loggedInUser = userProducer.getUser();
        if(loggedInUser != null) {
            welcomePage.go();
        }
    }

    @EventHandler("login-button")
    public void login(ClickEvent event) {
        event.preventDefault();
        Spinner.spin(loginButton);
        DivrollUser divrollUser = new DivrollUser();
        divrollUser.login(username.getText(), password.getText(), false).subscribe(loggedInUser -> {
            User user = new User();
            user.setEntityId(loggedInUser.getEntityId());
            user.setAuthToken(loggedInUser.getAuthToken());
            user.setUsername(loggedInUser.getUsername());
            userProducer.setUser(user);
            welcomePage.go();
        }, error -> {
            Console.error(error.getMessage());
        });

    }

}
