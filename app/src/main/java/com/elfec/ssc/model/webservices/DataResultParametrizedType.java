package com.elfec.ssc.model.webservices;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by drodriguez on 02/12/2016.
 * DataResultParametrizedType
 */

public class DataResultParametrizedType implements ParameterizedType {

    private Type wrapped;

    public DataResultParametrizedType(Type wrapped) {
        this.wrapped = wrapped;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {wrapped};
    }

    public Type getRawType() {
        return DataResult.class;
    }

    public Type getOwnerType() {
        return null;
    }

}
