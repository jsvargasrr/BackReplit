package com.nubo.masterparticipant.api.application.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ApplicationUtils {

    static final String FORMAT_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";

    private ApplicationUtils() {
    }

    public static String getFormatDatetime() {
        return FORMAT_DATETIME;
    }

    public static String getLocalDateTimeNow() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(ApplicationUtils.getFormatDatetime()));
    }

    public static String convertObjectToJson(Object object) {
        return new Gson().toJson(object);
    }

    public static Object convertJsonToObject(String json, Object object) {
        return new Gson().fromJson(json, (Type) object);
    }

}

