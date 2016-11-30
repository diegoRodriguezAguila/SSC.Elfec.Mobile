package com.elfec.ssc.model.validations;

import java.util.List;

/**
 * Wraper class para las reglas de validación junto con sus parametros
 * @author Diego
 *
 * @param <T>
 */
public class ValidationsAndParams<T> {

	private List<IValidationRule<T>> validationRules;
	private String[] params;
	
	public ValidationsAndParams(List<IValidationRule<T>> validationRules,
			String[] params) {
		super();
		this.validationRules = validationRules;
		this.params = params;
	}

	public List<IValidationRule<T>> getValidationRules() {
		return validationRules;
	}

	public String[] getParams() {
		return params;
	}
	
	
}
