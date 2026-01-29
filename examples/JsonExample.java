package com.evokerking.evokerapi.examples;

import com.evokerking.evokerapi.EvokerAPI;
import com.evokerking.evokerapi.annotations.DataDriven;
import com.evokerking.evokerapi.annotations.DataField;
import com.evokerking.evokerapi.annotations.Required;

/**
 * Example demonstrating JSON file loading using Gson.
 * This example shows how to load configuration from JSON files,
 * which are parsed using Gson library.
 */
public class JsonExample {
    
    @DataDriven
    public static class DatabaseConfig {
        @DataField
        @Required
        private String host;
        
        @DataField
        @Required
        private int port;
        
        @DataField
        @Required
        private String database;
        
        @DataField
        private String username;
        
        @DataField
        private String password;
        
        @DataField(defaultValue = "10")
        private int connectionPoolSize;
        
        @DataField(defaultValue = "30000")
        private int connectionTimeout;
        
        public DatabaseConfig() {}
        
        public String getHost() { return host; }
        public int getPort() { return port; }
        public String getDatabase() { return database; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public int getConnectionPoolSize() { return connectionPoolSize; }
        public int getConnectionTimeout() { return connectionTimeout; }
        
        @Override
        public String toString() {
            return "DatabaseConfig{" +
                    "host='" + host + '\'' +
                    ", port=" + port +
                    ", database='" + database + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + (password != null ? "***" : "null") + '\'' +
                    ", connectionPoolSize=" + connectionPoolSize +
                    ", connectionTimeout=" + connectionTimeout +
                    '}';
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== EvokerAPI JSON Example (Using Gson) ===\n");
        
        // Example: Load from JSON file
        String jsonFile = "examples/database-config.json";
        System.out.println("Loading configuration from: " + jsonFile);
        
        try {
            // This will use Gson to parse the JSON file
            DatabaseConfig config = EvokerAPI.loadFromFile(DatabaseConfig.class, jsonFile);
            System.out.println("Configuration loaded successfully:");
            System.out.println("   " + config);
            
            // Demonstrate accessing individual fields
            System.out.println("\nConnection Details:");
            System.out.println("   - Connecting to: " + config.getHost() + ":" + config.getPort());
            System.out.println("   - Database: " + config.getDatabase());
            System.out.println("   - Pool Size: " + config.getConnectionPoolSize());
            System.out.println("   - Timeout: " + config.getConnectionTimeout() + "ms");
            
        } catch (Exception e) {
            System.err.println("Error loading configuration: " + e.getMessage());
            e.printStackTrace();
            System.err.println("\nTo run this example, ensure 'examples/database-config.json' exists with:");
            System.err.println("""
                {
                  "host": "localhost",
                  "port": 5432,
                  "database": "myapp",
                  "username": "admin",
                  "password": "secret123",
                  "connectionPoolSize": 20,
                  "connectionTimeout": 60000
                }
                """);
        }
    }
}
