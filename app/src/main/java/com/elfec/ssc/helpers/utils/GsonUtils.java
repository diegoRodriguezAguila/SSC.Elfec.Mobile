package com.elfec.ssc.helpers.utils;

import com.elfec.ssc.model.webservices.serializers.DateTimeConverter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.lang.ref.SoftReference;
import java.lang.reflect.Modifier;

/**
 * Created by Diego on 21/8/2016.
 * Utils for gson
 */
public class GsonUtils {
    /**
     * Gson caché
     */
    private static SoftReference<Gson> sGsonCache;

    public static Gson generateGson() {
        if (sGsonCache == null || sGsonCache.get() == null)
            sGsonCache = new SoftReference<>(new GsonBuilder().setFieldNamingPolicy(
                    FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .registerTypeAdapter(DateTime.class, new DateTimeConverter())
                    .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                    .create());
        return sGsonCache.get();
    }
}
