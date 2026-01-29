package com.evokerking.evokerapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as required. The framework will throw an exception if
 * a required field is not present in the data source.
 * 
 * Example:
 * {@code
 * @DataDriven
 * public class DatabaseConfig {
 *     @DataField
 *     @Required
 *     private String connectionString;
 *     
 *     @DataField
 *     private int timeout; // optional
 * }
 * }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Required {
    /**
     * Custom error message when the required field is missing.
     */
    String message() default "";
}
