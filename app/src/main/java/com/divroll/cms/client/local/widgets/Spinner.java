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

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
public class Spinner {
    public static void spin(Widget widget) {
        String text = widget.getElement().getInnerText();
        widget.getElement().setInnerHTML("<i class='fa fa-circle-o-notch fa-spin'></i>");
        widget.getElement().setAttribute("__spin__", text);
    }
    public static void unspin(Widget widget) {
        String text = widget.getElement().getAttribute("__spin__");
        widget.getElement().setInnerText(text);
    }
    public static void spin(Element el) {
        String text = el.getInnerText();
        el.setInnerHTML("<i class='fa fa-circle-o-notch fa-spin'></i>");
        el.setAttribute("__spin__", text);
    }
    public static void unspin(Element el) {
        String text = el.getAttribute("__spin__");
        el.setInnerText(text);
    }
}
