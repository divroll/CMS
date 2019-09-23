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
package com.divroll.cms.client.local.widgets.fields;

import com.divroll.cms.client.local.ComponentBase;
import com.divroll.cms.client.model.Content;
import com.divroll.cms.client.model.SchemaField;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.text.shared.Renderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ValueListBox;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Templated("/com/divroll/cms/client/local/resource/templates/widgets/fields/ReferenceField.html")
public class ReferenceField extends ComponentBase implements HasModel<SchemaField> {

    SchemaField model;

    List<Content> contentList = new LinkedList<>();

    @Inject @Named("h2") @DataField HeadingElement name;
    @DataField
    ValueListBox<Content> values = new ValueListBox<>(new Renderer<Content>() {
        @Override
        public String render(Content content) {
            if(content.getSchema() != null && content.getSchema().getFields() != null && !content.getSchema().getFields().isEmpty()) {
                SchemaField schemaField = content.getSchema().getFields().get(0);
                return content.getStringProperty(schemaField.getName());
            }
            return null;
        }
        @Override
        public void render(Content content, Appendable appendable) throws IOException {
            appendable.append(render(content));
        }
    });

    @Override
    protected void onAttach() {
        super.onAttach();
    }

    @Override
    public SchemaField getModel() {
        return model;
    }

    @Override
    public void setModel(final SchemaField field) {
        model = field;
        name.setInnerText(model.getName());
//        String entityId = field.getReference();
//        if(contentList != null) {
//            contentList.forEach(content -> {
//                Window.alert(content.getEntityId());
//                if(entityId.equals(content.getEntityId())) {
//                    Window.alert("!!!");
//                    values.setValue(content);
//                }
//            });
//        }
    }


    public void setValue(Content selectedValue) {
        values.setValue(selectedValue);
    }

    public void setValues(Content selectedValue , List<Content> contentList) {
        if(contentList == null) {
            return;
        }
        values.setValue(selectedValue);
        values.setAcceptableValues(contentList);
        this.contentList = contentList;
    }

    public void setValues(List<Content> contentList) {
        if(contentList == null) {
            return;
        }
        values.setValue(contentList.stream().findFirst().get());
        values.setAcceptableValues(contentList);
        this.contentList = contentList;
    }

    public Content getValue() {
        return values.getValue();
    }

}
