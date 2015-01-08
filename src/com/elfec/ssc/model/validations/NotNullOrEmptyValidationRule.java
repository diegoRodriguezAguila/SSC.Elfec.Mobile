package com.elfec.ssc.model.validations;

public class NotNullOrEmptyValidationRule implements IValidationRule<String> {

	@Override
	public boolean IsValid(String objectToValidate) {
		return objectToValidate != null && objectToValidate.length() > 0 &&
				objectToValidate.trim().length() > 0;
	}

}
