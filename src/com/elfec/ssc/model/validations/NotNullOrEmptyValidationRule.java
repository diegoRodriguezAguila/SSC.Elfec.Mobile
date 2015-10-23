package com.elfec.ssc.model.validations;

public class NotNullOrEmptyValidationRule implements IValidationRule<String> {

	@Override
	public boolean isValid(String objectToValidate, String... params) {
		return objectToValidate != null && objectToValidate.length() > 0 &&
				objectToValidate.trim().length() > 0;
	}

	@Override
	public String getErrorMessage(String fieldName, boolean isMaleGender) {
		return (isMaleGender?"El ":"La ")+fieldName+" no puede estar "+(isMaleGender?"vacio.":"vacia.");
	}

}
