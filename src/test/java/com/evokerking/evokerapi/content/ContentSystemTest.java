package com.evokerking.evokerapi.content;

import com.evokerking.evokerapi.EvokerAPI;
import com.evokerking.evokerapi.content.models.Item;
import com.evokerking.evokerapi.content.models.Recipe;
import com.evokerking.evokerapi.content.registry.ContentLoader;
import com.evokerking.evokerapi.content.registry.DataRegistry;
import com.evokerking.evokerapi.core.DataBindException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ContentSystemTest {
    
    @Test
    void testItemCreation() throws DataBindException {
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("id", "test_sword");
        itemData.put("name", "Test Sword");
        itemData.put("type", "WEAPON");
        itemData.put("rarity", "COMMON");
        itemData.put("stackable", false);
        itemData.put("maxStackSize", 1);
        
        Item item = EvokerAPI.loadFromMap(Item.class, itemData);
        
        assertNotNull(item);
        assertEquals("test_sword", item.getId());
        assertEquals("Test Sword", item.getName());
        assertEquals("WEAPON", item.getType());
        assertEquals("COMMON", item.getRarity());
        assertFalse(item.isStackable());
        assertEquals(1, item.getMaxStackSize());
    }
    
    @Test
    void testItemWithProperties() throws DataBindException {
        Map<String, Object> props = new HashMap<>();
        props.put("damage", 10);
        props.put("durability", 100);
        
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("id", "test_item");
        itemData.put("name", "Test Item");
        itemData.put("type", "WEAPON");
        itemData.put("properties", props);
        
        Item item = EvokerAPI.loadFromMap(Item.class, itemData);
        
        assertNotNull(item);
        assertNotNull(item.getProperties());
        // Properties stored as-is from map
        assertEquals(10, item.getProperty("damage"));
        assertEquals(100, item.getProperty("durability"));
    }
    
    @Test
    void testRecipeCreation() throws DataBindException {
        Map<String, Object> ingredient1 = new HashMap<>();
        ingredient1.put("item", "iron_ingot");
        ingredient1.put("amount", 2);
        
        Map<String, Object> result = new HashMap<>();
        result.put("item", "iron_sword");
        result.put("amount", 1);
        
        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("id", "test_recipe");
        recipeData.put("name", "Test Recipe");
        recipeData.put("type", "CRAFTING");
        recipeData.put("ingredients", java.util.List.of(ingredient1));
        recipeData.put("result", result);
        recipeData.put("craftingTime", 5.0);
        
        Recipe recipe = EvokerAPI.loadFromMap(Recipe.class, recipeData);
        
        assertNotNull(recipe);
        assertEquals("test_recipe", recipe.getId());
        assertEquals("Test Recipe", recipe.getName());
        assertEquals("CRAFTING", recipe.getType());
        assertEquals(1, recipe.getIngredients().size());
        assertEquals("iron_sword", recipe.getResultItem());
        assertEquals(1, recipe.getResultAmount());
        assertEquals(5.0, recipe.getCraftingTime());
    }
    
    @Test
    void testRecipeIngredientQueries() throws DataBindException {
        Map<String, Object> ingredient1 = new HashMap<>();
        ingredient1.put("item", "iron_ingot");
        ingredient1.put("amount", 2);
        
        Map<String, Object> ingredient2 = new HashMap<>();
        ingredient2.put("item", "stick");
        ingredient2.put("amount", 1);
        
        Map<String, Object> result = new HashMap<>();
        result.put("item", "iron_sword");
        result.put("amount", 1);
        
        Map<String, Object> recipeData = new HashMap<>();
        recipeData.put("id", "test_recipe");
        recipeData.put("name", "Test Recipe");
        recipeData.put("type", "CRAFTING");
        recipeData.put("ingredients", java.util.List.of(ingredient1, ingredient2));
        recipeData.put("result", result);
        
        Recipe recipe = EvokerAPI.loadFromMap(Recipe.class, recipeData);
        
        assertTrue(recipe.hasIngredient("iron_ingot"));
        assertTrue(recipe.hasIngredient("stick"));
        assertFalse(recipe.hasIngredient("diamond"));
        
        assertEquals(2, recipe.getIngredientAmount("iron_ingot"));
        assertEquals(1, recipe.getIngredientAmount("stick"));
        assertEquals(0, recipe.getIngredientAmount("diamond"));
    }
    
    @Test
    void testDataRegistry() {
        DataRegistry<Item> registry = new DataRegistry<>("TestItems");
        
        assertEquals("TestItems", registry.getName());
        assertTrue(registry.isEmpty());
        assertEquals(0, registry.size());
        
        Item item1 = new Item();
        registry.register("item1", item1);
        
        assertEquals(1, registry.size());
        assertFalse(registry.isEmpty());
        assertTrue(registry.contains("item1"));
        assertSame(item1, registry.get("item1"));
        
        Item item2 = new Item();
        registry.register("item2", item2);
        
        assertEquals(2, registry.size());
        assertEquals(2, registry.getIds().size());
        assertEquals(2, registry.getAll().size());
    }
    
    @Test
    void testDataRegistryDuplicateId() {
        DataRegistry<Item> registry = new DataRegistry<>("TestItems");
        
        Item item1 = new Item();
        registry.register("item1", item1);
        
        Item item2 = new Item();
        assertThrows(IllegalArgumentException.class, () -> {
            registry.register("item1", item2);
        });
    }
    
    @Test
    void testDataRegistryRegisterOrReplace() {
        DataRegistry<Item> registry = new DataRegistry<>("TestItems");
        
        Item item1 = new Item();
        registry.register("item1", item1);
        assertSame(item1, registry.get("item1"));
        
        Item item2 = new Item();
        registry.registerOrReplace("item1", item2);
        assertSame(item2, registry.get("item1"));
        assertEquals(1, registry.size());
    }
    
    @Test
    void testDataRegistryRemove() {
        DataRegistry<Item> registry = new DataRegistry<>("TestItems");
        
        Item item1 = new Item();
        registry.register("item1", item1);
        assertTrue(registry.contains("item1"));
        
        Item removed = registry.remove("item1");
        assertSame(item1, removed);
        assertFalse(registry.contains("item1"));
        assertEquals(0, registry.size());
    }
    
    @Test
    void testDataRegistryClear() {
        DataRegistry<Item> registry = new DataRegistry<>("TestItems");
        
        registry.register("item1", new Item());
        registry.register("item2", new Item());
        registry.register("item3", new Item());
        
        assertEquals(3, registry.size());
        
        registry.clear();
        assertEquals(0, registry.size());
        assertTrue(registry.isEmpty());
    }
}
