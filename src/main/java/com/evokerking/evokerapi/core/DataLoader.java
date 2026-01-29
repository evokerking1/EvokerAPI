package com.evokerking.evokerapi.core;

import java.util.Map;

/**
 * Interface for loading data from various sources.
 * Implementations can load data from files, databases, network, etc.
 */
public interface DataLoader {
    /**
     * Load data from the source and return as a map.
     * 
     * @return Map containing the loaded data with keys and values
     * @throws DataLoadException if data cannot be loaded
     */
    Map<String, Object> load() throws DataLoadException;
    
    /**
     * Load data from a specific source location.
     * 
     * @param source The source identifier (e.g., file path, URL)
     * @return Map containing the loaded data
     * @throws DataLoadException if data cannot be loaded
     */
    Map<String, Object> load(String source) throws DataLoadException;
}
