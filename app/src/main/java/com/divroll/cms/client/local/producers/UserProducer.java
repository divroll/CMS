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
package com.divroll.cms.client.local.producers;

import com.divroll.backend.sdk.Divroll;
import com.divroll.cms.client.model.User;
import com.divroll.cms.client.services.LocalStorage;
import org.jboss.errai.common.client.logging.util.Console;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;

/**
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
@ApplicationScoped
public class UserProducer {
    private User user;

    public User getUser() {
        if(user == null) {
            try {
                String userJSONString = LocalStorage.getKey("user");
                JSONObject userJSON = new JSONObject(userJSONString);
                user = new User();
                user.setUsername(userJSON.getString("username"));
                user.setAuthToken(userJSON.getString("authToken"));
                user.setEntityId(userJSON.getString("entityId"));
                Divroll.setAuthToken(userJSON.getString("authToken"));
            } catch (Exception e) {
                Console.error(e.getMessage());
            }
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", user.getUsername());
        jsonObject.put("authToken", user.getAuthToken());
        jsonObject.put("entityId", user.getEntityId());
        LocalStorage.setKey("user", jsonObject.toString());
        Divroll.setAuthToken(user.getAuthToken());
    }
}
