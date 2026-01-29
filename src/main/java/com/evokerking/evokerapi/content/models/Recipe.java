package com.evokerking.evokerapi.content.models;

import com.evokerking.evokerapi.annotations.DataDriven;
import com.evokerking.evokerapi.annotations.DataField;
import com.evokerking.evokerapi.annotations.Required;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a crafting recipe with ingredients and results.
 * Use this for creating data-driven crafting systems.
 * 
 * Example JSON:
 * {@code
 * {
 *   "id": "iron_sword_recipe",
 *   "name": "Iron Sword Recipe",
 *   "type": "CRAFTING",
 *   "ingredients": [
 *     {"item": "iron_ingot", "amount": 2},
 *     {"item": "stick", "amount": 1}
 *   ],
 *   "result": {
 *     "item": "iron_sword",
 *     "amount": 1
 *   },
 *   "craftingTime": 5.0
 * }
 * }
 */
@DataDriven
public class Recipe {
    
    @DataField
    @Required
    private String id;
    
    @DataField
    @Required
    private String name;
    
    @DataField(defaultValue = "CRAFTING")
    private String type;
    
    @DataField
    private String description;
    
    @DataField
    @Required
    private List<Map<String, Object>> ingredients;
    
    @DataField
    @Required
    private Map<String, Object> result;
    
    @DataField(defaultValue = "0")
    private double craftingTime;
    
    @DataField
    private Map<String, Object> requirements;
    
    public Recipe() {
        this.ingredients = new ArrayList<>();
        this.result = new HashMap<>();
        this.requirements = new HashMap<>();
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public List<Map<String, Object>> getIngredients() { return ingredients; }
    public Map<String, Object> getResult() { return result; }
    public double getCraftingTime() { return craftingTime; }
    public Map<String, Object> getRequirements() { return requirements; }
    
    // Helper methods
    public String getResultItem() {
        return result != null ? (String) result.get("item") : null;
    }
    
    public int getResultAmount() {
        if (result == null || !result.containsKey("amount")) {
            return 1;
        }
        Object amount = result.get("amount");
        if (amount instanceof Number) {
            return ((Number) amount).intValue();
        }
        return 1;
    }
    
    public boolean hasIngredient(String itemId) {
        if (ingredients == null) {
            return false;
        }
        return ingredients.stream()
                .anyMatch(ing -> itemId.equals(ing.get("item")));
    }
    
    public int getIngredientAmount(String itemId) {
        if (ingredients == null) {
            return 0;
        }
        return ingredients.stream()
                .filter(ing -> itemId.equals(ing.get("item")))
                .mapToInt(ing -> {
                    Object amount = ing.get("amount");
                    return amount instanceof Number ? ((Number) amount).intValue() : 0;
                })
                .sum();
    }
    
    @Override
    public String toString() {
        return "Recipe{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", ingredients=" + ingredients.size() +
                ", result=" + getResultItem() + " x" + getResultAmount() +
                ", craftingTime=" + craftingTime +
                '}';
    }
}
