package com.elfec.ssc.model.validations;

/**
 * Valida si es que una cadena es transformable a un Int
 * @author Diego
 *
 */
public class NumericValidationRule implements IValidationRule<String> {

	@Override
	public boolean IsValid(String objectToValidate) {
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

}
