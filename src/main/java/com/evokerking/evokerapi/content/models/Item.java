package com.evokerking.evokerapi.content.models;

import com.evokerking.evokerapi.annotations.DataDriven;
import com.evokerking.evokerapi.annotations.DataField;
import com.evokerking.evokerapi.annotations.Required;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a game item with properties that can be loaded from data files.
 * Use this for creating data-driven items in your game or application.
 * 
 * Example JSON:
 * {@code
 * {
 *   "id": "iron_sword",
 *   "name": "Iron Sword",
 *   "description": "A sturdy sword made of iron",
 *   "type": "WEAPON",
 *   "rarity": "COMMON",
 *   "stackable": false,
 *   "maxStackSize": 1,
 *   "properties": {
 *     "damage": 7,
 *     "durability": 250,
 *     "attackSpeed": 1.6
 *   }
 * }
 * }
 */
@DataDriven
public class Item {
    
    @DataField
    @Required
    private String id;
    
    @DataField
    @Required
    private String name;
    
    @DataField
    private String description;
    
    @DataField
    @Required
    private String type;
    
    @DataField(defaultValue = "COMMON")
    private String rarity;
    
    @DataField(defaultValue = "true")
    private boolean stackable;
    
    @DataField(defaultValue = "64")
    private int maxStackSize;
    
    @DataField
    private Map<String, Object> properties;
    
    public Item() {
        this.properties = new HashMap<>();
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getType() { return type; }
    public String getRarity() { return rarity; }
    public boolean isStackable() { return stackable; }
    public int getMaxStackSize() { return maxStackSize; }
    public Map<String, Object> getProperties() { return properties; }
    
    // Property helpers
    public Object getProperty(String key) {
        return properties != null ? properties.get(key) : null;
    }
    
    public <T> T getProperty(String key, Class<T> type, T defaultValue) {
        if (properties == null || !properties.containsKey(key)) {
            return defaultValue;
        }
        Object value = properties.get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return defaultValue;
    }
    
    public void setProperty(String key, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }
    
    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", rarity='" + rarity + '\'' +
                ", stackable=" + stackable +
                ", maxStackSize=" + maxStackSize +
                ", properties=" + properties +
                '}';
    }
}
