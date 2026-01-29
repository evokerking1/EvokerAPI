package com.evokerking.evokerapi.core;

/**
 * Exception thrown when data loading fails.
 */
public class DataLoadException extends Exception {
    public DataLoadException(String message) {
        super(message);
    }
    
    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
