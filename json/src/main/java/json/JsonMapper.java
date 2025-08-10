package json;

import json.annotations.JsonName;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class JsonMapper<T> {
    private final Class<T> type;

    private JsonMapper(Class<T> type) {
        this.type = type;
    }

    public static <T> JsonMapper<T> of(Class<T> clazz) {
        return new JsonMapper<>(clazz);
    }

    public T fromMap(LinkedHashMap<String, Object> jsonMap) {
        try {
            T instance = type.getDeclaredConstructor().newInstance();

            List<Field> allFields = new ArrayList<>();
            Class<?> current = type;
            while (current != null && current != Object.class) {
                allFields.addAll(Arrays.asList(current.getDeclaredFields()));
                current = current.getSuperclass();
            }

            for (Field field : allFields) {
                field.setAccessible(true);

                // Determine the field name to use, checking for the @JsonName annotation
                String jsonFieldName = getJsonFieldName(field);

                // Retrieve the value from the map using the determined field name
                Object value = jsonMap.get(jsonFieldName);
                if (value == null) continue;

                Class<?> fieldType = field.getType();

                if (Map.class.isAssignableFrom(fieldType) && value instanceof Map) {
                    ParameterizedType mapType = (ParameterizedType) field.getGenericType();
                    Class<?> keyType = (Class<?>) mapType.getActualTypeArguments()[0];
                    Type valueType = mapType.getActualTypeArguments()[1];

                    Map<?, ?> rawMap = (Map<?, ?>) value;
                    Map<Object, Object> resultMap = new LinkedHashMap<>();

                    for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
                        Object mapKey = entry.getKey(); // assume String
                        Object rawVal = entry.getValue();

                        if (rawVal instanceof List && valueType instanceof ParameterizedType) {
                            ParameterizedType listType = (ParameterizedType) valueType;
                            Class<?> listElementType = (Class<?>) listType.getActualTypeArguments()[0];

                            List<Object> typedList = new ArrayList<>();
                            for (Object item : (List<?>) rawVal) {
                                if (item instanceof Map) {
                                    typedList.add(JsonMapper.of(listElementType).fromMap((LinkedHashMap<String, Object>) item));
                                } else {
                                    typedList.add(item);
                                }
                            }
                            resultMap.put(mapKey, typedList);
                        } else if (rawVal instanceof Map && valueType instanceof Class) {
                            resultMap.put(mapKey, JsonMapper.of((Class<?>) valueType).fromMap((LinkedHashMap<String, Object>) rawVal));
                        } else {
                            resultMap.put(mapKey, rawVal);
                        }
                    }

                    field.set(instance, resultMap);

                } else if (List.class.isAssignableFrom(fieldType) && value instanceof List) {
                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
                    Class<?> elementType = (Class<?>) listType.getActualTypeArguments()[0];

                    List<Object> typedList = new ArrayList<>();
                    for (Object item : (List<?>) value) {
                        if (item instanceof Map) {
                            typedList.add(JsonMapper.of(elementType).fromMap((LinkedHashMap<String, Object>) item));
                        } else {
                            typedList.add(item);
                        }
                    }
                    field.set(instance, typedList);

                } else if (value instanceof Map && !isPrimitiveOrWrapper(fieldType)) {
                    field.set(instance, JsonMapper.of(fieldType).fromMap((LinkedHashMap<String, Object>) value));
                } else {
                    field.set(instance, value);
                }
            }

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to map JSON to " + type.getSimpleName(), e);
        }
    }

    private boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz == String.class
                || clazz == Integer.class
                || clazz == Double.class
                || clazz == Boolean.class
                || clazz == Long.class
                || clazz == Float.class
                || clazz == Short.class
                || clazz == Byte.class
                || clazz == Character.class;
    }

    // Check if the field has the @JsonName annotation, and return its value if present
    private String getJsonFieldName(Field field) {
        // Check if the field has the @JsonName annotation
        JsonName jsonNameAnnotation = field.getAnnotation(JsonName.class);
        if (jsonNameAnnotation != null) {
            return jsonNameAnnotation.value();
        }
        // If @JsonName is not present, return the field's name as the JSON key
        return field.getName();
    }
}
