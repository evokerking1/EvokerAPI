package com.evokerking.evokerapi;

import com.evokerking.evokerapi.annotations.DataDriven;
import com.evokerking.evokerapi.annotations.DataField;
import com.evokerking.evokerapi.annotations.Required;
import com.evokerking.evokerapi.core.DataBindException;
import com.evokerking.evokerapi.core.DataLoadException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EvokerAPITest {
    
    @DataDriven
    public static class TestConfig {
        @DataField
        private String name;
        
        @DataField
        private int port;
        
        @DataField(defaultValue = "localhost")
        private String hostname;
        
        public TestConfig() {}
        
        public String getName() { return name; }
        public int getPort() { return port; }
        public String getHostname() { return hostname; }
    }
    
    @DataDriven
    public static class RequiredFieldConfig {
        @DataField
        @Required
        private String requiredField;
        
        @DataField
        private String optionalField;
        
        public RequiredFieldConfig() {}
        
        public String getRequiredField() { return requiredField; }
        public String getOptionalField() { return optionalField; }
    }
    
    @Test
    void testLoadFromMap() throws DataBindException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "test");
        data.put("port", 8080);
        
        TestConfig config = EvokerAPI.loadFromMap(TestConfig.class, data);
        
        assertNotNull(config);
        assertEquals("test", config.getName());
        assertEquals(8080, config.getPort());
        assertEquals("localhost", config.getHostname()); // default value
    }
    
    @Test
    void testBuilder() throws DataBindException, DataLoadException {
        TestConfig config = EvokerAPI.builder(TestConfig.class)
            .with("name", "builder-test")
            .with("port", 9090)
            .with("hostname", "example.com")
            .build();
        
        assertNotNull(config);
        assertEquals("builder-test", config.getName());
        assertEquals(9090, config.getPort());
        assertEquals("example.com", config.getHostname());
    }
    
    @Test
    void testBuilderWithDefaultValue() throws DataBindException, DataLoadException {
        TestConfig config = EvokerAPI.builder(TestConfig.class)
            .with("name", "default-test")
            .with("port", 7070)
            .build();
        
        assertNotNull(config);
        assertEquals("default-test", config.getName());
        assertEquals(7070, config.getPort());
        assertEquals("localhost", config.getHostname()); // should use default
    }
    
    @Test
    void testRequiredField() {
        Map<String, Object> data = new HashMap<>();
        data.put("optionalField", "optional");
        // requiredField is missing
        
        assertThrows(DataBindException.class, () -> {
            EvokerAPI.loadFromMap(RequiredFieldConfig.class, data);
        });
    }
    
    @Test
    void testRequiredFieldPresent() throws DataBindException {
        Map<String, Object> data = new HashMap<>();
        data.put("requiredField", "required-value");
        data.put("optionalField", "optional-value");
        
        RequiredFieldConfig config = EvokerAPI.loadFromMap(RequiredFieldConfig.class, data);
        
        assertNotNull(config);
        assertEquals("required-value", config.getRequiredField());
        assertEquals("optional-value", config.getOptionalField());
    }
    
    @Test
    void testTypeConversion() throws DataBindException {
        Map<String, Object> data = new HashMap<>();
        data.put("name", "convert-test");
        data.put("port", "8888"); // String that should be converted to int
        
        TestConfig config = EvokerAPI.loadFromMap(TestConfig.class, data);
        
        assertNotNull(config);
        assertEquals("convert-test", config.getName());
        assertEquals(8888, config.getPort());
    }
}
