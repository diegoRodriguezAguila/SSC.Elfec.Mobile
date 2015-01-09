package com.elfec.ssc.model.validations;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ValidationRulesFactory {

	private static Hashtable<String,Class<?>> registeredValidations = new Hashtable<String, Class<?>>();
	static
	{
		registeredValidations.put("MaxLenght", MaxLenghtValidationRule.class);
		registeredValidations.put("MinLenght", MinLenghtValidationRule.class);
		registeredValidations.put("NotNullOrEmpty", NotNullOrEmptyValidationRule.class);
		registeredValidations.put("Numeric", NumericValidationRule.class);
	}
	
	public static IValidationRule<?> getValidationRule(String key)
	{
		try {
			return (IValidationRule<?>) registeredValidations.get(key).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Se debe llamar a esta función antes de llamar a getValidationParams, crea las reglas de validación a partir de una cadena
	 * @param validationRules
	 * @return las reglas de validación y sus parámetros en una clase Wrapper ValidationsAndParams
	 */
	@SuppressWarnings("unchecked")
	public static <T> ValidationsAndParams<T> createValidationRulesWithParams(String validationRules)
	{
		List<IValidationRule<T>> objectValidationRules = new ArrayList<IValidationRule<T>>();
		validationRules = validationRules.replace(" ", "");
		String[] rules = validationRules.split(";");
		String[] params = new String[rules.length];
		int indParams;
		String param;
		for (int i = 0; i < rules.length; i++) {
			indParams = rules[i].indexOf(',');
			param = null;
			if(indParams!=-1)
			{
				param = rules[i].substring(indParams+1, rules[i].length());
				rules[i] = rules[i].substring(0, indParams);
			}
			params[i] = param;
			objectValidationRules.add( (IValidationRule<T>) getValidationRule(rules[i]));
		}
		return new ValidationsAndParams<T>(objectValidationRules, params);
		
	}
}
