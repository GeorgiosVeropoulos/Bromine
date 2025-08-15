package json;

import json.annotations.JsonName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

public class JsonMapper<T> {
    private final Class<T> type;

    private JsonMapper(Class<T> type) {
        this.type = type;
    }

    public static <T> JsonMapper<T> of(Class<T> clazz) {
        return new JsonMapper<>(clazz);
    }

    public List<T> fromArray(List<Object> jsonArray) {
        try {
            List<T> resultList = new ArrayList<>();

            for (Object item : jsonArray) {
                if (item instanceof Map) {
                    try {
                        // Map the JSON object to an instance of T
                        T instance;
                        try {
                            instance = fromMap((LinkedHashMap<String, Object>) item);
                        } catch (ClassCastException e0) {
                            throw new ClassCastException("Class cast exception");
                        }
                        resultList.add(instance);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to map JSON object: " + item, e);
                    }
                } else {
                    throw new RuntimeException("Unexpected item type in JSON array: " + item.getClass());
                }
            }

            return resultList;
        } catch (Exception e) {
            throw new RuntimeException("Failed to map JSON array to List<" + type.getSimpleName() + ">", e);
        }
    }

//    public T fromMap(LinkedHashMap<String, Object> jsonMap) {
//        try {
//            T instance = type.getDeclaredConstructor().newInstance();
//
//            List<Field> allFields = new ArrayList<>();
//            Class<?> current = type;
//            while (current != null && current != Object.class) {
//                allFields.addAll(Arrays.asList(current.getDeclaredFields()));
//                current = current.getSuperclass();
//            }
//
//            for (Field field : allFields) {
//                field.setAccessible(true);
//
//                // Determine the field name to use, checking for the @JsonName annotation
//                String jsonFieldName = getJsonFieldName(field);
//
//                // Retrieve the value from the map using the determined field name
//                Object value = jsonMap.get(jsonFieldName);
//                if (value == null) continue;
//
//                Class<?> fieldType = field.getType();
//
//                if (Map.class.isAssignableFrom(fieldType) && value instanceof Map) {
//
//                    Type genericType = field.getGenericType();
//
//                    ParameterizedType mapType = null;
//                    Class<?> keyClass = null;
//                    Type valueType = null;
//
//                    if (genericType instanceof ParameterizedType) {
//                        // Normal case: Map<String, Build> or LinkedHashMap<String, Build>
//                        mapType = (ParameterizedType) genericType;
//                        keyClass = (Class<?>) mapType.getActualTypeArguments()[0];
//                        valueType = mapType.getActualTypeArguments()[1];
//                    } else {
//                        // Fallback: field is a subclass of LinkedHashMap but no generic info here
//                        Type superType = fieldType.getGenericSuperclass();
//
//                        if (superType instanceof ParameterizedType) {
//                            mapType = (ParameterizedType) superType;
//                            keyClass = (Class<?>) mapType.getActualTypeArguments()[0];
//                            valueType = mapType.getActualTypeArguments()[1];
//                        } else {
//                            // Cannot resolve generic key/value types - fallback
//                            keyClass = Object.class;
//                            valueType = Object.class;
//                        }
//                    }
//
//                    Map<?, ?> rawMap = (Map<?, ?>) value;
//                    Map<Object, Object> resultMap = new LinkedHashMap<>();
//
//                    for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
//                        Object mapKey = entry.getKey(); // you may want to convert to keyClass type here if needed
//                        Object rawVal = entry.getValue();
//
//                        if (rawVal instanceof List && valueType instanceof ParameterizedType) {
//                            ParameterizedType listType = (ParameterizedType) valueType;
//                            Class<?> listElementType = (Class<?>) listType.getActualTypeArguments()[0];
//
//                            List<Object> typedList = new ArrayList<>();
//                            for (Object item : (List<?>) rawVal) {
//                                if (item instanceof Map) {
//                                    typedList.add(JsonMapper.of(listElementType).fromMap((LinkedHashMap<String, Object>) item));
//                                } else {
//                                    typedList.add(item);
//                                }
//                            }
//                            resultMap.put(mapKey, typedList);
//                        } else if (rawVal instanceof Map && valueType instanceof Class) {
//                            resultMap.put(mapKey, JsonMapper.of((Class<?>) valueType).fromMap((LinkedHashMap<String, Object>) rawVal));
//                        } else {
//                            resultMap.put(mapKey, rawVal);
//                        }
//                    }
//
//                    // Now, **if the field type is a subclass of Map (like Builds), instantiate that subclass and set it**
//                    if (!fieldType.equals(LinkedHashMap.class) && Map.class.isAssignableFrom(fieldType)) {
//                        // Try to create an instance of the subclass
//                        try {
//                            Map<Object, Object> typedInstance = (Map<Object, Object>) fieldType.getDeclaredConstructor().newInstance();
//                            typedInstance.putAll(resultMap);
//                            field.set(instance, typedInstance);
//                        } catch (Exception e) {
//                            // Fallback: set LinkedHashMap if subclass instantiation fails
//                            field.set(instance, resultMap);
//                        }
//                    } else {
//                        // Field is just Map or LinkedHashMap, set directly
//                        field.set(instance, resultMap);
//                    }
//                }
//                else if (List.class.isAssignableFrom(fieldType) && value instanceof List) {
//                    ParameterizedType listType = (ParameterizedType) field.getGenericType();
//                    Class<?> elementType = (Class<?>) listType.getActualTypeArguments()[0];
//
//                    List<Object> typedList = new ArrayList<>();
//                    for (Object item : (List<?>) value) {
//                        if (item instanceof Map) {
//                            typedList.add(JsonMapper.of(elementType).fromMap((LinkedHashMap<String, Object>) item));
//                        } else {
//                            typedList.add(item);
//                        }
//                    }
//                    field.set(instance, typedList);
//
//                } else if (value instanceof Map && !isPrimitiveOrWrapper(fieldType)) {
//                    field.set(instance, JsonMapper.of(fieldType).fromMap((LinkedHashMap<String, Object>) value));
//                } else {
//                    field.set(instance, value);
//                }
//            }
//
//            return instance;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to map JSON to " + type.getSimpleName(), e);
//        }
//    }

