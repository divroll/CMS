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
package com.divroll.cms.client.services;

import com.divroll.cms.client.model.User;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

import javax.enterprise.context.ApplicationScoped;

@Deprecated
@ApplicationScoped
public class UserProducer {

    private boolean testMode = false;
    private boolean rememberMe = false;

    private User user;

    public User getUser() {
        if(user == null) {
            Storage storage = Storage.getLocalStorageIfSupported();
            String json = null;
            if(storage != null) {
                json = storage.getItem("user");
            } else {
                json = Cookies.getCookie("user");
            }
            if(json == null) {
                return null;
            }
            JSONValue jsonValue = JSONParser.parseStrict(json);
            if(jsonValue != null) {
                JSONObject jsonObject = jsonValue.isObject();
                String objectId = jsonObject.get("objectId").isString().stringValue();
                String username = jsonObject.get("username").isString().stringValue();
                String sessionToken = jsonObject.get("sessionToken").isString().stringValue();
                User user = new User();
                user.setEntityId(objectId);
                user.setUsername(username);
                user.setAuthToken(sessionToken);
                return user;
            }
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        Storage storage = Storage.getLocalStorageIfSupported();
        if(storage != null) {
            storage.setItem("user", user.toString());
        } else {
            Cookies.setCookie("user", user.toString());
        }
    }
    public void logout(final AsyncCallback<Boolean> callback){
        // TODO
    }

    public boolean isTestMode() {
        return testMode;
    }

    public void setTestMode(Boolean testMode) {
        this.testMode = testMode;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

}
