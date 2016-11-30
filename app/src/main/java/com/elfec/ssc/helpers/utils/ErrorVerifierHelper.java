package com.elfec.ssc.helpers.utils;

import com.elfec.ssc.model.exceptions.OutdatedAppException;

import java.util.List;

/**
 * Clase que se encarga de verificar ciertas operaciones sobre errores
 */
public class ErrorVerifierHelper {
    /**
     * Verifica si alguno de los errores contiene el error de versión
     * de aplicación no actualizada
     * @param errors errores
     * @return true si es que contiene ese tipo de error
     */
    public static boolean isOutdatedApp(List<Exception> errors){
        for(Exception e : errors){
            if(e instanceof OutdatedAppException)
                return true;
        }
        return false;
    }
}
