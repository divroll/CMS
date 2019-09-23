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
package com.divroll.cms.client.utils;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

//TODO: Copy this to Parse-SDK-GWT
public class ParseJSON {
    private JSONValue jsonValue = null;
    private JSONObject jsonObject = null;
    public ParseJSON(JSONValue jsonValue) {
        this.jsonValue = jsonValue;
        if(jsonValue.isObject() != null) {
            jsonObject = jsonValue.isObject();
        }
    }
    public String getString(String key) {
        if(jsonObject != null) {
            if(jsonObject.get(key).isString() != null) {
                return jsonObject.get(key).isString().stringValue();
            }
        }
        return null;
    }
    public Double getNumber(String key) {
        if(jsonObject != null) {
            if(jsonObject.get(key).isNumber() != null) {
                return jsonObject.get(key).isNumber().doubleValue();
            }
        }
        return null;
    }
    public Boolean getBoolean(String key) {
        if(jsonObject != null) {
            if(jsonObject.get(key).isBoolean() != null) {
                return jsonObject.get(key).isBoolean().booleanValue();
            }
        }
        return null;
    }
    public JSONArray getArray(String key) {
        if(jsonObject != null) {
            if(jsonObject.get(key).isArray() != null) {
                return jsonObject.get(key).isArray();
            }
        }
        return null;
    }
    public JSONObject getObject(String key) {
        if(jsonObject != null) {
            if(jsonObject.get(key) != null && jsonObject.get(key).isObject() != null) {
                return jsonObject.get(key).isObject();
            }
        }
        return null;
    }
}
