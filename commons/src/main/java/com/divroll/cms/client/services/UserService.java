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

import com.divroll.backend.sdk.Divroll;
import com.divroll.backend.sdk.DivrollUser;
import com.divroll.cms.client.model.User;
import io.reactivex.Single;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserService {
    public UserService() {}

    public Single<User> login(String username, String password) {
        DivrollUser divrollUser = new DivrollUser();
        divrollUser.setUsername(username);
        divrollUser.setPassword(password);
        return divrollUser.login().flatMap(divrollUser1 -> {
            User user = new User();
            user.setUsername(divrollUser1.getUsername());
            user.setPassword(null);
            user.setAuthToken(divrollUser1.getAuthToken());

            JSONObject userJSON = new JSONObject();
            userJSON.put("username", divrollUser1.getUsername());
            userJSON.put("authToken", divrollUser1.getAuthToken());
            userJSON.put("entityId", divrollUser1.getEntityId());
            LocalStorage.setKey("user", userJSON.toString());

            return Single.just(user);
        });
    }

    public Single<User> getUser(String authToken) {
        DivrollUser divrollUser = new DivrollUser();
        divrollUser.setAuthToken(authToken);
        Divroll.setAuthToken(authToken);
        return divrollUser.login().flatMap(divrollUser1 -> {
            User user = new User();
            user.setUsername(divrollUser1.getUsername());
            user.setPassword(null);
            user.setAuthToken(divrollUser1.getAuthToken());

            JSONObject userJSON = new JSONObject();
            userJSON.put("username", divrollUser1.getUsername());
            userJSON.put("authToken", divrollUser1.getAuthToken());
            userJSON.put("entityId", divrollUser1.getEntityId());
            LocalStorage.setKey("user", userJSON.toString());

            return Single.just(user);
        });
    }

}
