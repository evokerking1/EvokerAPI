package com.evokerking.evokerapi.content.registry;

import com.evokerking.evokerapi.EvokerAPI;
import com.evokerking.evokerapi.core.DataBindException;
import com.evokerking.evokerapi.core.DataLoadException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Helper class for loading multiple content entries into a registry.
 * Supports batch loading from JSON arrays or maps.
 */
public class ContentLoader {
    
    /**
     * Load items from a JSON file containing an array of objects.
     * Each object should have an "id" field for registry.
     * 
     * @param registry The registry to populate
     * @param clazz The class type
     * @param filePath Path to JSON file
     * @param <T> The content type
     * @return Number of entries loaded
     * @throws DataLoadException if loading fails
     * @throws DataBindException if binding fails
     */
    public static <T> int loadFromArrayFile(DataRegistry<T> registry, Class<T> clazz, String filePath) 
            throws DataLoadException, DataBindException {
        Map<String, Object> data = EvokerAPI.fileLoader().load(filePath);
        
        // Check if data contains an array field
        Object items = data.get("items");
        if (items == null) {
            items = data.get("entries");
        }
        if (items == null) {
            items = data.get("content");
        }
        
        if (!(items instanceof List)) {
            throw new DataLoadException("File must contain an 'items', 'entries', or 'content' array");
        }
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) items;
        
        int count = 0;
        for (Map<String, Object> itemData : itemList) {
            T entry = EvokerAPI.loadFromMap(clazz, itemData);
            String id = extractId(entry);
            if (id != null) {
                registry.registerOrReplace(id, entry);
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Load a single entry and register it.
     * 
     * @param registry The registry to populate
     * @param clazz The class type
     * @param filePath Path to JSON file
     * @param <T> The content type
     * @throws DataLoadException if loading fails
     * @throws DataBindException if binding fails
     */
    public static <T> void loadSingle(DataRegistry<T> registry, Class<T> clazz, String filePath) 
            throws DataLoadException, DataBindException {
        T entry = EvokerAPI.loadFromFile(clazz, filePath);
        String id = extractId(entry);
        if (id != null) {
            registry.registerOrReplace(id, entry);
        } else {
            throw new DataLoadException("Entry does not have an 'id' field");
        }
    }
    
    /**
     * Extract the ID from an entry using reflection.
     * Looks for a getId() method.
     * 
     * @param entry The entry
     * @param <T> The type
     * @return The ID, or null if not found
     */
    private static <T> String extractId(T entry) {
        try {
            Method getIdMethod = entry.getClass().getMethod("getId");
            Object id = getIdMethod.invoke(entry);
            return id != null ? id.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }
}
