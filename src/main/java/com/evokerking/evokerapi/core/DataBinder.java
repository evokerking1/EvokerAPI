package com.evokerking.evokerapi.core;

import com.evokerking.evokerapi.annotations.DataDriven;
import com.evokerking.evokerapi.annotations.DataField;
import com.evokerking.evokerapi.annotations.Required;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Core class for binding data from maps to Java objects.
 * Handles the conversion and population of @DataDriven annotated classes.
 */
public class DataBinder {
    
    /**
     * Bind data from a map to a new instance of the specified class.
     * 
     * @param clazz The class to instantiate and populate
     * @param data The data to bind
     * @param <T> The type of the class
     * @return A new instance populated with data
     * @throws DataBindException if binding fails
     */
    public <T> T bind(Class<T> clazz, Map<String, Object> data) throws DataBindException {
        if (data == null) {
            throw new DataBindException("Data map cannot be null");
        }
        
        if (!clazz.isAnnotationPresent(DataDriven.class)) {
            throw new DataBindException("Class " + clazz.getName() + " is not annotated with @DataDriven");
        }
        
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            DataDriven dataDriven = clazz.getAnnotation(DataDriven.class);
            String prefix = dataDriven.prefix();
            
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(DataField.class)) {
                    bindField(instance, field, data, prefix);
                }
            }
            
            return instance;
        } catch (Exception e) {
            throw new DataBindException("Failed to bind data to class " + clazz.getName(), e);
        }
    }
    
    /**
     * Bind data to an existing instance.
     * 
     * @param instance The instance to populate
     * @param data The data to bind
     * @param <T> The type of the instance
     * @throws DataBindException if binding fails
     */
    public <T> void bind(T instance, Map<String, Object> data) throws DataBindException {
        Class<?> clazz = instance.getClass();
        if (!clazz.isAnnotationPresent(DataDriven.class)) {
            throw new DataBindException("Class " + clazz.getName() + " is not annotated with @DataDriven");
        }
        
        try {
            DataDriven dataDriven = clazz.getAnnotation(DataDriven.class);
            String prefix = dataDriven.prefix();
            
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(DataField.class)) {
                    bindField(instance, field, data, prefix);
                }
            }
        } catch (Exception e) {
            throw new DataBindException("Failed to bind data to instance", e);
        }
    }
    
    private void bindField(Object instance, Field field, Map<String, Object> data, String prefix) 
            throws IllegalAccessException, DataBindException {
        DataField dataField = field.getAnnotation(DataField.class);
        Required required = field.getAnnotation(Required.class);
        
        // Determine the key to look up in the data map
        String fieldName = dataField.name().isEmpty() ? field.getName() : dataField.name();
        if (!prefix.isEmpty()) {
            fieldName = prefix + "." + fieldName;
        }
        
        Object value = getNestedValue(data, fieldName);
        
        // Handle required fields
        if (value == null && required != null) {
            String message = required.message().isEmpty() 
                ? "Required field '" + fieldName + "' is missing"
                : required.message();
            throw new DataBindException(message);
        }
        
        // Use default value if provided and value is null
        if (value == null && !dataField.defaultValue().isEmpty()) {
            value = convertValue(dataField.defaultValue(), field.getType());
        }
        
        // Set the field value
        if (value != null) {
            field.setAccessible(true);
            try {
                field.set(instance, convertValue(value, field.getType()));
            } catch (IllegalAccessException e) {
                throw new DataBindException("Cannot access field '" + field.getName() + 
                    "'. Consider making the field package-private or providing a setter method.", e);
            }
        }
    }
    
    private Object getNestedValue(Map<String, Object> data, String key) {
        if (data.containsKey(key)) {
            return data.get(key);
        }
        
        // Handle nested keys like "server.port"
        String[] parts = key.split("\\.");
        Object current = data;
        
        for (String part : parts) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(part);
                if (current == null) {
                    return null;
                }
            } else {
                return null;
            }
        }
        
        return current;
    }
    
    private Object convertValue(Object value, Class<?> targetType) throws DataBindException {
        if (value == null) {
            return null;
        }
        
        // If types match, return as-is
        if (targetType.isAssignableFrom(value.getClass())) {
            return value;
        }
        
        // Handle Number to Number conversions (important for JSON parsing)
        if (value instanceof Number) {
            Number num = (Number) value;
            if (targetType == int.class || targetType == Integer.class) {
                return num.intValue();
            } else if (targetType == long.class || targetType == Long.class) {
                return num.longValue();
            } else if (targetType == double.class || targetType == Double.class) {
                return num.doubleValue();
            } else if (targetType == float.class || targetType == Float.class) {
                return num.floatValue();
            } else if (targetType == short.class || targetType == Short.class) {
                return num.shortValue();
            } else if (targetType == byte.class || targetType == Byte.class) {
                return num.byteValue();
            }
        }
        
        // Convert from string
        String strValue = value.toString();
        
        try {
            if (targetType == int.class || targetType == Integer.class) {
                // Handle both integer strings and decimal strings
                if (strValue.contains(".")) {
                    return (int) Double.parseDouble(strValue);
                }
                return Integer.parseInt(strValue);
            } else if (targetType == long.class || targetType == Long.class) {
                if (strValue.contains(".")) {
                    return (long) Double.parseDouble(strValue);
                }
                return Long.parseLong(strValue);
            } else if (targetType == double.class || targetType == Double.class) {
                return Double.parseDouble(strValue);
            } else if (targetType == float.class || targetType == Float.class) {
                return Float.parseFloat(strValue);
            } else if (targetType == boolean.class || targetType == Boolean.class) {
                return Boolean.parseBoolean(strValue);
            } else if (targetType == String.class) {
                return strValue;
            } else if (targetType.isEnum()) {
                @SuppressWarnings("unchecked")
                Class<Enum> enumType = (Class<Enum>) targetType;
                return Enum.valueOf(enumType, strValue);
            }
        } catch (Exception e) {
            throw new DataBindException("Cannot convert value '" + value + "' to type " + targetType.getName(), e);
        }
        
        throw new DataBindException("Unsupported type conversion to " + targetType.getName());
    }
}
