package com.evokerking.evokerapi.factory;

import com.evokerking.evokerapi.core.DataBindException;
import com.evokerking.evokerapi.core.DataBinder;
import com.evokerking.evokerapi.core.DataLoadException;
import com.evokerking.evokerapi.core.DataLoader;
import com.evokerking.evokerapi.loaders.FileDataLoader;

import java.util.Map;

/**
 * Factory for creating data-driven objects from various sources.
 * Provides convenient methods to load and bind data in one step.
 */
public class DataFactory {
    private final DataBinder binder;
    
    public DataFactory() {
        this.binder = new DataBinder();
    }
    
    /**
     * Create an instance from a data loader.
     * 
     * @param clazz The class to instantiate
     * @param loader The data loader
     * @param <T> The type
     * @return New instance populated with data
     * @throws DataLoadException if loading fails
     * @throws DataBindException if binding fails
     */
    public <T> T create(Class<T> clazz, DataLoader loader) throws DataLoadException, DataBindException {
        Map<String, Object> data = loader.load();
        return binder.bind(clazz, data);
    }
    
    /**
     * Create an instance from a file.
     * 
     * @param clazz The class to instantiate
     * @param filePath Path to the data file
     * @param <T> The type
     * @return New instance populated with data
     * @throws DataLoadException if loading fails
     * @throws DataBindException if binding fails
     */
    public <T> T createFromFile(Class<T> clazz, String filePath) throws DataLoadException, DataBindException {
        FileDataLoader loader = new FileDataLoader();
        return create(clazz, loader, filePath);
    }
    
    /**
     * Create an instance from a data loader with source.
     * 
     * @param clazz The class to instantiate
     * @param loader The data loader
     * @param source The source identifier
     * @param <T> The type
     * @return New instance populated with data
     * @throws DataLoadException if loading fails
     * @throws DataBindException if binding fails
     */
    public <T> T create(Class<T> clazz, DataLoader loader, String source) 
            throws DataLoadException, DataBindException {
        Map<String, Object> data = loader.load(source);
        return binder.bind(clazz, data);
    }
    
    /**
     * Create an instance from a data map.
     * 
     * @param clazz The class to instantiate
     * @param data The data map
     * @param <T> The type
     * @return New instance populated with data
     * @throws DataBindException if binding fails
     */
    public <T> T createFromMap(Class<T> clazz, Map<String, Object> data) throws DataBindException {
        return binder.bind(clazz, data);
    }
}
