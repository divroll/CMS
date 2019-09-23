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

import com.divroll.backend.sdk.Divroll;
import com.divroll.backend.sdk.DivrollACL;
import com.divroll.backend.sdk.DivrollEntity;
import com.divroll.backend.sdk.helper.Pair;
import com.divroll.cms.client.local.resource.UIKit;
import com.divroll.cms.client.local.widgets.upload.Upload;
import com.divroll.cms.client.local.widgets.upload.UploadCallback;
import com.divroll.cms.client.model.Content;
import com.divroll.cms.client.model.Schema;
import com.divroll.cms.client.model.SchemaField;
import com.divroll.http.client.Base64;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import elemental2.core.ArrayBuffer;
import elemental2.core.Function;
import elemental2.dom.*;
import jsinterop.base.Js;
import org.apache.xpath.operations.Div;
import org.jboss.errai.common.client.logging.util.Console;
import org.jboss.errai.ui.client.widget.HasModel;
import org.jboss.errai.ui.shared.api.annotations.DataField;
import org.jboss.errai.ui.shared.api.annotations.EventHandler;
import org.jboss.errai.ui.shared.api.annotations.Templated;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;

@Templated("/com/divroll/cms/client/local/resource/templates/widgets/fields/MediaField.html")
public class MediaField extends Composite implements HasModel<Pair<SchemaField,Content>> {

    Pair<SchemaField,Content> model;

    @Inject @Named("h2") @DataField HeadingElement name;
    @Inject @DataField("choose") Button upload;
    //@Inject @DataField DivElement preview;
    @Inject @DataField("file-name") AnchorElement fileName;

    @Override
    public Pair<SchemaField,Content> getModel() {
        return model;
    }

    @Override
    public void setModel(Pair<SchemaField,Content> field) {
        model = field;
        name.setInnerText(model.first.getName());
        String fileJSONString = model.first.getValue();
        try {
            JSONObject jsonObject = new JSONObject(fileJSONString);
            fileName.setInnerText(model.first.getName());
            fileName.setAttribute("href", jsonObject.getString("url"));
        } catch (Exception e) {
            Console.error(e.getMessage());
        }
    }


    @EventHandler("choose")
    public void upload(ClickEvent event) {
        event.getRelativeElement();
        Upload uploadWidget = new Upload();
        uploadWidget.createUploadFileObject().subscribe(fileObject -> {
            upload(Js.cast(fileObject), true);
        }, error -> Console.error(error.getMessage()));
    }

    protected void upload(File file, boolean asBinary) {

        Content content = model.second;
        SchemaField schemaField = model.first;

        FormData formData = new FormData();
        formData.append("file", file);

        XMLHttpRequest request = new XMLHttpRequest();
        request.upload.onprogress = p0 -> {
            double percentage = 100 - ( ( (p0.total - p0.loaded) / p0.total) * 100 );
            if(!Double.isInfinite(percentage)) {
                if(percentage == 100L) {
                }
            }
        };

        request.onerror = p0 -> {
            // TODO: Check actual error
            Console.error(request.responseText);
            return null;
        };

//        DivrollEntity mediaEntity = new DivrollEntity("Media");
//        DivrollACL acl = new DivrollACL();
//        acl.setPublicRead(false);
//        acl.setPublicWrite(false);
//        acl.setAclRead(Arrays.asList("0-1"));
//        acl.setAclWrite(Arrays.asList("0-1"));
//        mediaEntity.setAcl(acl);
//        String blobName = uuid();


        request.onreadystatechange = p0 -> {
            if(request.readyState == XMLHttpRequest.DONE) {
                if(request.status == 401) {
                    Console.error(request.responseText);
                } else if(request.status >= 400) {
                    Console.error(request.responseText);
                } else if(request.status == 201) {
                    // Success
                    UIKit.notif("Upload complete", "info", 3000L, "top-right");
                    String blobPath = getBlobPath(content.getEntityType(), content.getEntityId(), schemaField.getName());
                    JSONObject fileObj = new JSONObject();
                    fileObj.put("type", "File");
                    fileObj.put("url", blobPath);
                    schemaField.setValue(fileObj.toString());
                    setModel(new Pair<SchemaField, Content>(schemaField, content));
                }
            }
            return null;
        };



        request.open("POST", Divroll.getServerUrl() + "/entities/" + content.getEntityType() + "/" + content.getEntityId() + "/blobs/" + schemaField.getName());
        request.setRequestHeader("X-Divroll-Auth-Token", Divroll.getAuthToken());
        request.setRequestHeader("X-Divroll-Api-Key", Divroll.getApiKey());
        request.setRequestHeader("X-Divroll-App-Id", Divroll.getAppId());
        if(asBinary) {
            FileReader fileReader = new FileReader();;
            fileReader.onload = p0 -> {
                ArrayBuffer arrayBuffer = fileReader.result.asArrayBuffer();
                request.setRequestHeader("Content-Type", "application/octet-stream");
                request.send(arrayBuffer);
                return arrayBuffer;
            };
            fileReader.readAsArrayBuffer(file);
        } else {
            request.send(formData);
        }


    }

    public native static String uuid() /*-{
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g,
            function(c) {
                var r = Math.random() * 16 | 0, v = c == 'x' ? r
                        : (r & 0x3 | 0x8);
                return v.toString(16);
            });
    }-*/;

    public String getBlobPath(String entityType, String entityId, String blobName) {
        //String appId, String apiKey, String masterKey, String nameSpace
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appId", Divroll.getAppId());
        jsonObject.put("apiKey", Divroll.getApiKey());
        jsonObject.put("nameSpace", Divroll.getNamespace());
        jsonObject.put("entityId", entityId);
        jsonObject.put("entityType", entityType);
        jsonObject.put("blobName", blobName);
        String jsonString = jsonObject.toString();
        String base64path =  Base64.btoa(jsonString);
        String completeUrl = Divroll.getServerUrl() + "/blobs/" + base64path;
        return completeUrl;
    }

}
