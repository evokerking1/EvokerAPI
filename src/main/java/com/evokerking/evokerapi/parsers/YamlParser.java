package com.evokerking.evokerapi.parsers;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * Parser for YAML data using SnakeYAML.
 */
public class YamlParser {
    private final Yaml yaml;
    
    public YamlParser() {
        this.yaml = new Yaml();
    }
    
    /**
     * Parse YAML string to a map.
     * 
     * @param yamlContent The YAML string
     * @return Map containing the parsed data
     */
    public Map<String, Object> parse(String yamlContent) {
        return yaml.load(yamlContent);
    }
    
    /**
     * Convert object to YAML string.
     * 
     * @param obj The object to convert
     * @return YAML string representation
     */
    public String toYaml(Object obj) {
        return yaml.dump(obj);
    }
}
