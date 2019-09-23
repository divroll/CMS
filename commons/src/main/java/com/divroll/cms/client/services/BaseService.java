package com.divroll.cms.client.services;

import com.divroll.backend.sdk.DivrollEntity;
import com.divroll.cms.client.model.Content;
import com.divroll.cms.client.model.User;
import org.json.JSONObject;

public class BaseService {

    protected static final String SCHEMA_CLASSNAME = "Schema";

    protected DivrollEntity getLoggedInUser() {
        String userJSON = LocalStorage.getKey("user");
        if(userJSON != null) {
            JSONObject jsonObject = new JSONObject(userJSON);
            String entityId = jsonObject.getString("entityId");
            String username = jsonObject.getString("username");
            String authToken = jsonObject.getString("authToken");
            DivrollEntity divrollEntity = new DivrollEntity("User");
            divrollEntity.setEntityId(entityId);
            divrollEntity.setProperty("username", username);
            divrollEntity.setProperty("authToken", authToken);
            return divrollEntity;
        }
        return null;
    }

    protected Content copy(DivrollEntity divrollEntity) {
        Content content = new Content(divrollEntity.getEntityType());
        content.setEntityId(divrollEntity.getEntityId());
        content.setAcl(divrollEntity.getAcl());
        content.setDateCreated(divrollEntity.getDateCreated());
        content.setDateUpdated(divrollEntity.getDateUpdated());
        //divrollEntity.getFirstLink();
//        content.getBlobNames().addAll(divrollEntity.getBlobNames());
//        content.getLinkNames().addAll(divrollEntity.getLinkNames());
//        content.getPropertyNames().addAll(divrollEntity.getPropertyNames());
//        content.getLinks().addAll(divrollEntity.getLinks());
//        divrollEntity.setLinkType(divrollEntity.getLinkType());
        divrollEntity.getProperties().keySet().forEach(key -> {
            content.setProperty(key,  divrollEntity.getProperties().get(key));
        });
        return content;
    }
}
