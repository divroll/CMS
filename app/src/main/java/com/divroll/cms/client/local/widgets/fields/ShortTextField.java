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

import com.divroll.cms.client.model.SchemaField;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextBox;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.inject.Inject;
import javax.inject.Named;

@Templated("/com/divroll/cms/client/local/resource/templates/widgets/fields/ShortTextField.html")
public class ShortTextField extends Composite implements HasModel<SchemaField> {

    SchemaField model;

    @Inject @Named("h2") @DataField HeadingElement name;
    @Inject @DataField TextBox value;

    @Override
    public SchemaField getModel() {
        model.setValue(value.getText());
        return model;
    }

    @Override
    public void setModel(SchemaField field) {
        model = field;
        name.setInnerText(model.getName());
        value.setText(field.getValue());
    }
}
