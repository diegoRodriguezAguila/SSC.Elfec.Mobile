package com.elfec.ssc.model.validations;

/**
 * Valida si es que una cadena es transformable a un Int
 * @author Diego
 *
 */
public class NumericValidationRule implements IValidationRule<String> {

	@Override
	public boolean isValid(String objectToValidate, String... params) {
		try
		{
			Integer.parseInt(objectToValidate);
		}
		catch(NumberFormatException e)
		{
			return false;
		}
		return true;
	}

	@Override
	public String getErrorMessage(String fieldName, boolean isMaleGender) {
		return (isMaleGender?"El ":"La ")+fieldName+" tiene que ser "+(isMaleGender?"numérico-":"numérica.");
	}

}
