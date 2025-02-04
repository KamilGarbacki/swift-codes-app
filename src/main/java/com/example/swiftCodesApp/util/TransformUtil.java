package com.example.swiftCodesApp.util;

import java.lang.reflect.Field;

public class TransformUtil {

    public static void allObjectFieldsToUpperCase(Object obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (!field.getType().equals(String.class)) {
                continue;
            }

            field.setAccessible(true);
            String val = (String) field.get(obj);

            if (val != null) {
                field.set(obj, val.toUpperCase());
            }
        }
    }
}
