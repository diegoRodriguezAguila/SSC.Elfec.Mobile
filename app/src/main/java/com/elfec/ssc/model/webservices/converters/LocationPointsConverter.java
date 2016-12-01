package com.elfec.ssc.model.webservices.converters;

import com.elfec.ssc.helpers.utils.GsonUtils;
import com.elfec.ssc.model.LocationPoint;
import com.elfec.ssc.model.webservices.ResultConverter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationPointsConverter implements
        ResultConverter<List<LocationPoint>> {

    @Override
    public List<LocationPoint> convert(String result) {
        if (result == null)
            return new ArrayList<>();
        Gson gson = GsonUtils.generateGson();
        return Arrays.asList(gson.fromJson(result, LocationPoint[].class));
    }
}
