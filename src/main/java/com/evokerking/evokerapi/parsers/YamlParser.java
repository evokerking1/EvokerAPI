package com.evokerking.evokerapi.parsers;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import java.util.Map;

/**
 * Parser for YAML data using SnakeYAML with safe parsing.
 * Uses SafeConstructor to prevent arbitrary code execution vulnerabilities.
 */
public class YamlParser {
    private final Yaml yaml;
    
    public YamlParser() {
        // Use SafeConstructor to prevent arbitrary code execution (CVE-2022-1471)
        this.yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
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
