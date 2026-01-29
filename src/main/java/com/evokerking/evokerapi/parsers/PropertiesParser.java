package com.evokerking.evokerapi.parsers;

import java.io.IOException;
import java.io.StringReader;
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
     * 
     * @param data The data map
     * @return Properties format string
     */
    public String toProperties(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        return sb.toString();
    }
}
