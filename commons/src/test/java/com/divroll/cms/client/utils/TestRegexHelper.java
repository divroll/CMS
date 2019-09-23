package com.divroll.cms.client.utils;

import com.google.gwt.junit.client.GWTTestCase;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
/**
 * Test
 *
 * @author kerbymart
 * @version 1.0.0
 * @since 1.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRegexHelper extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "com.divroll.cms.Commons";
    }
    public void testRegexAlphaNumeric() {
        delayTestFinish(1000);
        assertTrue(RegexHelper.isAlphaNumeric("abc123"));
        assertFalse(RegexHelper.isAlphaNumeric("abc123!"));
        assertFalse(RegexHelper.isAlphaNumeric("@#$%^&"));
        assertFalse(RegexHelper.isAlphaNumeric("abc 123"));
        finishTest();
    }

}
