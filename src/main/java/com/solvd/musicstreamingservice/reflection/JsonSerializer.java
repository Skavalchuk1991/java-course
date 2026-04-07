package com.solvd.musicstreamingservice.reflection;

import java.lang.reflect.Field;

/**
 * Simple JSON serializer that uses @JsonField annotation via reflection.
 */
public class JsonSerializer {

    public static String serialize(Object obj) {
        StringBuilder json = new StringBuilder("{");
        Field[] fields = obj.getClass().getDeclaredFields();
        boolean first = true;

        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonField.class)) {
                field.setAccessible(true);
                JsonField annotation = field.getAnnotation(JsonField.class);
                String key = annotation.name().isEmpty() ? field.getName() : annotation.name();
                try {
                    Object value = field.get(obj);
                    if (!first) json.append(", ");
                    json.append("\"").append(key).append("\": ");
                    if (value instanceof String) {
                        json.append("\"").append(value).append("\"");
                    } else {
                        json.append(value);
                    }
                    first = false;
                } catch (IllegalAccessException e) {
                    System.out.println("Cannot access field: " + field.getName());
                }
            }
        }
        json.append("}");
        return json.toString();
    }
}
