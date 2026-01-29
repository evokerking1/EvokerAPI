package com.evokerking.evokerapi.examples;

import com.evokerking.evokerapi.EvokerAPI;
import com.evokerking.evokerapi.annotations.DataDriven;
import com.evokerking.evokerapi.annotations.DataField;
import com.evokerking.evokerapi.annotations.Required;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic example demonstrating the core features of EvokerAPI.
 * This example shows how to create data-driven configuration classes
 * and populate them from various sources.
 */
public class BasicExample {
    
    @DataDriven
    public static class ServerConfig {
        @DataField
        @Required
        private String hostname;
        
        @DataField(defaultValue = "8080")
        private int port;
        
        @DataField
        private boolean enableSSL;
        
        @DataField(name = "max.connections")
        private int maxConnections;
        
        public ServerConfig() {}
        
        public String getHostname() { return hostname; }
        public int getPort() { return port; }
        public boolean isEnableSSL() { return enableSSL; }
        public int getMaxConnections() { return maxConnections; }
        
        @Override
        public String toString() {
            return "ServerConfig{" +
                    "hostname='" + hostname + '\'' +
                    ", port=" + port +
                    ", enableSSL=" + enableSSL +
                    ", maxConnections=" + maxConnections +
                    '}';
        }
    }
    
    public static void main(String[] args) {
        System.out.println("=== EvokerAPI Basic Example ===\n");
        
        // Example 1: Load from a Map
        System.out.println("1. Loading from Map:");
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("hostname", "localhost");
            data.put("port", 9090);
            data.put("enableSSL", true);
            data.put("max.connections", 100);
            
            ServerConfig config1 = EvokerAPI.loadFromMap(ServerConfig.class, data);
            System.out.println("   " + config1);
        } catch (Exception e) {
            System.err.println("   Error: " + e.getMessage());
        }
        
        // Example 2: Using Builder Pattern
        System.out.println("\n2. Using Builder Pattern:");
        try {
            ServerConfig config2 = EvokerAPI.builder(ServerConfig.class)
                    .with("hostname", "example.com")
                    .with("port", 8443)
                    .with("enableSSL", true)
                    .with("max.connections", 200)
                    .build();
            System.out.println("   " + config2);
        } catch (Exception e) {
            System.err.println("   Error: " + e.getMessage());
        }
        
        // Example 3: Using Default Values
        System.out.println("\n3. Using Default Values:");
        try {
            ServerConfig config3 = EvokerAPI.builder(ServerConfig.class)
                    .with("hostname", "api.example.com")
                    // port will use default value of 8080
                    .with("enableSSL", false)
                    .with("max.connections", 50)
                    .build();
            System.out.println("   " + config3);
        } catch (Exception e) {
            System.err.println("   Error: " + e.getMessage());
        }
        
        // Example 4: Required Field Validation
        System.out.println("\n4. Required Field Validation (should fail):");
        try {
            Map<String, Object> incompleteData = new HashMap<>();
            incompleteData.put("port", 8080);
            // Missing required 'hostname' field
            
            ServerConfig config4 = EvokerAPI.loadFromMap(ServerConfig.class, incompleteData);
            System.out.println("   " + config4);
        } catch (Exception e) {
            System.out.println("   Expected error: " + e.getMessage());
        }
    }
}
