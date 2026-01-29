package com.evokerking.evokerapi.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as data-driven, enabling automatic data binding and instantiation.
 * Classes annotated with @DataDriven can be automatically populated from various
 * data sources like JSON, YAML, or properties files.
 * 
 * Example:
 * {@code
 * @DataDriven
 * public class UserConfig {
 *     @DataField
 *     private String username;
 *     
 *     @DataField
 *     private int age;
 * }
 * }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DataDriven {
    /**
     * Optional prefix for data fields when loading from external sources.
     * For example, with prefix = "app", a field "name" would be loaded from "app.name"
     */
    String prefix() default "";
}
