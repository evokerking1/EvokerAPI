package com.evokerking.evokerapi.builders;

import com.evokerking.evokerapi.core.DataBindException;
import com.evokerking.evokerapi.core.DataBinder;
import com.evokerking.evokerapi.core.DataLoadException;
import com.evokerking.evokerapi.core.DataLoader;
import com.evokerking.evokerapi.loaders.FileDataLoader;
import com.evokerking.evokerapi.loaders.MapDataLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * Fluent builder for creating data-driven objects.
 * Provides a convenient API for configuring and building objects.
 * 
 * Example:
 * <pre>
 * Config config = DataBuilder.of(Config.class)
 *     .with("hostname", "localhost")
 *     .with("port", 8080)
 *     .build();
 * </pre>
 */
public class DataBuilder<T> {
    private final Class<T> clazz;
    private final Map<String, Object> data;
    private DataLoader loader;
    private String source;
    
    private DataBuilder(Class<T> clazz) {
        this.clazz = clazz;
        this.data = new HashMap<>();
    }
    
    /**
     * Create a new builder for the specified class.
     * 
     * @param clazz The class to build
     * @param <T> The type
     * @return New DataBuilder instance
     */
    public static <T> DataBuilder<T> of(Class<T> clazz) {
        return new DataBuilder<>(clazz);
    }
    
    /**
     * Add a data field.
     * 
     * @param key The field name
     * @param value The field value
     * @return This builder
     */
    public DataBuilder<T> with(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
    
    /**
     * Add multiple data fields from a map.
     * 
     * @param data The data map
     * @return This builder
     */
    public DataBuilder<T> withData(Map<String, Object> data) {
        this.data.putAll(data);
        return this;
    }
    
    /**
     * Use a data loader.
     * 
     * @param loader The data loader
     * @return This builder
     */
    public DataBuilder<T> from(DataLoader loader) {
        this.loader = loader;
        return this;
    }
    
    /**
     * Load from a file.
     * 
     * @param filePath The file path
     * @return This builder
     */
    public DataBuilder<T> fromFile(String filePath) {
        this.loader = new FileDataLoader();
        this.source = filePath;
        return this;
    }
    
    /**
     * Build the object.
     * 
     * @return New instance populated with data
     * @throws DataBindException if binding fails
     * @throws DataLoadException if loading fails
     */
    public T build() throws DataBindException, DataLoadException {
        DataBinder binder = new DataBinder();
        Map<String, Object> finalData = new HashMap<>(data);
        
        // Load data from loader if specified
        if (loader != null) {
            Map<String, Object> loadedData = source != null ? loader.load(source) : loader.load();
            // Merge loaded data with manually added data (manual data takes precedence)
            loadedData.putAll(finalData);
            finalData = loadedData;
        }
        
        return binder.bind(clazz, finalData);
    }
    
    /**
     * Build the object without throwing checked exceptions.
     * Wraps exceptions in RuntimeException.
     * 
     * @return New instance populated with data
     * @throws RuntimeException if building fails
     */
    public T buildUnchecked() {
        try {
            return build();
        } catch (DataBindException | DataLoadException e) {
            throw new RuntimeException("Failed to build object", e);
        }
    }
}
