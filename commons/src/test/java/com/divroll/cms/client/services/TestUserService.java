package com.divroll.cms.client.services;

import com.google.gwt.junit.client.GWTTestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestUserService extends GWTTestCase {

    static final Logger logger = Logger.getLogger(TestUserService.class.getName());

    @Override
    public String getModuleName() {
        return "com.divroll.cms.Commons";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
    }

}
