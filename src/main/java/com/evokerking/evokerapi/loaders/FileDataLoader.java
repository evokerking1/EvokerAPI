package com.evokerking.evokerapi.loaders;

import com.evokerking.evokerapi.core.DataLoadException;
import com.evokerking.evokerapi.core.DataLoader;
import com.evokerking.evokerapi.parsers.JsonParser;
import com.evokerking.evokerapi.parsers.PropertiesParser;
import com.evokerking.evokerapi.parsers.YamlParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Loader for file-based data sources.
 * Supports JSON, YAML, and properties files.
 */
public class FileDataLoader implements DataLoader {
    
    @Override
    public Map<String, Object> load() throws DataLoadException {
        throw new DataLoadException("File path must be specified");
    }
    
    @Override
    public Map<String, Object> load(String filePath) throws DataLoadException {
        try {
            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                throw new DataLoadException("File not found: " + filePath);
            }
            
            String content = Files.readString(path);
            String fileName = path.getFileName().toString().toLowerCase();
            
            if (fileName.endsWith(".json")) {
                JsonParser jsonParser = new JsonParser();
                return jsonParser.parse(content);
            } else if (fileName.endsWith(".yaml") || fileName.endsWith(".yml")) {
                YamlParser yamlParser = new YamlParser();
                return yamlParser.parse(content);
            } else if (fileName.endsWith(".properties")) {
                PropertiesParser propertiesParser = new PropertiesParser();
                return propertiesParser.parse(content);
            } else {
                throw new DataLoadException("Unsupported file format: " + fileName);
            }
        } catch (IOException e) {
            throw new DataLoadException("Failed to load file: " + filePath, e);
        }
    }
}
