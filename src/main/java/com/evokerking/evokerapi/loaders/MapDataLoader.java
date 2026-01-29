package com.evokerking.evokerapi.loaders;

import com.evokerking.evokerapi.core.DataLoadException;
import com.evokerking.evokerapi.core.DataLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory data loader for programmatically constructed data.
 */
public class MapDataLoader implements DataLoader {
    private Map<String, Object> data;
    
    public MapDataLoader() {
        this.data = new HashMap<>();
    }
    
    public MapDataLoader(Map<String, Object> data) {
        this.data = new HashMap<>(data);
    }
    
    /**
     * Set data for this loader.
     * 
     * @param data The data map
     */
    public void setData(Map<String, Object> data) {
        this.data = new HashMap<>(data);
    }
    
    /**
     * Add a key-value pair to the data.
     * 
     * @param key The key
     * @param value The value
     */
    public void put(String key, Object value) {
        this.data.put(key, value);
    }
    
    @Override
    public Map<String, Object> load() throws DataLoadException {
        return new HashMap<>(data);
    }
    
    @Override
    public Map<String, Object> load(String source) throws DataLoadException {
        return load();
    }
}
