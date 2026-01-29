package com.evokerking.evokerapi;

import com.evokerking.evokerapi.builders.DataBuilder;
import com.evokerking.evokerapi.core.DataBindException;
import com.evokerking.evokerapi.core.DataBinder;
import com.evokerking.evokerapi.core.DataLoadException;
import com.evokerking.evokerapi.factory.DataFactory;
import com.evokerking.evokerapi.loaders.FileDataLoader;
import com.evokerking.evokerapi.loaders.MapDataLoader;

import java.util.Map;

/**
 * Main entry point for the EvokerAPI framework.
 * Provides convenient static methods for common operations.
 * 
 * Example usage:
 * <pre>
 * // From file
 * Config config = EvokerAPI.loadFromFile(Config.class, "config.json");
 * 
 * // From map
 * Map&lt;String, Object&gt; data = Map.of("name", "test", "port", 8080);
 * Config config = EvokerAPI.loadFromMap(Config.class, data);
 * 
 * // Using builder
 * Config config = EvokerAPI.builder(Config.class)
 *     .with("name", "test")
 *     .with("port", 8080)
 *     .build();
 * </pre>
 */
public class EvokerAPI {
    private static final DataFactory factory = new DataFactory();
    private static final DataBinder binder = new DataBinder();
    
    /**
     * Load an object from a file.
     * Supports JSON, YAML, and properties files.
     * 
     * @param clazz The class to instantiate
     * @param filePath Path to the data file
     * @param <T> The type
     * @return New instance populated with data from file
     * @throws DataLoadException if loading fails
     * @throws DataBindException if binding fails
     */
    public static <T> T loadFromFile(Class<T> clazz, String filePath) 
            throws DataLoadException, DataBindException {
        return factory.createFromFile(clazz, filePath);
    }
    
    /**
     * Load an object from a map.
     * 
     * @param clazz The class to instantiate
     * @param data The data map
     * @param <T> The type
     * @return New instance populated with data
     * @throws DataBindException if binding fails
     */
    public static <T> T loadFromMap(Class<T> clazz, Map<String, Object> data) throws DataBindException {
        return binder.bind(clazz, data);
    }
    
    /**
     * Create a builder for the specified class.
     * 
     * @param clazz The class to build
     * @param <T> The type
     * @return New DataBuilder instance
     */
    public static <T> DataBuilder<T> builder(Class<T> clazz) {
        return DataBuilder.of(clazz);
    }
    
    /**
     * Get the data factory instance.
     * 
     * @return DataFactory instance
     */
    public static DataFactory getFactory() {
        return factory;
    }
    
    /**
     * Get the data binder instance.
     * 
     * @return DataBinder instance
     */
    public static DataBinder getBinder() {
        return binder;
    }
    
    /**
     * Create a new file data loader.
     * 
     * @return New FileDataLoader instance
     */
    public static FileDataLoader fileLoader() {
        return new FileDataLoader();
    }
    
    /**
     * Create a new map data loader.
     * 
     * @return New MapDataLoader instance
     */
    public static MapDataLoader mapLoader() {
        return new MapDataLoader();
    }
    
    /**
     * Create a new map data loader with initial data.
     * 
     * @param data Initial data
     * @return New MapDataLoader instance
     */
    public static MapDataLoader mapLoader(Map<String, Object> data) {
        return new MapDataLoader(data);
    }
}
