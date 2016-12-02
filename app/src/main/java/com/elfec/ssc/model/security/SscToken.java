package com.elfec.ssc.model.security;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Es la clase que contiene el token que permite llamar a los WS
 *
 * @author drodriguez
 */
public class SscToken {
    private String imei;
    private String token;

    public SscToken() {

    }

    public SscToken(String imei, String token) {
        this.imei = imei;
        this.token = token;
    }

    public String getImei() {
        return imei;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        try {
            return (new JSONObject().put("imei", imei).put("token", token)).toString();
        } catch (JSONException e) {
            return "{\"error\": \"invalid credentials\"}";
        }
    }
}
