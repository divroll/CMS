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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.divroll.cms.client.local.widgets.Navbar;
import com.divroll.cms.client.local.widgets.Spinner;
import com.divroll.cms.client.model.Schema;
import com.divroll.cms.client.model.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageState;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.inject.Inject;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/ContentTypePage.html")
@Page(path = "type")
public class ContentTypePage extends ComponentBase {

    @Inject @DataField Navbar navbar;
    @Inject @DataField TextBox name;
    @Inject @DataField TextBox description;
    @Inject @DataField Button save;
    @Inject @DataField Button cancel;

    @EventHandler("save")
    public void save(ClickEvent event) {
        event.preventDefault();
        String schemaName = name.getText();
        String schemaDesc = description.getText();
        Spinner.spin(save);
        schemaService.createSchema(schemaName, schemaDesc).subscribe(schema -> {
            Spinner.unspin(save);
            if(schema != null) {
                Multimap<String,String> state = ArrayListMultimap.create();
                contentTypesPage.go(state);
            } else {
                info("Error");
            }
        }, error -> {
            Spinner.unspin(save);
            info(error.getMessage());
        });
    }

    @EventHandler("cancel")
    public void cancel(ClickEvent event) {
        event.preventDefault();
        History.back();
    }

    @PageShowing
    public void showing() {
        User loggedInUser = userProducer.getUser();
        if(loggedInUser != null) {
            removeSessionTokenInURL();
            navbar.setModel(loggedInUser);
        } else {
            loginPage.go();
        }
    }

}
