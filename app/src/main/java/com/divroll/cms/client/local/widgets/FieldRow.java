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

import com.divroll.backend.sdk.Divroll;
import com.divroll.cms.client.local.ComponentBase;
import com.divroll.cms.client.local.events.*;
import com.divroll.cms.client.model.Schema;
import com.divroll.cms.client.model.SchemaField;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.databinding.client.api.DataBinder;
import org.jboss.errai.databinding.client.api.StateSync;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.*;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/widgets/FieldRow.html")
public class FieldRow extends ComponentBase implements HasModel<SchemaField> {

    @Inject
    @AutoBound
    DataBinder<SchemaField> modelBinder;

    Schema parentModel;

    @Inject @DataField  @Bound(property = "name") TextBox name;
    @Inject @DataField  ListBox type;
    @DataField
    ValueListBox ref = new ValueListBox(new Renderer<Schema>() {
        @Override
        public String render(Schema schema) {
            return schema.getName();
        }

        @Override
        public void render(Schema schema, Appendable appendable) throws IOException {
            appendable.append(render(schema));
        }
    });
    @Inject @DataField DivElement up;
    @Inject @DataField DivElement down;
    @Inject @DataField DivElement delete;

    @Inject @Added   Event<SchemaField> added;
    @Inject @Remove  Event<SchemaField> remove;
    @Inject @Up      Event<SchemaFieldWrapper> upEvent;
    @Inject @Down    Event<SchemaFieldWrapper> downEvent;
    @Inject @Updated Event<Schema> schemaUpdated;

    boolean isNew = false;

