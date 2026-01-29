package com.evokerking.evokerapi.examples;

import com.evokerking.evokerapi.content.models.Item;
import com.evokerking.evokerapi.content.models.Recipe;
import com.evokerking.evokerapi.content.registry.ContentLoader;
import com.evokerking.evokerapi.content.registry.DataRegistry;

/**
 * Example demonstrating data-driven content system for items and recipes.
 * Shows how to load game content from JSON files and use registries.
 */
public class ContentSystemExample {
    
    public static void main(String[] args) {
        System.out.println("=== EvokerAPI Content System Example ===\n");
        
        // Create registries for items and recipes
        DataRegistry<Item> itemRegistry = new DataRegistry<>("Items");
        DataRegistry<Recipe> recipeRegistry = new DataRegistry<>("Recipes");
        
        // Load items from JSON file
        System.out.println("1. Loading Items from JSON...");
        try {
            int itemCount = ContentLoader.loadFromArrayFile(
                itemRegistry, 
                Item.class, 
                "examples/items.json"
            );
            System.out.println("   Loaded " + itemCount + " items successfully!");
            System.out.println("   Registry: " + itemRegistry);
        } catch (Exception e) {
            System.err.println("   Error loading items: " + e.getMessage());
        }
        
        // Load recipes from JSON file
        System.out.println("\n2. Loading Recipes from JSON...");
        try {
            int recipeCount = ContentLoader.loadFromArrayFile(
                recipeRegistry, 
                Recipe.class, 
                "examples/recipes.json"
            );
            System.out.println("   Loaded " + recipeCount + " recipes successfully!");
            System.out.println("   Registry: " + recipeRegistry);
        } catch (Exception e) {
            System.err.println("   Error loading recipes: " + e.getMessage());
        }
        
        // Display loaded items
        System.out.println("\n3. Displaying Loaded Items:");
        for (String itemId : itemRegistry.getIds()) {
            Item item = itemRegistry.get(itemId);
            System.out.println("   - " + item.getName() + " (" + item.getId() + ")");
            System.out.println("     Type: " + item.getType() + ", Rarity: " + item.getRarity());
            if (item.getProperties() != null && !item.getProperties().isEmpty()) {
                System.out.println("     Properties: " + item.getProperties());
            }
        }
        
        // Query items by type
        System.out.println("\n4. Querying Items by Type (WEAPON):");
        var weapons = itemRegistry.findAll(item -> "WEAPON".equals(item.getType()));
        for (Item weapon : weapons) {
            System.out.println("   - " + weapon.getName() + 
                " (Damage: " + weapon.getProperty("damage", Double.class, 0.0) + ")");
        }
        
        // Display loaded recipes
        System.out.println("\n5. Displaying Loaded Recipes:");
        for (String recipeId : recipeRegistry.getIds()) {
            Recipe recipe = recipeRegistry.get(recipeId);
            System.out.println("   - " + recipe.getName() + " (" + recipe.getType() + ")");
            System.out.println("     Ingredients: " + recipe.getIngredients().size());
            System.out.println("     Result: " + recipe.getResultItem() + " x" + recipe.getResultAmount());
            System.out.println("     Crafting Time: " + recipe.getCraftingTime() + "s");
        }
        
        // Find recipes by ingredient
        System.out.println("\n6. Finding Recipes Using 'iron_ingot':");
        var ironRecipes = recipeRegistry.findAll(recipe -> recipe.hasIngredient("iron_ingot"));
        for (Recipe recipe : ironRecipes) {
            int amount = recipe.getIngredientAmount("iron_ingot");
            System.out.println("   - " + recipe.getName() + " (requires " + amount + " iron ingots)");
        }
        
        // Look up specific items
        System.out.println("\n7. Looking Up Specific Items:");
        Item sword = itemRegistry.get("iron_sword");
        if (sword != null) {
            System.out.println("   Found: " + sword.getName());
            System.out.println("   " + sword.getDescription());
            System.out.println("   Stackable: " + sword.isStackable());
            System.out.println("   Max Stack: " + sword.getMaxStackSize());
        }
        
        System.out.println("\n=== Content System Demo Complete! ===");
        System.out.println("Total Items: " + itemRegistry.size());
        System.out.println("Total Recipes: " + recipeRegistry.size());
    }
}
