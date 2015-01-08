package com.elfec.ssc.model.validations;

/**
 * Valida si el tamaño de una cadena es válido, es decir si su longitud es menor o hasta igual que el tamaño
 * máximo establecido
 * @author Diego
 *
 */
public class MaxLenghtValidationRule implements IValidationRule<String> {

	private int maxLenght;
	
	public MaxLenghtValidationRule(int maxLenght) {
		this.maxLenght = maxLenght;
	}
	
	public int getMaxLenght() {
		return maxLenght;
	}
	public void setMaxLenght(int maxLenght) {
		this.maxLenght = maxLenght;
	}
	
	@Override
	public boolean IsValid(String objectToValidate) {
		return objectToValidate!=null && objectToValidate.length()<=maxLenght;
	}

}
