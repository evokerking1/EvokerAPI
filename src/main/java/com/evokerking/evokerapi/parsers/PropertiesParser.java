package com.evokerking.evokerapi.parsers;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Parser for properties files.
 */
public class PropertiesParser {
    
    /**
     * Parse properties string to a map.
     * 
     * @param propertiesContent The properties string
     * @return Map containing the parsed data
     * @throws IOException if parsing fails
     */
    public Map<String, Object> parse(String propertiesContent) throws IOException {
        Properties properties = new Properties();
        properties.load(new StringReader(propertiesContent));
        
        Map<String, Object> map = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            map.put(key, properties.getProperty(key));
        }
        
        return map;
    }
    
    /**
     * Convert map to properties format string.
     * Uses proper escaping for special characters.
     * 
     * @param data The data map
     * @return Properties format string
     */
    public String toProperties(Map<String, Object> data) {
        Properties properties = new Properties();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            properties.setProperty(entry.getKey(), String.valueOf(entry.getValue()));
        }
        
        try (StringWriter writer = new StringWriter()) {
            properties.store(writer, null);
            return writer.toString();
        } catch (IOException e) {
            // StringWriter doesn't actually throw IOException, but store() declares it
            throw new RuntimeException("Failed to convert map to properties", e);
        }
    }
}
