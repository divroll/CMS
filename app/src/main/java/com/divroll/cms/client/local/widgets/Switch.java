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

import com.divroll.cms.client.local.ComponentBase;
import com.divroll.cms.client.model.Content;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FocusWidget;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.Templated;

import static com.google.gwt.query.client.GQuery.$;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@Templated("/com/divroll/cms/client/local/resource/templates/widgets/Switch.html")
public class Switch extends FocusWidget implements HasModel<Content> {

    Content model;

//    @Inject @DataField InputElement published;
//    @Inject @DataField("published-label") LabelElement publishedLabel;
//    @Inject @DataField InputElement draft;
//    @Inject @DataField("draft-label")LabelElement draftLabel;

    protected Switch() {
        super();
    }

    protected Switch(Element element){
        super((Element)element.cast());
    }
/*
    @EventHandler("published-label")
    public void publishedLabel(ClickEvent event) {
        event.preventDefault();

        publishedLabel.removeClassName("switch-label-on");
        publishedLabel.removeClassName("switch-label-off");
        draftLabel.removeClassName("switch-label-on");
        draftLabel.removeClassName("switch-label-off");


        publishedLabel.addClassName("switch-label-on");
    }

    @EventHandler("draft-label")
    public void draftLabel(ClickEvent event) {
        event.preventDefault();

        publishedLabel.removeClassName("switch-label-on");
        publishedLabel.removeClassName("switch-label-off");
        draftLabel.removeClassName("switch-label-on");
        draftLabel.removeClassName("switch-label-off");

        draftLabel.addClassName("switch-label-on");

    }
*/
    @Override
    protected void onAttach() {
        super.onAttach();
//        final String uuidPub = uuid();
//        final String uuidDraft = uuid();
//        $(this).find("#published").attr("value", "published-" + uuidPub);
//        $(this).find("#published").attr("id", "published-" + uuidPub);
//        $(this).find("#draft").attr("value", "draft-" + uuidDraft);
//        $(this).find("#draft").attr("id", "draft-" + uuidDraft);
//        $(this).find("#published-label").attr("for", "published-" + uuidPub);
//        $(this).find("#draft-label").attr("for", "draft-" + uuidDraft);
    }

    @Override
    public Content getModel() {
        return model;
    }

    @Override
    public void setModel(Content aContent) {
        model = aContent;
    }

    // TODO: Re-use this somewhere else
    public static native String uuid() /*-{
        var result, i, j;
        result = '';
        for(j=0; j<32; j++) {
            if( j == 8 || j == 12|| j == 16|| j == 20)
                result = result + '-';
                i = $wnd.Math.floor($wnd.Math.random()*16).toString(16).toUpperCase();
            result = result + i;
        }
      return result;
    }-*/;

    public static Switch wrap(Element element) {
        Switch aSwitch = new Switch(element);
        return aSwitch;
    }


}
