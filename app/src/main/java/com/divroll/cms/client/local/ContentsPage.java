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

import com.divroll.cms.client.local.events.*;
import com.divroll.cms.client.local.resource.UIKit;
import com.divroll.cms.client.local.widgets.*;
import com.divroll.cms.client.local.widgets.modal.DeleteModal;
import com.divroll.cms.client.model.Content;
import com.divroll.cms.client.model.Schema;
import com.divroll.cms.client.model.SchemaField;
import com.divroll.cms.client.model.User;
import com.divroll.cms.client.validation.exceptions.InvalidNameException;
import com.divroll.cms.client.validation.exceptions.NullContentIdException;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.nav.client.local.PageState;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/ContentsPage.html")
@Page(path = "contents")
public class ContentsPage extends ComponentBase implements HasModel<Schema> {

    Schema model;

    @PageState String id;

    @Inject @DataField Navbar navbar;
    @Inject @DataField SpanElement name;
    @Inject @DataField Button create;
    @Inject @DataField Button add;
    @Inject @DataField Button update;
    @Inject @DataField FlowPanel rows;

    @Inject @DataField("name-text") TextBox nameText;
    @Inject @DataField("description-text") TextBox descriptionText;
    @Inject @DataField("menu-customize") Anchor menuCustomize;
    @Inject @DataField("delete-modal") DeleteModal deleteModal;
    @Inject @DataField("field-rows") FlowPanel rowsField;

    @Inject @DataField("menu-file") Anchor contentsTab;

    @Inject Instance<FieldRow> fieldRows;
    @Inject Instance<ContentRow> contentRows;

    @Inject @Removed Event<SchemaField> removedSchemaField;

    @Inject @Selected Event<Schema> selectSchema;

    boolean isTyping = false;

    @PostConstruct
    public void init() {
        rowsField.clear();
        final Timer timer = new Timer() {
            @Override
            public void run() {
                isTyping = false;
                Command command = commandProducer.getCommand();
                if(command != null)
                    command.execute();
            }
        };
        Element el = this.getElement();
        com.google.gwt.user.client.Event.sinkEvents(el, com.google.gwt.user.client.Event.ONMOUSEDOWN);
        com.google.gwt.user.client.Event.setEventListener(el, new EventListener() {
            @Override
            public void onBrowserEvent(com.google.gwt.user.client.Event event) {
                Element element = Element.as(event.getCurrentEventTarget());
                timer.schedule(3000);
                isTyping = true;
            }
        });
    }

    @PageShowing
    public void showing() {
        UIKit.inject();
        User loggedInUser = userProducer.getUser();
        if(loggedInUser != null) {
            removeSessionTokenInURL();
            navbar.setModel(loggedInUser);
        } else {
            loginPage.go();
        }
    }

    @PageShown
    public void shown() {
        setup();
    }

    @Override
    public Schema getModel() {
        if(model != null) {
            model.setName(nameText.getText());
            model.setDescription(descriptionText.getText());
        }
        return model;
    }

    @Override
    public void setModel(Schema schema) {
        model = schema;
        renderContents(schema);
    }

    @EventHandler("menu-file")
    public void contentsMenu(ClickEvent event) {
        renderContents(getModel());
    }

    @EventHandler("menu-customize")
    public void customizeMenu(ClickEvent event) {
        renderFields();
    }

    @EventHandler("update")
    public void update(ClickEvent event) {
        event.preventDefault();
        Schema model = getModel();
        onUpdate(model);
    }

    @EventHandler("add")
    public void addField(ClickEvent event) {
        event.preventDefault();
        FieldRow fieldRow = fieldRows.get();
        fieldRow.setModel(null);
        fieldRow.setParentModel(getModel());
        rowsField.add(fieldRow);
    }

    @EventHandler("create")
    public void createContent(ClickEvent event) {
        event.preventDefault();
        Multimap state = ArrayListMultimap.create();
        state.put("create", getModel().getName());
        ///////////////////////////////////////////////
        // Load Schemas to producer before transition
        //////////////////////////////////////////////
        schemaService.listSchema().subscribe(schemas -> {
            contentPage.go(state);
        }, error -> {
            Console.error(error.getMessage());
        });
    }

