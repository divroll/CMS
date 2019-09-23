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
import com.google.gwt.dom.client.*;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.*;
import com.divroll.cms.client.local.events.Remove;
import com.divroll.cms.client.local.widgets.modal.SetupModal;
import com.divroll.cms.client.model.Schema;
import com.divroll.cms.client.model.User;
import com.divroll.cms.client.local.resource.UIKit;
import com.divroll.cms.client.local.widgets.Navbar;
import com.divroll.cms.client.local.widgets.SchemaRow;
import com.divroll.cms.client.local.widgets.modal.DeleteModal;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.nav.client.local.PageState;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.List;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/ContentTypesPage.html")
@Page(path = "types")
public class ContentTypesPage extends ComponentBase {

    @Inject @DataField Navbar navbar;
    @Inject @DataField FlowPanel types;

    @Inject @DataField("delete-modal") DeleteModal deleteModal;
    @Inject @DataField("setup-modal") SetupModal setupModal;
    @Inject @DataField("create-button") Button create;
    @Inject @DataField("setup-button") Button setup;
    @Inject Instance<SchemaRow>schemaRows;

    @EventHandler("create-button")
    public void create(ClickEvent event) {
        event.preventDefault();
        Multimap<String,String> state = ArrayListMultimap.create();
        contentTypePage.go(state);
    }

    @EventHandler("setup-button")
    public void setup(ClickEvent event) {
        event.preventDefault();
        setupModal.setModel(userProducer.getUser());
        setupModal.show();
    }

    @PostConstruct
    public void init() {

    }

    @PageShowing
    public void showing() {
        UIKit.inject();
        User loggedInUser = userProducer.getUser();
        if(loggedInUser != null) {
            removeSessionTokenInURL();
            navbar.setModel(loggedInUser);
            schemaService.listSchema().subscribe(schemas -> {
                if(schemas != null) {
                    for(Schema schema : schemas) {
                        SchemaRow schemaRow = schemaRows.get();
                        schemaRow.setModel(schema);
                        types.add(schemaRow);
                    }
                }
            }, error -> Console.error(error.getMessage()));
        } else {
            loginPage.go();
        }
    }

    @PageShown
    public void shown() {
    }

    private void notif(String message) {
        UIKit.notif(message, "info", 3000L, "top-right");
    }

    public void onDelete(@Observes @Remove final Schema schema) {
        deleteModal.setName(schema.getName());
        Command command = new Command() {
            @Override
            public void execute() {
                schemaService.removeShema(schema.getEntityId()).flatMap(removed -> {
                    notif(messages.successTitle());
                    return schemaService.listSchema();
                }).subscribe(schemas -> {
                    notif(messages.successTitle());
                    if(schemas != null) {
                        for(Schema schema : schemas) {
                            SchemaRow schemaRow = schemaRows.get();
                            schemaRow.setModel(schema);
                            types.add(schemaRow);
                        }
                    }
                }, error -> {
                    notif("Unable to delete");
                    Console.error(error.getMessage());
                });
            }
        };
        deleteModal.setModel(command);
        deleteModal.show();
    }

}
