/**
 * Copyright 2017 Dotweblabs Web Technologies
 *
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or or EPL 1.0 (the "Licenses"). You can
 * select the license that you prefer but you may not use this file except in
 * compliance with one of these Licenses.
 *
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 *
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 *
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 *
 */
package com.divroll.cms.client.services;

import com.google.gwt.storage.client.Storage;

/**
 * Helper for Local storage
 *
 * @author kerbymart
 * @version 1.0.0
 * @since 1.0.0
 */
public class LocalStorage {
    public static void setKey(String key, String value) {
        Storage storage = Storage.getLocalStorageIfSupported();
        if(storage != null) {
            storage.setItem(key, value);
        }
    }

    public static String getKey(String key) {
        Storage storage = Storage.getLocalStorageIfSupported();
        if(storage != null) {
            return storage.getItem(key);
        }
        return null;
    }

    public static void removeKey(String key) {
        Storage storage = Storage.getLocalStorageIfSupported();
        if(storage != null) {
            storage.removeItem(key);
        }
    }
}
