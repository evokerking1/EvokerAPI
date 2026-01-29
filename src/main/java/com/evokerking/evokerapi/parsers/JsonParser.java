package com.evokerking.evokerapi.parsers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Parser for JSON data using Gson.
 */
public class JsonParser {
    private final Gson gson;
    
    public JsonParser() {
        this.gson = new Gson();
    }
    
    /**
     * Parse JSON string to a map.
     * 
     * @param json The JSON string
     * @return Map containing the parsed data
     */
    public Map<String, Object> parse(String json) {
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(json, type);
    }
    
    /**
     * Convert object to JSON string.
     * 
     * @param obj The object to convert
     * @return JSON string representation
     */
    public String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
