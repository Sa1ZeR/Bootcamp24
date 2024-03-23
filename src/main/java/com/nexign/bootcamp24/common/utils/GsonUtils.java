package com.nexign.bootcamp24.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GsonUtils {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Серbализует переданные объект в json
     * @param object объект на сериализацию
     * @return обхект в виде json строки
     */
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }
}
