/*
 * Divroll, Platform for Hosting Static Sites
 * Copyright 2019, Divroll, and individual contributors
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
package com.divroll.cms.client.local.widgets.upload;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import io.reactivex.Single;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
public class Upload {

    private Element uploadElement;

    public Single<FileObject> createUploadFileObject() {
        return Single.create(e -> {
            uploadElement = DOM.createElement("input");
            uploadElement.setAttribute("type", "file");
            uploadElement.setAttribute("accept", "*/*");
            Event.sinkEvents(uploadElement, Event.ONCHANGE);
            Event.setEventListener(uploadElement, event -> {
                // TODO: Pass these values to modal to render
                final FileObject fileObject = (FileObject) getFile(event);
                e.onSuccess(fileObject);
            });
            click();
        });
    }

    public Single<FileObject> createUploadFileObject(String entityType, String entityId, String blobName) {
        return Single.create(e -> {
            uploadElement = DOM.createElement("input");
            uploadElement.setAttribute("type", "file");
            uploadElement.setAttribute("accept", "*/*");
            Event.sinkEvents(uploadElement, Event.ONCHANGE);
            Event.setEventListener(uploadElement, event -> {
                // TODO: Pass these values to modal to render
                final FileObject fileObject = (FileObject) getFile(event);
                e.onSuccess(fileObject);
            });
            click();
        });
    }

    public void click() {
        clickJsni(uploadElement);
    }

    public static native void clickJsni(Element element)/*-{
        element.click();
    }-*/;

    public static native void readAsDataURL(FileObject file, FileReaderCallback callback) /*-{
        var reader  = new $wnd.FileReader();
        reader.addEventListener("load", function () {
            var result = reader.result;
            callback.@com.divroll.cms.client.local.widgets.upload.FileReaderCallback::onLoad(*)(result);
        }, false);
        if (file) {
            reader.readAsDataURL(file);
        }
    }-*/;

    public static native JavaScriptObject getFile(Element e)/*-{
        var files = e.target.files || e.dataTransfer.files;
        var file = files[0];
        $wnd.console.log("file="+file);
        return file;
    }-*/;

    public static native JavaScriptObject getFile(NativeEvent e)/*-{
        var files = e.target.files || e.dataTransfer.files;
        var file = files[0];
        $wnd.console.log("file="+JSON.stringify(file));
        return file;
    }-*/;
}
