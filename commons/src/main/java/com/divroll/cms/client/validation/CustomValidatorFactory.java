package com.divroll.cms.client.validation;

import javax.validation.Validator;

import com.divroll.cms.client.model.Content;
import com.divroll.cms.client.validation.groups.DraftValidationGroup;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.validation.client.AbstractGwtValidatorFactory;
import com.google.gwt.validation.client.GwtValidation;
import com.google.gwt.validation.client.impl.AbstractGwtValidator;

public final class CustomValidatorFactory extends AbstractGwtValidatorFactory {

	@Override
	public AbstractGwtValidator createValidator() {
		return GWT.create(GwtValidator.class);
	}
	
	@GwtValidation(value = Content.class, groups = {DraftValidationGroup.class})
	public interface GwtValidator extends Validator{
		
	}

}
