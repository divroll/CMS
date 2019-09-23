package com.divroll.cms.client.model;

import org.jboss.errai.databinding.client.api.Bindable;

@Bindable
public class SchemaField {

    public SchemaField() {

    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public Schema getParent() {
        return parent;
    }

    public void setParent(Schema parent) {
        this.parent = parent;
    }

    public enum TYPE { SHORT_TEXT, LONG_TEXT, DATE, MEDIA, REFERENCE }
    private String name;
    private String type;
    private String reference; // Reference Entity ID
    private String referenceType;
    private String value;
    private Schema parent;

    @Override
    public boolean equals(Object obj) {
        if( !(obj instanceof SchemaField) ) {
            return false;
        }
        SchemaField schemaField = (SchemaField) obj;
        return schemaField.getName().equals(this.getName());
    }
}
