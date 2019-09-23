package com.divroll.cms.client.model;

import com.divroll.backend.sdk.DivrollEntity;

public class Content extends DivrollEntity {

    private Schema schema;

    public Content(String entityType) {
        super(entityType);
    }

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    @Override
    public boolean equals(Object obj) {
        return ( (obj instanceof Content)  && ((Content) obj).getEntityId().equals(getEntityId()) );
    }
}
