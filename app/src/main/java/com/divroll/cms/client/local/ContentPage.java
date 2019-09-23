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

import com.divroll.backend.sdk.helper.Pair;
import com.divroll.cms.client.local.events.Selected;
import com.divroll.cms.client.local.producers.ContentProducer;
import com.divroll.cms.client.local.widgets.Navbar;
import com.divroll.cms.client.local.widgets.fields.*;
import com.divroll.cms.client.model.Content;
import com.divroll.cms.client.model.Schema;
import com.divroll.cms.client.model.SchemaField;
import com.divroll.cms.client.model.User;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.nav.client.local.Page;
import org.jboss.errai.ui.nav.client.local.PageShowing;
import org.jboss.errai.ui.nav.client.local.PageShown;
import org.jboss.errai.ui.nav.client.local.PageState;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.json.JSONObject;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.*;

import static com.google.gwt.query.client.GQuery.$;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/ContentPage.html")
@Page(path = "content")
public class ContentPage extends IsPage implements HasModel<Content> {

    Content model;

    @PageState String id;
    @PageState String type;
    @PageState String create;

    @Inject @DataField Navbar navbar;
    @Inject @DataField SpanElement name;
    @Inject @DataField Button save;
    @Inject @DataField Button cancel;
    @Inject @DataField FlowPanel fields;

    @Inject Instance<DateTimeField> dateTimeFields;
    @Inject Instance<LongTextField> longTextFields;
    @Inject Instance<ShortTextField> shortTextFields;
    @Inject Instance<MediaField> mediaFields;
    @Inject Instance<ReferenceField> referenceFields;

    @Inject Event<Content> contentCreated; // TODO
    @Inject @Selected Event<Schema> selectSchema;

    @PostConstruct
    public void init() {
        fields.clear();
    }

    @PageShowing
    public void showing() {
        User loggedInUser = userProducer.getUser();
        if(loggedInUser != null) {
            removeSessionTokenInURL();
            navbar.setModel(loggedInUser);
            if(create != null) {
                schemaService.listSchema().subscribe(schemas -> {
                    schemas.forEach(schema -> {
                        if(schema.getName().equals(create)) {
                            Content content = new Content(create);
                            content.setSchema(schema);
                            setModel(content);
                        }
                    });
                }, error -> Console.error(error.getMessage()));
            } else if(id != null && type != null) {
                contentService.listContents(type).subscribe(contents -> {
                    contents.forEach(content -> {
                        if(content.getEntityId().equals(id)) {
                            setModel(content);
                        }
                    });
                });
            }
        } else {
            loginPage.go();
        }
    }

    @PageShown
    public void shown() {
        if(id != null) {
            $(getElement()).find("#name-prefix").first().addClass("uk-hidden");
            save.getElement().setInnerText("Update");
        } else {
            $(getElement()).find("#name-prefix").first().removeClass("uk-hidden");
            save.getElement().setInnerText("Save");
        }
    }

    @EventHandler("cancel")
    public void cancel(ClickEvent event) {
        event.preventDefault();
        Multimap<String,String> state = ArrayListMultimap.create();
        contentsPage.go(state);
        fireSelectSchema();
    }

