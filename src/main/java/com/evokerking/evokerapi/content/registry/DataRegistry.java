package com.evokerking.evokerapi.content.registry;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Generic registry for managing collections of data-driven content.
 * Can store items, recipes, or any other game content by ID.
 * 
 * @param <T> The type of content to store
 */
public class DataRegistry<T> {
    
    private final Map<String, T> entries;
    private final String name;
    
    public DataRegistry(String name) {
        this.name = name;
        this.entries = new LinkedHashMap<>();
    }
    
    /**
     * Register a new entry with the given ID.
     * 
     * @param id The unique identifier
     * @param entry The entry to register
     * @throws IllegalArgumentException if ID already exists
     */
    public void register(String id, T entry) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (entries.containsKey(id)) {
            throw new IllegalArgumentException("Entry with ID '" + id + "' already exists in " + name);
        }
        entries.put(id, entry);
    }
    
    /**
     * Register or update an entry with the given ID.
     * 
     * @param id The unique identifier
     * @param entry The entry to register
     */
    public void registerOrReplace(String id, T entry) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        entries.put(id, entry);
    }
    
    /**
     * Get an entry by ID.
     * 
     * @param id The identifier
     * @return The entry, or null if not found
     */
    public T get(String id) {
        return entries.get(id);
    }
    
    /**
     * Get an entry by ID, or return a default value if not found.
     * 
     * @param id The identifier
     * @param defaultValue The default value
     * @return The entry or default value
     */
    public T getOrDefault(String id, T defaultValue) {
        return entries.getOrDefault(id, defaultValue);
    }
    
    /**
     * Check if an entry exists.
     * 
     * @param id The identifier
     * @return true if exists
     */
    public boolean contains(String id) {
        return entries.containsKey(id);
    }
    
    /**
     * Remove an entry.
     * 
     * @param id The identifier
     * @return The removed entry, or null if not found
     */
    public T remove(String id) {
        return entries.remove(id);
    }
    
    /**
     * Clear all entries.
     */
    public void clear() {
        entries.clear();
    }
    
    /**
     * Get all registered IDs.
     * 
     * @return Set of IDs
     */
    public Set<String> getIds() {
        return new LinkedHashSet<>(entries.keySet());
    }
    
    /**
     * Get all entries.
     * 
     * @return Collection of entries
     */
    public Collection<T> getAll() {
        return new ArrayList<>(entries.values());
    }
    
    /**
     * Get the number of registered entries.
     * 
     * @return Size of registry
     */
    public int size() {
        return entries.size();
    }
    
    /**
     * Check if registry is empty.
     * 
     * @return true if empty
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }
    
    /**
     * Find entries matching a predicate.
     * 
     * @param predicate The filter condition
     * @return List of matching entries
     */
    public List<T> findAll(Predicate<T> predicate) {
        return entries.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    /**
     * Find the first entry matching a predicate.
     * 
     * @param predicate The filter condition
     * @return Optional containing the first match
     */
    public Optional<T> findFirst(Predicate<T> predicate) {
        return entries.values().stream()
                .filter(predicate)
                .findFirst();
    }
    
    /**
     * Get the name of this registry.
     * 
     * @return Registry name
     */
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "DataRegistry{" +
                "name='" + name + '\'' +
                ", size=" + entries.size() +
                '}';
    }
}
