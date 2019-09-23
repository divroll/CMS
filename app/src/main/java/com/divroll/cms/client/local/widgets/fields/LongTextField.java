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
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.inject.Inject;
import javax.inject.Named;

@Templated("/com/divroll/cms/client/local/resource/templates/widgets/fields/LongTextField.html")
public class LongTextField extends Composite implements HasModel<SchemaField> {

    SchemaField model;

    @Inject @Named("h2") @DataField HeadingElement name;
//    @Inject @DataField TextArea value;
    @Inject @DataField DivElement toolbar;
    @Inject @DataField DivElement editor;

    JavaScriptObject quill;

    @Override
    protected void onAttach() {
        super.onAttach();
        quill = quill(toolbar, editor);
    }

    @Override
    public SchemaField getModel() {
        model.setValue(turndown(getHTML(quill)));
        return model;
    }

    @Override
    public void setModel(SchemaField field) {
        model = field;
        name.setInnerText(model.getName());
        if(field.getValue() != null) {
            setHTML(quill, showdown(field.getValue()));
        }
        //value.setText(field.getValue());
        //value.setValue(field.getValue());
    }

    public static native JavaScriptObject quill(Element toolbarEl, Element editorEl)/*-{
        var editor = new $wnd.Quill(editorEl, {
            modules: { toolbar: toolbarEl },
            theme: 'snow'
        });
        return editor;
    }-*/;

    public static native String getHTML(JavaScriptObject quill) /*-{
        var html = quill.root.innerHTML;
        return html;
    }-*/;

    public static native void setHTML(JavaScriptObject quill, String html)/*-{
        quill.root.innerHTML = html;
    }-*/;

    public static native String getText(JavaScriptObject quill)/*-{
        var text = quill.getText();
        return text;
    }-*/;

    public static native void setText(JavaScriptObject quill, String text)/*-{
        quill.setText(text);
    }-*/;

    public static native String turndown(String html) /*-{
        var turndownService = new $wnd.TurndownService();
        var markdown = turndownService.turndown(html);
        return markdown;
    }-*/;

    public static native String showdown(String markdown) /*-{
        var converter = new $wnd.showdown.Converter()
        var html = converter.makeHtml(markdown);
        return html;
    }-*/;


}
