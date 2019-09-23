package com.divroll.cms.client.model;

import com.divroll.cms.client.validation.ValidationMessages;
import com.divroll.cms.client.validation.constraint.NotNull;
import com.divroll.cms.client.validation.groups.DraftValidationGroup;
import com.divroll.cms.client.validation.groups.PersistenceValidationGroup;

public class ContentWrapper {

    @NotNull(groups = { DraftValidationGroup.class, PersistenceValidationGroup.class }, message = ValidationMessages.EMPTY_FIELD)
    private Content content;

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