    @EventHandler("save")
    public void save(ClickEvent event) {
        event.preventDefault();
        int count = fields.getElement().getChildCount();

        if(model == null) {
            model = new Content(type);
        }

        for(int i=0;i<count;i++){
            Element el = Element.as(fields.getElement().getChild(i));
            String fieldType = el.getAttribute("field-type");
            if(fieldType.equals(SchemaField.TYPE.SHORT_TEXT.toString())) {
                ShortTextField widget = (ShortTextField) fields.getWidget(i);
                model.setProperty(widget.getModel().getName(), widget.getModel().getValue());
            } else if(fieldType.equals(SchemaField.TYPE.LONG_TEXT.toString())) {
                LongTextField widget = (LongTextField) fields.getWidget(i);
                model.setProperty(widget.getModel().getName(), widget.getModel().getValue());
            } else if(fieldType.equals(SchemaField.TYPE.DATE.toString())) {
                // TODO ISO8601 date string format
                DateTimeField widget = (DateTimeField) fields.getWidget(i);
                String dateTimeString = widget.getModel().getValue();
                //2017-05-01 12:00
                TimeZone timeZone = TimeZone.createTimeZone(new Date().getTimezoneOffset());
                String isoTimeZone = timeZone.getISOTimeZoneString(new Date());

                dateTimeString = dateTimeString.replace(" ", "T");
                dateTimeString = dateTimeString + ":00.000";
                dateTimeString = dateTimeString + isoTimeZone;

                JSONObject date = new JSONObject();
                date.put("type", "Date");
                date.put("iso", dateTimeString);

                model.setProperty(widget.getModel().getName(), date);

            } else if(fieldType.equals(SchemaField.TYPE.MEDIA.toString())) {

                MediaField widget = (MediaField) fields.getWidget(i);
                String fileJSONString = widget.getModel() != null ? widget.getModel().first.getValue() : null;
                try {
                    JSONObject fileObj = new JSONObject(fileJSONString);
                    model.setProperty(widget.getModel().first.getName(), fileObj);
                } catch (Exception e) {
                    Console.error(e.getMessage());
                }

            } else if(fieldType.equals(SchemaField.TYPE.REFERENCE.toString())) {
                ReferenceField widget = (ReferenceField) fields.getWidget(i);
                if(widget != null) {
                    String name = widget.getModel().getName();
                    String schemaId = widget.getModel().getReference();
                    //String value = widget.getValue(); // TODO we can't use widget.getModel().getName() here
                    String entityId = widget.getValue().getEntityId();
                    String entityType = widget.getValue().getEntityType();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("entityId", entityId);
                    jsonObject.put("entityType", entityType);
                    model.setProperty(name, jsonObject);
                } else {
                    Console.warn("Reference widget model is null");
                }

            }
        }

        if(model.getEntityId() != null) {
            contentService.updateContent(model).subscribe(content -> {
                Multimap<String,String> state = ArrayListMultimap.create();
                contentsPage.go(state);
                fireSelectSchema();
            }, error -> {
                Console.error(error.getMessage());
                info(error.getMessage());
            });
        } else {
            contentService.createContent(model).subscribe(content -> {
                Multimap<String,String> state = ArrayListMultimap.create();
                contentsPage.go(state);
                contentCreated.fire(model);
                fireSelectSchema();
            }, error ->{
                Console.error(error.getMessage());
            });
        }
    }

    @Override
    public Content getModel() {
        return this.model;
    }

    @Override
    public void setModel(Content content) {
        this.model = content;
        render();
    }

    @Inject
    ContentProducer contentProducer;

    private void renderFields() {
        int count = fields.getElement().getChildCount();
        int index = 0;
        List<Widget> updatedWidget = new LinkedList<>();
        for(int i=0;i<count;i++){
            Element el = Element.as(fields.getElement().getChild(i));
            String fieldType = el.getAttribute("field-type");
            if(fieldType.equals(SchemaField.TYPE.SHORT_TEXT.toString())) {
                ShortTextField widget = (ShortTextField) fields.getWidget(i);
                SchemaField model = widget.getModel();
                model.setValue(getValue(model.getName()));
                widget.setModel(model);
                updatedWidget.add(widget);
            } else if(fieldType.equals(SchemaField.TYPE.LONG_TEXT.toString())) {
                LongTextField widget = (LongTextField) fields.getWidget(i);
                SchemaField model = widget.getModel();
                model.setValue(getValue(model.getName()));
                widget.setModel(model);
                updatedWidget.add(widget);
            } else if(fieldType.equals(SchemaField.TYPE.DATE.toString())) {
                // TODO ISO8601 date string format
                DateTimeField widget = (DateTimeField) fields.getWidget(i);
                SchemaField model = widget.getModel();
                model.setValue(getValue(model.getName()));
                widget.setModel(model); // TODO:
                updatedWidget.add(widget);
            } else if(fieldType.equals(SchemaField.TYPE.MEDIA.toString())) {
                MediaField widget = (MediaField) fields.getWidget(i);
                Pair<SchemaField, Content> pair = widget.getModel();
                SchemaField model = pair.first;
                //String value = getValue(model.getName());
                Content content = getModel();
                if(content.getProperty(model.getName()) != null) {
                    Map<String,Object> mapValue = (Map<String, Object>) content.getProperty(model.getName());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("type", "File");
                    jsonObject.put("url", mapValue.get("url"));
                    model.setValue(jsonObject.toString());
                    widget.setModel(new Pair<>(model, pair.second));
                }
                updatedWidget.add(widget);
            } else if(fieldType.equals(SchemaField.TYPE.REFERENCE.toString())) {
                ReferenceField widget = (ReferenceField) fields.getWidget(i);
                SchemaField model = widget.getModel();
                model.setValue(getValue(model.getName()));
                widget.setModel(model);
                Map<String,Object> property = (Map<String, Object>) this.model.getProperty(model.getName());
                Content selected = new Content((String) property.get("entityType"));
                selected.setEntityId((String) property.get("entityId"));
                // at this point  contentList is empty
                widget.setValue(selected);
                updatedWidget.add(widget);

            }
            index++;
        }
        fields.clear();
        for(Widget w : updatedWidget) {
            fields.add(w);
        }
    }