    public T fromMap(LinkedHashMap<String, Object> jsonMap) {
        try {
            // Create an instance of the target type
            T instance = type.getDeclaredConstructor().newInstance();

            // Collect all fields from the class hierarchy
            List<Field> allFields = new ArrayList<>();
            Class<?> current = type;
            while (current != null && current != Object.class) {
                allFields.addAll(Arrays.asList(current.getDeclaredFields()));
                current = current.getSuperclass();
            }

            // Process each field
            for (Field field : allFields) {

                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }
                field.setAccessible(true);

                // Determine the JSON field name (considering @JsonName annotation)
                String jsonFieldName = getJsonFieldName(field);

                // Retrieve the value from the JSON map
                Object value = jsonMap.get(jsonFieldName);
                if (value == null) continue;

                Class<?> fieldType = field.getType();

                // Handle type conversion for primitive types and wrappers
                if (fieldType == Integer.class || fieldType == int.class) {
                    field.set(instance, convertToInteger(value));
                } else if (fieldType == Long.class || fieldType == long.class) {
                    field.set(instance, convertToLong(value));
                } else if (fieldType == Double.class || fieldType == double.class) {
                    field.set(instance, convertToDouble(value));
                } else if (fieldType == Boolean.class || fieldType == boolean.class) {
                    field.set(instance, convertToBoolean(value));
                }
                // Handle Map fields
                else if (Map.class.isAssignableFrom(fieldType) && value instanceof Map) {
                    handleMapField(instance, field, value);
                }
                // Handle List fields
                else if (List.class.isAssignableFrom(fieldType) && value instanceof List) {
                    handleListField(instance, field, value);
                }
                // Handle nested objects
                else if (value instanceof Map && !isPrimitiveOrWrapper(fieldType)) {
                    field.set(instance, JsonMapper.of(fieldType).fromMap((LinkedHashMap<String, Object>) value));
                }
                // Handle primitive or directly assignable types
                else {
                    field.set(instance, value);
                }
            }

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to map JSON to " + type.getSimpleName(), e);
        }
    }

    // Helper methods for type conversion
    private Integer convertToInteger(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        } else if (value instanceof String) {
            return Integer.parseInt((String) value);
        } else if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        throw new IllegalArgumentException("Cannot convert value to Integer: " + value);
    }

    private Long convertToLong(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        } else if (value instanceof String) {
            return Long.parseLong((String) value);
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        throw new IllegalArgumentException("Cannot convert value to Long: " + value);
    }

    private Double convertToDouble(Object value) {
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).doubleValue();
        } else if (value instanceof String) {
            return Double.parseDouble((String) value);
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        throw new IllegalArgumentException("Cannot convert value to Double: " + value);
    }

    private Boolean convertToBoolean(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        throw new IllegalArgumentException("Cannot convert value to Boolean: " + value);
    }

    private void handleMapField(T instance, Field field, Object value) throws Exception {
        Class<?> fieldType = field.getType();
        Type genericType = field.getGenericType();

        ParameterizedType mapType = null;
        Class<?> keyClass = null;
        Type valueType = null;

        if (genericType instanceof ParameterizedType) {
            mapType = (ParameterizedType) genericType;
            keyClass = (Class<?>) mapType.getActualTypeArguments()[0];
            valueType = mapType.getActualTypeArguments()[1];
        } else {
            // Fallback for non-parameterized Map types
            keyClass = Object.class;
            valueType = Object.class;
        }

        Map<?, ?> rawMap = (Map<?, ?>) value;
        Map<Object, Object> resultMap = new LinkedHashMap<>();

        for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
            Object mapKey = entry.getKey();
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

        if (!fieldType.equals(LinkedHashMap.class) && Map.class.isAssignableFrom(fieldType)) {
            try {
                Map<Object, Object> typedInstance = (Map<Object, Object>) fieldType.getDeclaredConstructor().newInstance();
                typedInstance.putAll(resultMap);
                field.set(instance, typedInstance);
            } catch (Exception e) {
                field.set(instance, resultMap);
            }
        } else {
            field.set(instance, resultMap);
        }
    }

    private void handleListField(T instance, Field field, Object value) throws Exception {
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
