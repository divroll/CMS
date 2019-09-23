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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBoxBase;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
public class FormPasswordTextBox extends TextBoxBase {

    Element input = DOM.createInputPassword();
    Element a = DOM.createAnchor();

    protected FormPasswordTextBox(Element elem) {
        super(elem);
    }
    public FormPasswordTextBox(){
        this(Document.get().createDivElement(), "uk-form-password");
    }

    public FormPasswordTextBox(Element element, String styleName) {
        super(element);
        a.addClassName("uk-form-password-toggle");
        a.setAttribute("data-uk-form-password", "");
        a.setInnerText("Show");
        element.appendChild(input);
        element.appendChild(a);
        if(styleName != null) {
            element.addClassName(styleName);
        }
    }

    @Override
    public String getText() {
        return input.getPropertyString("value");
    }

    @Override
    public void setText(String text) {
        input.setPropertyString("value", text);
    }

    /**
     * Add class like, 'uk-width-1-1', 'uk-small-1-1', etc.
     *
     * @param className
     */
    public void addClass(String className){
        getElement().addClassName(className);
    }

    public void setWidth(String sizing){
        input.addClassName(sizing);
    }

    public void setWidth(String... sizings){
        for(String w : sizings){
            input.addClassName(w);
        }
    }

}
