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

import com.divroll.cms.client.local.ComponentBase;
import com.divroll.cms.client.model.User;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import javax.inject.Inject;

@Templated("/com/divroll/cms/client/local/resource/templates/widgets/modal/SetupModal.html")
public class SetupModal extends ComponentBase implements HasModel<User> {

    private static final String CODE_TEMPLATE =
            "<script type=\"text/javascript\" language=\"javascript\" src=\"sdk/sdk.nocache.js\"></script>\n" +
            "<script>\n" +
            "   var graffiti = new Graffiti(${{}});\n" +
            "   graffiti.getContents('Post', {\n" +
            "       success: function(results) {\n" +
            "           for (var i = 0; i < results.length; i++) {\n" +
            "               var object = results[i];\n" +
            "               // use contents\n" +
            "           }\n" +
            "       },\n" +
            "       error: function(error) {\n" +
            "           alert(error);\n" +
            "       }\n" +
            "   },{\n" +
            "       include: 'Author',\n" +
            "       skip: 0,\n" +
            "       limit: 100\n" +
            "   });\n" +
            "</script>";

    User model;

    @DataField Element code = DOM.createElement("code");
    @Inject @DataField Button copy;

    @Override
    protected void onAttach() {
        super.onAttach();
        highlightElement(code);
    }

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

    @EventHandler("copy")
    public void copy(ClickEvent event) {
        event.preventDefault();
        //model.execute();
        hide();
    }

    @Override
    public User getModel() {
        return model;
    }

    @Override
    public void setModel(User user) {
        this.model = user;
        //String template = CODE_TEMPLATE.replace("${{}}", "'" + user.getObjectId() + "','" + getApplicationId() + "'");
        //code.setInnerText(template);
    }

    public static native void highlightElement(Element el)/*-{
        $wnd.Prism.highlightElement(el);
        $wnd.console.log("After call of Prism.highlightAll()");
    }-*/;
}
