package com.divroll.cms.client.model;

import com.divroll.backend.sdk.DivrollEntity;
import org.jboss.errai.databinding.client.api.Bindable;
import org.json.JSONObject;

@Bindable
public class User implements Model {

    private String entityId;
    private String username;
    private String password;
    private String authToken;

    public User() {
    }

    public User(String userId) {
        setEntityId(userId);
    }

    @Override
    public Model fromDivrolLEntity(DivrollEntity divrollEntity) {
        if(divrollEntity == null){
            return null;
        }
        User user = new User();
        user.setEntityId(divrollEntity.getEntityId());
        user.setUsername(divrollEntity.getStringProperty("username"));
        user.setPassword(divrollEntity.getStringProperty("password"));
        user.setAuthToken(divrollEntity.getStringProperty("authToken"));
        return user;    }

    @Override
    public DivrollEntity toDivrollEntity() {
       DivrollEntity divrollEntity = new DivrollEntity("User");
        if(entityId != null){
            divrollEntity.setEntityId(entityId);

        }
        divrollEntity.setProperty("username", username);
        divrollEntity.setProperty("password", password);
        divrollEntity.setProperty("authToken", authToken);
        return divrollEntity;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("authToken", authToken);
        return jsonObject.toString();
    }
}