    List<Schema> tempSchemas = new ArrayList<>();

    private void render() {
        tempSchemas.clear();
        //////////////////////////////
        // Load schemas
        //////////////////////////////
        Content contentModel = this.model;
        schemaService.listSchema().subscribe(schemas -> {
            schemas.forEach(schema -> {
                tempSchemas.add(schema);
                // TODO: Double-check
                String n = schema.getName();
                if(n.equals(getModel().getEntityType())) {
                    name.setInnerText(schema.getName());
                    List<SchemaField> sfields = schema.getFields();
                    for(SchemaField sf : sfields) {
                        sf.setParent(schema);
                        Widget fieldWidget = createField(sf);
                        fields.add(fieldWidget);
                        if(fieldWidget instanceof ReferenceField) {
                            Object referenceField = contentModel.getProperty(sf.getName());
                            if(referenceField != null) {
                                Map<String,Object> referenceFieldMap = (Map<String, Object>) referenceField;
//                                Content content = new Content((String) referenceFieldMap.get("entityType"));
//                                content.setEntityId((String) referenceFieldMap.get("entityId"));
                                String selectedContentId = (String) referenceFieldMap.get("entityId");
                                renderReferences((ReferenceField) fieldWidget, selectedContentId);
                            } else {
                                renderReferences((ReferenceField) fieldWidget, null);
                            }
                        }
                    }
                    renderFields();
                }
            });
        }, error -> {
            Console.error(error.getMessage());
        });
    }

    private Composite createField(SchemaField field) {
        String name = field.getName();
        String type = field.getType();

        if(type.equals(SchemaField.TYPE.SHORT_TEXT.toString())) {
            ShortTextField instance = shortTextFields.get();
            instance.setModel(field);
            return instance;
        } else if(type.equals(SchemaField.TYPE.LONG_TEXT.toString())) {
            LongTextField instance = longTextFields.get();
            instance.setModel(field);
            return instance;
        } else if(type.equals(SchemaField.TYPE.DATE.toString())) {
            DateTimeField instance = dateTimeFields.get();
            instance.setModel(field);
            return instance;
        } else if(type.equals(SchemaField.TYPE.MEDIA.toString())) {
            MediaField instance = mediaFields.get();
            instance.setModel(new Pair<>(field, model));
            return instance;
        } else if(type.equals(SchemaField.TYPE.REFERENCE.toString())) {
            ReferenceField instance = referenceFields.get();
            instance.setModel(field);
            return instance;
        }

        return null;
    }

    private String getValue(String fieldName) {
        Content content = getModel();
        return String.valueOf(content.getProperty(fieldName));
    }

    private void fireSelectSchema() {
        selectSchema.fire(getModel().getSchema());
    }

    public void renderReferences(ReferenceField field, String selectedId) {
        SchemaField sf = field.getModel();
        if(sf != null) {
            String referenceId = sf.getReference();
            String referenceType = sf.getReferenceType();
            contentService.listContents(referenceType).subscribe(contents -> {
                contentProducer.setContentList(contents);
                final Content[] selected = {null};
                contents.forEach(content -> {
                    if(selectedId != null && selectedId.equals(content.getEntityId())) {
                        selected[0] = content;
                    }
                });

                if(selected[0] != null) {
                    field.setValues(selected[0], contents);
                } else {
                    field.setValues(contents);
                }
            }, error -> Console.error(error.getMessage()));
        }
    }



}
