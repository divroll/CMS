package com.divroll.cms.client.model;

import com.divroll.backend.sdk.DivrollEntity;

public interface Model {
    Model fromDivrolLEntity(DivrollEntity divrollEntity);
    DivrollEntity toDivrollEntity();
}
