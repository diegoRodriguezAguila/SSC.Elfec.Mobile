package com.elfec.ssc.model.webservices;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Diego on 13/8/2016.
 * Describes a web service result with a data field
 */
public class DataResult<T> {
    @SerializedName("Errors")
    private List<Exception> errors;
    @SerializedName("Response")
    private T data;

    public boolean hasErrors() {
        return errors != null && errors.size() > 0;
    }

    public List<Exception> getErrors() {
        return errors;
    }

    public void setErrors(List<Exception> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

