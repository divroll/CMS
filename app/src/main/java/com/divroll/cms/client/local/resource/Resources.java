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
package com.divroll.cms.client.local.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Javascript Resources
 *
 * @author kerbymart
 * @version 1.0.0
 * @since 1.0.0
 */
public interface Resources extends ClientBundle{

    Resources INSTANCE = GWT.create(Resources.class);

    @Source("uikit.js")
    TextResource uikit();

    @Source("components/slideshow.js")
    TextResource slideShow();

    @Source("components/slideset.js")
    TextResource slideSet();

    @Source("components/accordion.js")
    TextResource accordion();

    @Source("components/notify.min.js")
    TextResource notification();

    @Source("components/datepicker.min.js")
    TextResource datepicker();

    @Source("components/form-password.min.js")
    TextResource formPassword();

}
