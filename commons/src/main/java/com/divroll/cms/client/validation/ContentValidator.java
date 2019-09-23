/**
 * Copyright 2017 Dotweblabs Web Technologies
 *
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or or EPL 1.0 (the "Licenses"). You can
 * select the license that you prefer but you may not use this file except in
 * compliance with one of these Licenses.
 *
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 *
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 *
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 *
 */
package com.divroll.cms.client.validation;


import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ContentValidator {
    private static Logger logger = Logger.getLogger(ContentValidator.class.getName());
    private static Validator validator;
    public static void init() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public static <T extends Object> Set<ConstraintViolation<T>> validate(T bean) {
        return validate(bean, null);
    }

    public static <T extends Object> Set<ConstraintViolation<T>> validate(T bean, Class... sequences) {
        init();
        Set<ConstraintViolation<T>> violations = null;
        try {
            if (sequences != null) {
                violations = validator.validate(bean, sequences);
            } else {
                violations = validator.validate(bean);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error trying to perform validations for bean: " + bean.getClass().getName(), e);
        }
        return violations;
    }
}
