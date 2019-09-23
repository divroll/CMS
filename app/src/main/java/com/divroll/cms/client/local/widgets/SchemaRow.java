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

import com.divroll.cms.client.local.ComponentBase;
import com.divroll.cms.client.local.events.Remove;
import com.divroll.cms.client.local.events.Selected;
import com.divroll.cms.client.model.Schema;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.EventListener;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/widgets/SchemaRow.html")
public class SchemaRow extends ComponentBase implements HasModel<Schema> {

    public class ClickHelper {
        private boolean isClicked;

        public boolean isClicked() {
            return isClicked;
        }

        public void setClicked(boolean clicked) {
            isClicked = clicked;
        }
    }

    Schema model;

    @Inject @DataField SpanElement name;
    @Inject @DataField ImageElement delete;
    @Inject @Remove Event<Schema> deleteSchema;

    @Inject @Selected Event<Schema> selectSchema;

    @Override
    public Schema getModel() {
        return model;
    }

    @Override
    public void setModel(Schema schema) {
        model = schema;
        name.setInnerText(model.getName());
    }

    @EventHandler("delete")
    public void delete(ClickEvent event) {
        event.preventDefault();
    }

    final ClickHelper helper = new ClickHelper();

    @Override
    protected void onAttach() {
        super.onAttach();
        //////////////////////////////////////////////////////
        // Forward to 'view' page when this widget is clicked
        //////////////////////////////////////////////////////
        Element el = this.getElement();
        com.google.gwt.user.client.Event.sinkEvents(el, com.google.gwt.user.client.Event.ONCLICK);
        com.google.gwt.user.client.Event.setEventListener(el, new EventListener() {
            @Override
            public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                Element element = Element.as(event.getCurrentEventTarget());
                if(!helper.isClicked()) {
                    transitionToContentsPage();
                } else {
                    helper.setClicked(false);
                    deleteSchema.fire(getModel());
                }
            }
        });
        com.google.gwt.user.client.Event.sinkEvents(delete, com.google.gwt.user.client.Event.ONCLICK);
        com.google.gwt.user.client.Event.setEventListener(delete, new EventListener() {
            @Override
            public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                Element element = Element.as(event.getCurrentEventTarget());
                helper.setClicked(true);
            }
        });
    }

    private void transitionToContentsPage() {
        Schema schema = getModel();
        Multimap<String,String> state = ArrayListMultimap.create();
        state.put("id", schema.getEntityId());
        contentsPage.go(state);
        selectSchema.fire(schema);
    }

}
