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
package com.divroll.cms.client.local.widgets;

import com.divroll.cms.client.services.LocalStorage;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.divroll.cms.client.local.ComponentBase;
import com.divroll.cms.client.model.User;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.inject.Inject;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/widgets/Navbar.html")
public class Navbar extends ComponentBase implements HasModel<User> {

    User model;

    @Inject @DataField("home") SpanElement home;
    @Inject @DataField SpanElement logout;

    @Override
    public User getModel() {
        return model;
    }

    @Override
    public void setModel(User user) {
        model = user;
        logout.setInnerText("logout " + model.getUsername());
    }

    @EventHandler("home")
    public void home(ClickEvent event) {
        event.preventDefault();
        Multimap state = ArrayListMultimap.create();
        contentTypesPage.go(state);
    }

    @EventHandler("logout")
    public void logout(ClickEvent event) {
        event.preventDefault();
        LocalStorage.removeKey("user");
        Window.Location.reload();
    }

}
