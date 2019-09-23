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

import com.divroll.backend.sdk.DivrollACL;
import com.divroll.cms.client.local.ComponentBase;
import com.divroll.cms.client.local.ContentWrapper;
import com.divroll.cms.client.local.events.Publish;
import com.divroll.cms.client.local.events.Remove;
import com.divroll.cms.client.local.events.Unpublish;
import com.divroll.cms.client.model.Content;
import com.divroll.cms.client.model.SchemaField;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.SimplePanel;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Date;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/widgets/ContentRow.html")
public class ContentRow extends ComponentBase implements HasModel<Content> {

    public class ClickHelper {
        private boolean isClickedDelete;
        private boolean isClickedSwitch;

        public boolean isClickedDelete() {
            return isClickedDelete;
        }

        public void setClickedDelete(boolean clickedDelete) {
            isClickedDelete = clickedDelete;
        }

        public boolean isClickedSwitch() {
            return isClickedSwitch;
        }

        public void setClickedSwitch(boolean clickedSwitch) {
            isClickedSwitch = clickedSwitch;
        }
    }

    Content model;

    @Inject @DataField SpanElement title;
    @Inject @DataField("delete-button") DivElement delete;
    @Inject @DataField("toggle-switch") SimplePanel toggleSwitch;
    @Inject @DataField SpanElement updatedAt;
    @Inject Instance<Switch> switches;
    @Inject @Remove Event<Content> deleteContent;
    @Inject @Publish Event<ContentWrapper> publishContent;
    @Inject @Unpublish Event<ContentWrapper> unpublishContent;

    final ClickHelper helper = new ClickHelper();

