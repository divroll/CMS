package com.divroll.cms.client.model;

import org.jboss.errai.databinding.client.api.Bindable;

import java.util.LinkedList;
import java.util.List;

@Bindable
public class Schema {

    private String entityId;
    private String createdAt;
    private String updatedAt;
    private String name;
    private String description;
    private List<SchemaField> fields;

    public Schema() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SchemaField> getFields() {
        if(fields == null) {
            fields = new LinkedList<>();
        }
        return fields;
    }

    public void setFields(List<SchemaField> fields) {
        List<SchemaField> fieldsToAdd = new LinkedList<>();
        fields.forEach(schemaField -> {
            if(!fieldsToAdd.contains(schemaField)) {
                fieldsToAdd.add(schemaField);
            }
        });
        this.fields = fieldsToAdd;
    }
    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