    private void renderContents(Schema schema) {
        if(schema == null) {
            Console.error("Cannot render null schema");
            return;
        }
        Spinner.spin(contentsTab);
        name.setInnerText(schema.getName());
        nameText.setText(schema.getName());
        descriptionText.setText(schema.getDescription());
        renderFields();
        //updateRows(getModel().getFields());
        rows.clear();
        contentService.listContents(schema.getName()).subscribe(contents -> {
                    for(Content content : contents) {
                        content.setSchema(schema);
                        ContentRow contentRow = contentRows.get();
                        contentRow.setModel(content);
                        rows.add(contentRow);
                    }
                    Spinner.unspin(contentsTab);
                },
                error -> {
                    Console.error(error.getMessage());
                    Spinner.unspin(contentsTab);
        });
    }

    private void renderFields() {
        Iterator<SchemaField> it = getModel().getFields().iterator();
        while(it.hasNext()) {
            SchemaField value = it.next();
            addField(value);
        }
    }

    private void loadContents() {
        rows.clear();
        rowsField.clear();
        renderContents(getModel());
    }

    ////////////////////////////////////
    // Observers
    ////////////////////////////////////

    public void onSelected(@Observes @Selected Schema schema) {
        setModel(schema);
    }

    public void onUpdate(@Observes @Updated Schema schema) {
        schemaService.updateSchema(schema).subscribe(updated -> {
            setModel(schema);
        }, error -> {
            Console.error(error.getMessage());
        });
    }

    public void onUpField(@Observes @Up SchemaFieldWrapper wrapper) {
        if(wrapper.getOp().equals("UP")) {
            Iterator<Widget> it = rowsField.iterator();
            int idx = 0;
            int selectedIdx = -1;
            int max = rowsField.getWidgetCount();
            List<SchemaField> fieldRows = new LinkedList<>();
            while (it.hasNext()) {
                Widget widget = it.next();
                FieldRow fieldRow = (FieldRow) widget;
                SchemaField schemaField = fieldRow.getModel();
                if(schemaField.getName().equals(wrapper.getWrapped().getName())) {
                    selectedIdx = idx;
                }
                idx++;
                fieldRows.add(schemaField);
            }
            if(selectedIdx == 0) {
                return;
            }
            moveItem(selectedIdx, selectedIdx - 1, fieldRows);
            rowsField.clear();
            for(SchemaField sf : fieldRows) {
                String name = sf.getName();
                FieldRow row = this.fieldRows.get();
                row.setModel(sf);
                rowsField.add(row);
            }
            Schema model = getModel();
            model.setFields(fieldRows);
            onUpdate(model);
        }
    }

    public void onDownField(@Observes @Down SchemaFieldWrapper wrapper) {
        if(wrapper.getOp().equals("DOWN")) {
            Iterator<Widget> it = rowsField.iterator();
            int idx = 0;
            int selectedIdx = -1;
            int max = rowsField.getWidgetCount();
            List<SchemaField> fieldRows = new LinkedList<>();
            while (it.hasNext()) {
                Widget widget = it.next();
                FieldRow fieldRow = (FieldRow) widget;
                SchemaField schemaField = fieldRow.getModel();
                if(schemaField.getName().equals(wrapper.getWrapped().getName())) {
                    selectedIdx = idx;
                }
                fieldRows.add(schemaField);
                idx++;
            }
            if(selectedIdx == (max -1)) {
                return;
            }
            moveItem(selectedIdx, selectedIdx + 1, fieldRows);
            rowsField.clear();
            for(SchemaField sf : fieldRows) {
                String name = sf.getName();
                FieldRow row = this.fieldRows.get();
                row.setModel(sf);
                rowsField.add(row);
            }
            Schema model = getModel();
            model.setFields(fieldRows);
            onUpdate(model);
        }
    }

        public void onDeleteField(@Observes @Remove final SchemaField schemaField) {
        deleteModal.setName(schemaField.getName());
        Schema model = getModel();
        Command command = new Command() {
            @Override
            public void execute() {
                final int[] removeIndex = {-1};
                final int[] index = {0};
                rowsField.forEach(widget -> {
                    FieldRow fieldRow = (FieldRow) widget;
                    SchemaField sf = fieldRow.getModel();
                    if(schemaField.getName().equals(sf.getName())) {
                        removeIndex[0] = index[0];
                    }
                    index[0]++;
                });
//                for(SchemaField sf : model.getFields()) {
//                    if(schemaField.getName().equals(sf.getName())) {
//                        removeIndex = index;
//                    }
//                    index++;
//                }
                if(removeIndex[0] != -1) {
                    model.getFields().remove(removeIndex[0]);
                    rowsField.remove(removeIndex[0]);
                }
                onUpdate(model);
            }
        };
        deleteModal.setModel(command);
        deleteModal.show();
    }

