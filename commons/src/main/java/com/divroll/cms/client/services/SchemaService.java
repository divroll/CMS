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

import com.divroll.backend.sdk.Divroll;
import com.divroll.backend.sdk.DivrollACL;
import com.divroll.backend.sdk.DivrollEntities;
import com.divroll.backend.sdk.DivrollEntity;
import com.divroll.cms.client.model.Schema;
import com.divroll.cms.client.model.SchemaField;
import com.divroll.cms.client.utils.RegexHelper;
import com.divroll.cms.client.validation.exceptions.InvalidNameException;
import io.reactivex.Single;
import org.jboss.errai.common.client.logging.util.Console;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class SchemaService extends BaseService {

    public Single<Schema> createSchema(final String name, final String description) {
        if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
            return Single.error(new IllegalArgumentException("No authentication token was found"));
        }
        if(!RegexHelper.isAlphaNumeric(name)) {
            return Single.error(new InvalidNameException());
        }
        DivrollEntity divrollEntity = new DivrollEntity(SCHEMA_CLASSNAME);
        divrollEntity.setProperty("name", name);
        divrollEntity.setProperty("description", description);
        DivrollACL acl = DivrollACL.build();
        acl.setAclWrite(Arrays.asList(getLoggedInUser().getEntityId(), "0-1"));
        acl.setAclRead(Arrays.asList(getLoggedInUser().getEntityId(), "0-1"));
        acl.setPublicWrite(false);
        acl.setPublicRead(false);
        divrollEntity.setAcl(acl);
        return divrollEntity.create().flatMap(divrollEntity1 -> {
            if(divrollEntity1 != null) {
                Schema schema = new Schema();
                String dateCreated = divrollEntity1.getDateCreated();
                String dateUpdated = divrollEntity1.getDateUpdated();
                schema.setCreatedAt(dateCreated);
                schema.setUpdatedAt(dateUpdated);
                schema.setEntityId(divrollEntity1.getEntityId());
                schema.setName(divrollEntity1.getStringProperty("name"));
                schema.setDescription(divrollEntity1.getStringProperty("description"));
                return Single.just(schema);
            }
            return null;
        });
    }

    public Single<List<Schema>> listSchema() {
        DivrollEntities divrollEntities = new DivrollEntities(SCHEMA_CLASSNAME);
        return divrollEntities.query().flatMap(divrollEntities1 -> {
            if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
                return Single.error(new IllegalArgumentException("No authentication token was found"));
            }
            final List<Schema> schemas = new LinkedList<>();
            divrollEntities1.getEntities().forEach(divrollEntity -> {
                Schema schema = new Schema();
                schema.setDescription(divrollEntity.getStringProperty("description"));
                schema.setName(divrollEntity.getStringProperty("name"));
                schema.setEntityId(divrollEntity.getEntityId());
                schema.setCreatedAt(divrollEntity.getDateCreated());
                schema.setUpdatedAt(divrollEntity.getDateUpdated());
                List<Object> fields = (List<Object>) divrollEntity.getProperty("fields");
                if(fields != null) {
                    fields.forEach(field -> {
                        if(field != null) {
                            Map<String,Object> mapField = (Map<String, Object>) field;
                            SchemaField schemaField = new SchemaField();
                            schemaField.setParent(schema);
                            schemaField.setName(String.valueOf(mapField.get("name")));
                            schemaField.setReference(String.valueOf(mapField.get("reference")));
                            schemaField.setReferenceType(String.valueOf(mapField.get("referenceType")));
                            schemaField.setType(String.valueOf(mapField.get("type")));
                            schemaField.setValue(String.valueOf(mapField.get("value")));
                            schema.getFields().add(schemaField);
                        }
                    });
                }
                schemas.add(schema);
            });
            return Single.just(schemas);
        });
    }

    public Single<List<Schema>> listSchema(int limit, int skip) {
        if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
            return Single.error(new IllegalArgumentException("No authentication token was found"));
        }
        DivrollEntities divrollEntities = new DivrollEntities(SCHEMA_CLASSNAME);
        divrollEntities.setSkip(skip);
        divrollEntities.setLimit(limit);
        return divrollEntities.query().flatMap(divrollEntities1 -> {
            List<Schema> schemas = new LinkedList<>();
            divrollEntities1.getEntities().forEach(divrollEntity -> {
                Schema schema = new Schema();
                schema.setDescription(divrollEntity.getStringProperty("description"));
                schema.setName(divrollEntity.getStringProperty("name"));
                schema.setEntityId(divrollEntity.getEntityId());
                schema.setCreatedAt(divrollEntity.getDateCreated());
                schema.setUpdatedAt(divrollEntity.getDateUpdated());
                List<Object> fields = (List<Object>) divrollEntity.getProperty("fields");
                fields.forEach(field -> {
                    Map<String,Object> mapField = (Map<String, Object>) field;
                    SchemaField schemaField = new SchemaField();
                    schemaField.setParent(schema);
                    schemaField.setName(String.valueOf(mapField.get("name")));
                    schemaField.setReference(String.valueOf(mapField.get("reference")));
                    schemaField.setReferenceType(String.valueOf(mapField.get("referenceType")));
                    schemaField.setType(String.valueOf(mapField.get("type")));
                    schemaField.setValue(String.valueOf(mapField.get("value")));
                    schema.getFields().add(schemaField);
                });
                schemas.add(schema);
            });
            return Single.just(schemas);
        });
    }

    public Single<Schema> updateSchema(final Schema schema) {
        if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
            return Single.error(new IllegalArgumentException("No authentication token was found"));
        }
        if(schema.getEntityId() == null || schema.getEntityId().isEmpty()) {
            return Single.error(new NullPointerException("Schema id is null or empty"));
        }

        if(!RegexHelper.isAlphaNumeric(schema.getName())) {
            return Single.error(new InvalidNameException());
        }
        DivrollEntity divrollEntity = new DivrollEntity(SCHEMA_CLASSNAME);
        divrollEntity.setEntityId(schema.getEntityId());
        divrollEntity.setProperty("name", schema.getName());
        divrollEntity.setProperty("description", schema.getDescription());

        if(schema.getFields() != null) {
            org.json.JSONArray jsonArray = new org.json.JSONArray();
            schema.getFields().forEach(schemaField -> {
                String name = schemaField.getName();
                String type = schemaField.getType();
                String ref = schemaField.getReference();
                String refType = schemaField.getReferenceType();
                String val = schemaField.getValue();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", name);
                jsonObject.put("type", type);
                if(ref != null) {
                    jsonObject.put("reference", ref);
                }
                if(refType != null) {
                    jsonObject.put("referenceType", refType);
                }
                if(val != null) {
                    jsonObject.put("value", val);
                }
                jsonArray.put(jsonObject);
            });
            divrollEntity.setProperty("fields", jsonArray);
        }
        DivrollACL acl = DivrollACL.build();
        acl.setAclWrite(Arrays.asList(getLoggedInUser().getEntityId(), "0-1"));
        acl.setAclRead(Arrays.asList(getLoggedInUser().getEntityId(), "0-1"));
        acl.setPublicWrite(false);
        acl.setPublicRead(false);
        divrollEntity.setAcl(acl);
        return divrollEntity.update().flatMap(updated -> {
            return divrollEntity.retrieve();
        }).flatMap(divrollEntity1 -> {
            Schema ss = new Schema();
            String dateCreated = divrollEntity1.getDateCreated();
            String dateUpdated = divrollEntity1.getDateUpdated();
            ss.setCreatedAt(dateCreated);
            ss.setUpdatedAt(dateUpdated);
            ss.setEntityId(divrollEntity1.getEntityId());
            ss.setName(divrollEntity1.getStringProperty("name"));
            ss.setDescription(divrollEntity1.getStringProperty("description"));
            return Single.just(ss);
        });
    }

    public Single<Boolean> removeShema(String schemaId) {
        if(Divroll.getAuthToken() == null || Divroll.getAuthToken().isEmpty()) {
            return Single.error(new IllegalArgumentException("No authentication token was found"));
        }
        DivrollEntity divrollEntity = new DivrollEntity(SCHEMA_CLASSNAME);
        divrollEntity.setEntityId(schemaId);
        return divrollEntity.delete();
    }

}
