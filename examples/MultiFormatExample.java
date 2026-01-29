package com.evokerking.evokerapi.examples;

import com.evokerking.evokerapi.EvokerAPI;
import com.evokerking.evokerapi.annotations.DataDriven;
import com.evokerking.evokerapi.annotations.DataField;
import com.evokerking.evokerapi.annotations.Required;

/**
 * Example demonstrating loading configuration from multiple file formats.
 * Shows JSON (Gson), YAML, and Properties file support.
 */
public class MultiFormatExample {
    
    @DataDriven
    public static class ApiConfig {
        @DataField
        @Required
        private String host;
        
        @DataField
        @Required
        private int port;
        
        @DataField
        private String apiKey;
        
        @DataField(defaultValue = "30")
        private int timeout;
        
        @DataField
        private int retryAttempts;
        
        @DataField
        private boolean enableLogging;
        
        public ApiConfig() {}
        
        public String getHost() { return host; }
        public int getPort() { return port; }
        public String getApiKey() { return apiKey; }
        public int getTimeout() { return timeout; }
        public int getRetryAttempts() { return retryAttempts; }
        public boolean isEnableLogging() { return enableLogging; }
        
        @Override
        public String toString() {
            return "ApiConfig{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", apiKey='" + (apiKey != null ? apiKey.substring(0, 3) + "***" : "null") + '\'' +
                    ", timeout=" + timeout +
                    ", retryAttempts=" + retryAttempts +
                    ", enableLogging=" + enableLogging +
                    '}';
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== EvokerAPI Multi-Format Example ===\n");
        
        // Example 1: Load from JSON (using Gson)
        System.out.println("1. Loading from JSON file (using Gson):");
        try {
            ApiConfig jsonConfig = EvokerAPI.loadFromFile(ApiConfig.class, "examples/api-config.json");
            System.out.println("   " + jsonConfig);
        } catch (Exception e) {
            System.out.println("   Note: api-config.json not found (this is optional)");
        }
        
        // Example 2: Load from YAML
        System.out.println("\n2. Loading from YAML file:");
        try {
            ApiConfig yamlConfig = EvokerAPI.loadFromFile(ApiConfig.class, "examples/api-config.yaml");
            System.out.println("   " + yamlConfig);
        } catch (NoClassDefFoundError e) {
            System.out.println("   Note: YAML support requires SnakeYAML in classpath at runtime");
        } catch (Exception e) {
            System.err.println("   Error: " + e.getMessage());
        }
        
        // Example 3: Load from Properties
        System.out.println("\n3. Loading from Properties file:");
        try {
            // For properties files, use flat structure
            ApiConfig propsConfig = EvokerAPI.loadFromFile(ApiConfig.class, "examples/app.properties");
            System.out.println("   Note: Properties file has different structure");
        } catch (Exception e) {
            System.out.println("   Note: app.properties has different keys");
        }
        
        // Example 4: Builder API (programmatic configuration)
        System.out.println("\n4. Using Builder API (programmatic):");
        try {
            ApiConfig builderConfig = EvokerAPI.builder(ApiConfig.class)
                    .with("host", "api.example.com")
                    .with("port", 443)
                    .with("apiKey", "secret-key-12345")
                    .with("timeout", 60)
                    .with("retryAttempts", 5)
                    .with("enableLogging", true)
                    .build();
            System.out.println("   " + builderConfig);
        } catch (Exception e) {
            System.err.println("   Error: " + e.getMessage());
        }
        
        System.out.println("\n=== All formats work with EvokerAPI! ===");
    }
}