    @Override
    protected void onAttach() {
        super.onAttach();
        Element el = this.getElement();
        com.google.gwt.user.client.Event.sinkEvents(el, com.google.gwt.user.client.Event.ONCLICK);
        com.google.gwt.user.client.Event.setEventListener(el, new EventListener() {
            @Override
            public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                Element element = Element.as(event.getCurrentEventTarget());
                if(!helper.isClickedDelete() && !helper.isClickedSwitch()) {
                    Multimap state = ArrayListMultimap.create();
                    state.put("id", getModel().getEntityId());
                    state.put("type", getModel().getEntityType());
                    ///////////////////////////////////////////////
                    // Load Schemas to producer before transition
                    //////////////////////////////////////////////
                    schemaService.listSchema().subscribe(schemas -> {
                        contentPage.go(state);
                    }, error -> {
                        Console.error(error.getMessage());
                    });
                } else if(helper.isClickedDelete()){
                    helper.setClickedDelete(false);
                    deleteContent.fire(getModel());
                } else if(helper.isClickedSwitch()) {
                    // just let the switch handle
                }
            }
        });
        com.google.gwt.user.client.Event.sinkEvents(delete, com.google.gwt.user.client.Event.ONCLICK);
        com.google.gwt.user.client.Event.setEventListener(delete, new EventListener() {
            @Override
            public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                Element element = Element.as(event.getCurrentEventTarget());
                helper.setClickedDelete(true);
            }
        });
        com.google.gwt.user.client.Event.sinkEvents(toggleSwitch.getElement(), com.google.gwt.user.client.Event.ONCLICK);
        com.google.gwt.user.client.Event.setEventListener(toggleSwitch.getElement(), new EventListener() {
            @Override
            public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                Element element = Element.as(event.getCurrentEventTarget());
                helper.setClickedSwitch(true);
            }
        });
    }

    @Override
    public Content getModel() {
        return model;
    }

    @Override
    public void setModel(Content contentModel) {
        model = contentModel;

        if(contentModel.getSchema() == null) {
            throw new IllegalArgumentException("Content model should have Schema");
        }

        Switch switchSwitch = Switch.wrap(createSwitch(model));
        switchSwitch.setModel(contentModel);
        toggleSwitch.setWidget(switchSwitch);
        String updatedAt = contentModel.getDateUpdated();
//        this.updatedAt.setInnerText(updatedAt);

        try {
//            TimeAgo time = new TimeAgo();
//            String sTime = time.timeAgo(Date.parse(updatedAt));
//            this.updatedAt.setInnerText(sTime);
        } catch (Exception e) {
            this.updatedAt.setInnerText(updatedAt);
            Console.error(e.getMessage());
        }
        if(model.getSchema() != null && model.getSchema().getFields() != null && !model.getSchema().getFields().isEmpty()) {
            SchemaField schemaField = model.getSchema().getFields().get(0);
            title.setInnerText(model.getStringProperty(schemaField.getName()));
        }
    }

    /*

    <!--<div class="switch switch-yellow" id="switch">-->
	<!--<input type="radio" class="switch-input" name="view" value="published" id="published" checked>-->
	<!--<label for="published" class="switch-label switch-label-off">Published</label>-->
	<!--<input type="radio" class="switch-input" name="view" value="draft" id="draft">-->
	<!--<label for="draft" class="switch-label switch-label-on">Draft</label>-->
	<!--<span class="switch-selection"></span>-->


     */
    private Element createSwitch(final Content content) {

        String entityId = content.getEntityId();

        Element el = DOM.createElement("div");
        el.addClassName("switch");
        el.addClassName("switch yellow");
        el.setId("switch");

        Element input = DOM.createElement("input");
        input.setAttribute("type", "radio");
        input.setAttribute("class", "switch-input");
        input.setAttribute("name", "view");
        input.setAttribute("value", "published" + entityId);
        input.setAttribute("id", "published" + entityId);

        el.appendChild(input);

        Element label = DOM.createElement("label");
        label.setAttribute("for", "published" + entityId);
        label.setAttribute("class", "switch-label switch-label-on");
        label.setInnerText("Published");

        el.appendChild(label);

        Element input2 = DOM.createElement("input");
        input2.setAttribute("type", "radio");
        input2.setAttribute("class", "switch-input");
        input2.setAttribute("name", "view");
        input2.setAttribute("value", "draft" + entityId);
        input2.setAttribute("id", "draft" + entityId);

        el.appendChild(input2);

        Element label2 = DOM.createElement("label");
        label2.setAttribute("for", "draft" + entityId);
        label2.setAttribute("class", "switch-label switch-label-off");
        label2.setInnerText("Draft");

        el.appendChild(label2);

        com.google.gwt.user.client.Event.sinkEvents(input, com.google.gwt.user.client.Event.ONCLICK);
        com.google.gwt.user.client.Event.setEventListener(input, new EventListener() {
            @Override
            public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                Element element = Element.as(event.getCurrentEventTarget());
                ContentWrapper wrapper = new ContentWrapper(content);
                wrapper.setPublish(true); // TODO: Added because other event is also fired, bug?
                publishContent.fire(wrapper);
                label.toggleClassName("switch-input-checked");
                label2.toggleClassName("switch-input-checked");
            }
        });
        com.google.gwt.user.client.Event.sinkEvents(input2, com.google.gwt.user.client.Event.ONCLICK);
        com.google.gwt.user.client.Event.setEventListener(input2, new EventListener() {
            @Override
            public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                Element element = Element.as(event.getCurrentEventTarget());
                ContentWrapper wrapper = new ContentWrapper(content);
                wrapper.setPublish(false);
                unpublishContent.fire(wrapper);
                label.toggleClassName("switch-input-checked");
                label2.toggleClassName("switch-input-checked");
            }
        });

        DivrollACL acl = content.getAcl();
        if(acl != null) {
            if(acl.getPublicRead() != null && acl.getPublicRead()) {
                input.setAttribute("checked", "true");
                input2.removeAttribute("checked");
                label.toggleClassName("switch-input-checked");
            } else {
                input2.setAttribute("checked", "true");
                input.removeAttribute("checked");
                label2.toggleClassName("switch-input-checked");
            }
        }

        return el;
    }

}