    public void onCreatedContent(@Observes @Created Content content) {
        ContentRow row = contentRows.get();
        row.setModel(content);
        rows.add(row);
    }

    public void onPublishContent(@Observes @Publish ContentWrapper wrapper) {
        if(wrapper.getPublish()) {
            contentService.publishContent(wrapper.getWrapped().getEntityId(), wrapper.getWrapped().getEntityType()).subscribe(updated -> {
                info(messages.successTitle());
            }, error -> {
                info(error.getMessage());
            });
        }
    }

    public void onUnpublishContent(@Observes @Unpublish ContentWrapper wrapper) {
        if(!wrapper.getPublish()) {
            contentService.unpublishContent(wrapper.getWrapped().getEntityId(), wrapper.getWrapped().getEntityType()).subscribe(updated -> {
                info(messages.successTitle());
            }, error -> {
                info(error.getMessage());
            });
        }
    }

    public void onDeleteContent(@Observes @Remove Content content) {
        deleteModal.setName(content.getStringProperty("title"));
        deleteModal.setModel(new Command() {
            @Override
            public void execute() {
                content.delete().subscribe(deleted -> {
                    loadContents();
                }, error -> {
                    Console.error(error.getMessage());
                });
            }
        });
        deleteModal.show();
    }

    private void updateRows(List<SchemaField> fields) {
        Iterator<Widget> it = rowsField.iterator();
        while(it.hasNext()) {
            Widget row = it.next();
            FieldRow fieldRow = (FieldRow) row;
            SchemaField model = fieldRow.getModel();

            boolean isKeep = false;
            for(SchemaField sf : fields) {
                if(sf.getName().equals(model.getName())) {
                    isKeep = true;
                }
            }
            if(!isKeep) {
                fieldRow.getElement().removeFromParent();
            }
        }
    }

    private int getRowIndexOf(SchemaField field) {
        Iterator<Widget> it = rowsField.iterator();
        boolean isContain = false;
        int index = 0;
        int rowIndex = -1;
        while(it.hasNext()) {
            Widget row = it.next();
            FieldRow fieldRow = (FieldRow) row;
            SchemaField model = fieldRow.getModel();
            if(model.getName().equals(field.getName())) {
                isContain = true;
                rowIndex = index;
            }
            index++;
        }
        return rowIndex;
    }

    private void addField(SchemaField field) {
        Iterator<Widget> it = rowsField.iterator();
        boolean isContain = false;
        while(it.hasNext()) {
            Widget row = it.next();
            FieldRow fieldRow = (FieldRow) row;
            SchemaField model = fieldRow.getModel();
            if(model.getName().equals(field.getName())) {
                isContain = true;
            }
        }
        if(!isContain) {
            FieldRow fieldRow = fieldRows.get();
            fieldRow.setModel(field);
            fieldRow.setParentModel(getModel());
            rowsField.add(fieldRow);
        } else {
            // do nothing?
        }
    }

    public static <T> void moveItem(int sourceIndex, int targetIndex, List<T> list) {
        if (sourceIndex <= targetIndex) {
            Collections.rotate(list.subList(sourceIndex, targetIndex + 1), -1);
        } else {
            Collections.rotate(list.subList(targetIndex, sourceIndex + 1), 1);
        }
    }

    // TODO: Convert this to GWT
    public static native void setup()/*-{
        $wnd.$("#menu-file").click(function () {
            $wnd.$('#settings').addClass('uk-hidden');
            $wnd.$('#file').removeClass('uk-hidden');
            $wnd.$('#customize').addClass('uk-hidden');
        });

        $wnd.$("#menu-settings").click(function () {
            $wnd.$('#file').addClass('uk-hidden');
            $wnd.$('#customize').addClass('uk-hidden');
            $wnd.$('#settings').removeClass('uk-hidden');
        });

        $wnd.$("#menu-customize").click(function () {
            $wnd.$('#file').addClass('uk-hidden');
            $wnd.$('#customize').removeClass('uk-hidden');
            $wnd.$('#settings').addClass('uk-hidden');
        });

        $wnd.$("#menu-delete").click(function () {
            $wnd.$wnd.$('#li-contents').addClass('uk-active');
            $wnd.$('#li-delete').removeClass('uk-active');
        });

        $wnd.$("#close").click(function () {
            $wnd.$('#li-contents').addClass('uk-active');
            $wnd.$('#li-delete').removeClass('uk-active');
        });

    }-*/;

}
