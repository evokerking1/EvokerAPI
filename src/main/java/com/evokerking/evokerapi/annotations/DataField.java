package com.evokerking.evokerapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field as a data field that should be populated from data sources.
 * Fields annotated with @DataField will be automatically populated when
 * loading data into a @DataDriven class.
 * 
 * Example:
 * {@code
 * @DataDriven
 * public class Config {
 *     @DataField(name = "server.port")
 *     private int port;
 *     
 *     @DataField
 *     private String hostname;
 * }
 * }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataField {
    /**
     * The name of the field in the data source.
     * If not specified, uses the field name.
     */
    String name() default "";
    
    /**
     * Default value if the field is not present in the data source.
     */
    String defaultValue() default "";
}
