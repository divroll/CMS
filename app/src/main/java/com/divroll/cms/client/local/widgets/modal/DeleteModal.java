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
package com.divroll.cms.client.local.widgets.modal;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.inject.Inject;

@Templated("/com/divroll/cms/client/local/resource/templates/widgets/modal/DeleteModal.html")
public class DeleteModal extends Composite implements HasModel<Command> {

    Command model;

    @Inject @DataField SpanElement name;

    @Inject @DataField Button delete;

    public void show() {
        _show(this.getElement());
    }

    public void hide() {
        _hide(this.getElement());
    }

    private static native void _hide(Element el)/*-{
        var modal = $wnd.UIkit.modal(el);
        modal.hide();
    }-*/;

    private static native void _show(Element el)/*-{
        var modal = $wnd.UIkit.modal(el, {center: true});
        modal.show();
    }-*/;

    public void setName(String name) {
        this.name.setInnerText(name);
    }

    @EventHandler("delete")
    public void delete(ClickEvent event) {
        event.preventDefault();
        if(model != null) {
            model.execute();
        }
        hide();
    }

    @Override
    public Command getModel() {
        return model;
    }

    @Override
    public void setModel(Command command) {
        this.model = command;
    }

}
