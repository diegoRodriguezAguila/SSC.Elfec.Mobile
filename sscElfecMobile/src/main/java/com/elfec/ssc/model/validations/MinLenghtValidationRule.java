package com.elfec.ssc.model.validations;

/**
 * Valida si el tama�o de una cadena es v�lido, es decir si su longitud es mayor o igual que el tama�o
 * m�nimo requerido
 * @author Diego
 *
 */
public class MinLenghtValidationRule implements IValidationRule<String> {

	private int minLenght = 0;
	@Override
	public boolean isValid(String objectToValidate, String... params) {
		minLenght = Integer.parseInt(params[0]);
		return  objectToValidate!=null && objectToValidate.length()>=minLenght;
	}

	@Override
	public String getErrorMessage(String fieldName, boolean isMaleGender) {
		return (isMaleGender?"El ":"La ")+fieldName+" tiene que ser de al menos "+minLenght+" caracteres.";
	}

}