    @PostConstruct
    public void init() {
        final Timer timer = new Timer() {
            @Override
            public void run() {
                saveField(getModel());
            }
        };
        name.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                timer.schedule(3000);
            }
        });
        for(int i=0;i<type.getItemCount();i++){
            type.removeItem(i);
        }
        type.addItem("Short Text", SchemaField.TYPE.SHORT_TEXT.toString()); // 0
        type.addItem("Long Text", SchemaField.TYPE.LONG_TEXT.toString()); // 1
        type.addItem("Date/Time", SchemaField.TYPE.DATE.toString()); // 2
        type.addItem("Media", SchemaField.TYPE.MEDIA.toString()); // 3
        type.addItem("Reference", SchemaField.TYPE.REFERENCE.toString()); //4

        type.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent changeEvent) {
                if(modelBinder.getModel() != null)
                    modelBinder.getModel().setType(type.getSelectedValue());

                String modelValue = type.getSelectedValue();

                if(!modelValue.equals(SchemaField.TYPE.REFERENCE.toString())) {
                    ref.getElement().addClassName("uk-hidden");
                }

                if(modelValue.equals(SchemaField.TYPE.SHORT_TEXT.toString())) {
                    selectDropdown(modelValue, null);
                } else if(modelValue.equals(SchemaField.TYPE.LONG_TEXT.toString())) {
                    selectDropdown(modelValue, null);
                } else if(modelValue.equals(SchemaField.TYPE.DATE.toString())) {
                    selectDropdown(modelValue, null);
                } else if(modelValue.equals(SchemaField.TYPE.MEDIA.toString())) {
                    selectDropdown(modelValue, null);
                } else if(modelValue.equals(SchemaField.TYPE.REFERENCE.toString())) {
                    selectDropdown(modelValue, null);
                    ref.getElement().removeClassName("uk-hidden");
                    /////////////////////////////////////////////
                    // Load Schemas
                    /////////////////////////////////////////////
                    schemaService.listSchema().subscribe(schemas -> {
//                        for(Schema s : schemas) {
//                            ref.addItem(s.getName(), s.getEntityId());
//                        }
                        ref.setAcceptableValues(schemas);
                    }, error -> {
                        Console.error(error.getMessage());
                    });
                }
                saveField(getModel());
            }
        });
        ref.addValueChangeHandler(valueChangeEvent -> {
           saveField(getModel());
        });

    }

    public void selectDropdown(String value, String ref) {
        modelBinder.getModel().setType(value);
        modelBinder.getModel().setReference(ref);
    }

    @EventHandler("delete")
    public void delete(ClickEvent event) {
        if(!isNew) {
            remove.fire(getModel());
        } else {
            getElement().removeFromParent();
        }
    }

    @EventHandler("up")
    public void up(ClickEvent event) {
        if(!isNew) {
            SchemaFieldWrapper wrapper = new SchemaFieldWrapper(getModel());
            wrapper.setOp("UP");
            upEvent.fire(wrapper);
        }
    }

    @EventHandler("down")
    public void down(ClickEvent event) {
        if(!isNew) {
            SchemaFieldWrapper wrapper = new SchemaFieldWrapper(getModel());
            wrapper.setOp("DOWN");
            downEvent.fire(wrapper);
        }
    }

    @Override
    public SchemaField getModel() {
//        String entityId = ref.getSelectedValue();
//        String entityType = ref.getSelectedItemText();
        if(ref.getValue() != null) {
            Schema selectedSchema = (Schema) ref.getValue();
            modelBinder.getModel().setReference(selectedSchema.getEntityId());
            modelBinder.getModel().setReferenceType(selectedSchema.getName());
        }
        return modelBinder.getModel();
    }

    @Override
    public void setModel(SchemaField schemaField) {
        if(schemaField == null) {
            schemaField = new SchemaField();
            schemaField.setType(type.getSelectedValue());
            isNew = true;
        }
        modelBinder.setModel(schemaField, StateSync.FROM_MODEL);
        select(modelBinder.getModel());
    }

    private void saveField(SchemaField field) {
        if(field.getName() == null || field.getName().isEmpty()) {
            info("Name cannot be empty");
            return;
        } else {
            if(isNew && !checkNameExist(field.getName())) {
                createField(field);
            } else {
                updateField(field);
            }
        }

    }

    private void createField(SchemaField field) {
        Schema current = parentModel;
        List<SchemaField> fields = current.getFields();
        fields.add(field);
        current.setFields(fields);
        schemaUpdated.fire(current);
    }

    private void updateField(SchemaField field) {
        Schema current = parentModel;
        List<SchemaField> fields = current.getFields();
        int idx = 0;
        int idxUpdate = -1;
        for(SchemaField schemaField : fields) {
            if(field.getName().equals(schemaField.getName())) {
                idxUpdate = idx;
            }
            idx++;
        }
        if(idxUpdate != -1) {
            // update
            fields.add(idxUpdate, field);
            current.setFields(fields);
            schemaUpdated.fire(current);
        }
    }

    void select(SchemaField model) {
        if(model != null && model.getType() != null) {
            String modelType = model.getType();
            if(modelType.equals(SchemaField.TYPE.SHORT_TEXT.toString())) {
                type.setSelectedIndex(0);
            } else if(modelType.equals(SchemaField.TYPE.LONG_TEXT.toString())) {
                type.setSelectedIndex(1);
            } else if(modelType.equals(SchemaField.TYPE.DATE.toString())) {
                type.setSelectedIndex(2);
            } else if(modelType.equals(SchemaField.TYPE.MEDIA.toString())) {
                type.setSelectedIndex(3);
            } else if(modelType.equals(SchemaField.TYPE.REFERENCE.toString())) {
                ref.getElement().removeClassName("uk-hidden");

                String referenceType = model.getReferenceType();
                String referenceEntityId = model.getReference();
                type.setSelectedIndex(4);
                final Schema[] referencedSchema = {null};
                schemaService.listSchema().subscribe(schemas -> {
                    schemas.forEach(schema -> {
                        //ref.addItem(schema.getName(), schema.getEntityId());
                        if(referenceType != null && referenceType.equals(schema.getName())) {
                            referencedSchema[0] = schema;
                        }
                    });
                    ref.setValue(referencedSchema[0]);
                    ref.setAcceptableValues(schemas);
                }, error -> Console.error(error.getMessage()));

            } else {
//                current.setType(SchemaField.TYPE.SHORT_TEXT.toString());
//                type.setSelectedIndex(0);
//                saveField(model);
            }
        } else {
            Console.warn("No model or type");
        }
    }

    private boolean checkNameExist(String name) {
        if(parentModel != null) {
            Schema current = parentModel;
            List<SchemaField> fields = current.getFields();
            for(SchemaField schemaField : fields) {
                if(name.equals(schemaField.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setParentModel(Schema parentModel) {
        this.parentModel = parentModel;
    }

}
