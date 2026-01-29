package com.evokerking.evokerapi.core;

/**
 * Exception thrown when data binding fails.
 */
public class DataBindException extends Exception {
    public DataBindException(String message) {
        super(message);
    }
    
    public DataBindException(String message, Throwable cause) {
        super(message, cause);
    }
}
