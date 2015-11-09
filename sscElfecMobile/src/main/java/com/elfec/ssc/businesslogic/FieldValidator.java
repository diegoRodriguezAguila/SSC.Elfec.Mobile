package com.elfec.ssc.businesslogic;

import com.elfec.ssc.model.validations.IValidationRule;

import java.util.ArrayList;
import java.util.List;

/**
 * Se encarga de validar un campo con las reglas y parametros de validación definidos
 * @author Diego
 *
 */
public class FieldValidator{

	/**
	 * Realiza la validaci�n de un campo y recopila los mensajes de error arrojados por cada una de las validaciones
	 * @param fieldName
	 * @param isMaleGender
	 * @param fieldValue
	 * @param validationRules
	 * @param validationParams
	 * @return
	 */
	public static <T> List<String> validate(String fieldName, boolean isMaleGender, T fieldValue, List<IValidationRule<T>> validationRules, String[] validationParams)
	{
		List<String> validationErrors = new ArrayList<String>();
		int size = validationRules.size();
		for (int i = 0; i < size; i++) {
			if(!validationRules.get(i).isValid(fieldValue, validationParams[i]!=null? validationParams[i].split(","):null))
			{
				validationErrors.add(validationRules.get(i).getErrorMessage(fieldName, isMaleGender));
			}
		} 
		return validationErrors;
	}
}
