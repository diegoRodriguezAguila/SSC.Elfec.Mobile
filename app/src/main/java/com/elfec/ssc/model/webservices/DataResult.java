package com.elfec.ssc.model.webservices;

import com.elfec.ssc.model.exceptions.ApiException;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Diego on 13/8/2016.
 * Describes a web service result with a data field
 */
public class DataResult<T>{
    @SerializedName("Response")
    protected T data;

    @SerializedName("Errors")
    private List<ApiException> errors;

    public DataResult() {
    }

    //region getter setters

    public boolean hasErrors() {
        return errors != null && errors.size() > 0;
    }

    public List<ApiException> getErrors() {
        return errors;
    }

    public void setErrors(List<ApiException> errors) {
        this.errors = errors;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
    //endregion
}

