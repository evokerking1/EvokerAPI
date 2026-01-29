# EvokerAPI

A powerful Java framework for building data-driven applications with Gradle. EvokerAPI makes it easy to create configuration classes and populate them from various data sources including JSON (using **Gson**), YAML, and properties files. **Now with built-in support for game content like items, recipes, and more!**

## Features

- 🚀 **Simple annotations** for marking data-driven classes
- 📝 **Multiple data formats** - JSON (via Gson), YAML, and Properties
- 🔧 **Type conversion** - Automatic conversion between common types
- ✅ **Validation** - Required field validation
- 🏗️ **Builder pattern** - Fluent API for programmatic configuration
- 🎯 **Default values** - Specify defaults for optional fields
- 🔌 **Extensible** - Custom data loaders and parsers
- 📦 **Gradle-based** - Easy integration into Gradle projects
- 🎮 **Game Content Models** - Built-in Item and Recipe models
- 🗂️ **Registry System** - Manage collections of data-driven content
- 📋 **Batch Loading** - Load multiple items/recipes from single files

## Installation

### Gradle

Add the following to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.evokerking:EvokerAPI:1.0.0'
}
```

### Maven

```xml
<dependency>
    <groupId>com.evokerking</groupId>
    <artifactId>EvokerAPI</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Quick Start

### 1. Define a Data-Driven Class

```java
import com.evokerking.evokerapi.annotations.*;

@DataDriven
public class ServerConfig {
    @DataField
    @Required
    private String hostname;
    
    @DataField(defaultValue = "8080")
    private int port;
    
    @DataField
    private boolean enableSSL;
    
    // Constructors, getters, etc.
}
```

### 2. Load from JSON (Using Gson)

Create a JSON file `config.json`:
```json
{
  "hostname": "localhost",
  "port": 9090,
  "enableSSL": true
}
```

Load it with EvokerAPI:
```java
ServerConfig config = EvokerAPI.loadFromFile(ServerConfig.class, "config.json");
System.out.println(config.getHostname()); // "localhost"
System.out.println(config.getPort());      // 9090
```

### 3. Load from Map

```java
Map<String, Object> data = Map.of(
    "hostname", "example.com",
    "port", 8443,
    "enableSSL", true
);

ServerConfig config = EvokerAPI.loadFromMap(ServerConfig.class, data);
```

### 4. Use Builder Pattern

```java
ServerConfig config = EvokerAPI.builder(ServerConfig.class)
    .with("hostname", "api.example.com")
    .with("port", 8443)
    .with("enableSSL", true)
    .build();
```

## Core Concepts

### Annotations

#### `@DataDriven`
Marks a class as data-driven, enabling automatic data binding.

```java
@DataDriven
public class Config {
    // fields
}
```

**Optional Parameters:**
- `prefix` - Prefix for all field names when loading from data sources

```java
@DataDriven(prefix = "app")
public class AppConfig {
    @DataField  // Will look for "app.name" in data
    private String name;
}
```

#### `@DataField`
Marks a field for automatic population from data sources.

```java
@DataField
private String name;

@DataField(name = "server.port")  // Use custom name
private int port;

@DataField(defaultValue = "localhost")  // Provide default
private String hostname;
```

#### `@Required`
Marks a field as required. Framework throws exception if missing.

```java
@DataField
@Required
private String apiKey;

@DataField
@Required(message = "Database connection string is required")
private String connectionString;
```

## Supported Data Formats

### JSON (Gson)
EvokerAPI uses **Gson** for JSON parsing, providing robust and efficient JSON handling.

```json
{
  "host": "localhost",
  "port": 5432,
  "database": "myapp"
}
```

```java
Config config = EvokerAPI.loadFromFile(Config.class, "config.json");
```

### YAML
Uses SnakeYAML for YAML parsing.

```yaml
host: localhost
port: 5432
database: myapp
```

```java
Config config = EvokerAPI.loadFromFile(Config.class, "config.yaml");
```

### Properties

```properties
host=localhost
port=5432
database=myapp
```

```java
Config config = EvokerAPI.loadFromFile(Config.class, "config.properties");
```

## Advanced Features

### Nested Field Names

Use dot notation for nested structures:

```java
@DataField(name = "server.host")
private String serverHost;

@DataField(name = "server.port")
private int serverPort;
```

Works with JSON:
```json
{
  "server": {
    "host": "localhost",
    "port": 8080
  }
}
```

### Type Conversion

Automatic conversion between types:
- String to primitives (int, long, double, float, boolean)
- String to wrapper classes
- Number types to other number types
- String to Enum values

```java
@DataField
private int port;  // Accepts "8080" string or 8080 number
```

### Custom Data Loaders

Create custom loaders by implementing `DataLoader`:

```java
public class DatabaseLoader implements DataLoader {
    @Override
    public Map<String, Object> load(String source) throws DataLoadException {
        // Load from database
        return data;
    }
}

// Use it
DataFactory factory = EvokerAPI.getFactory();
Config config = factory.create(Config.class, new DatabaseLoader(), "config_table");
```

## Data-Driven Content System

EvokerAPI includes a powerful content system for creating game items, recipes, and other data-driven content.

### Built-in Content Models

#### Items

Create game items with properties, rarity, and type information:

```java
import com.evokerking.evokerapi.content.models.Item;

// Load from JSON
Item sword = EvokerAPI.loadFromFile(Item.class, "iron_sword.json");

System.out.println(sword.getName());     // "Iron Sword"
System.out.println(sword.getRarity());   // "COMMON"
System.out.println(sword.isStackable()); // false

// Access custom properties
double damage = sword.getProperty("damage", Double.class, 0.0);
```

Example `iron_sword.json`:
```json
{
  "id": "iron_sword",
  "name": "Iron Sword",
  "description": "A sturdy sword made of iron",
  "type": "WEAPON",
  "rarity": "COMMON",
  "stackable": false,
  "maxStackSize": 1,
  "properties": {
    "damage": 7,
    "durability": 250,
    "attackSpeed": 1.6
  }
}
```

#### Recipes

Create crafting recipes with ingredients and requirements:

```java
import com.evokerking.evokerapi.content.models.Recipe;

Recipe recipe = EvokerAPI.loadFromFile(Recipe.class, "iron_sword_recipe.json");

System.out.println(recipe.getName());           // "Iron Sword Recipe"
System.out.println(recipe.getResultItem());     // "iron_sword"
System.out.println(recipe.hasIngredient("iron_ingot")); // true
```

Example `iron_sword_recipe.json`:
```json
{
  "id": "iron_sword_recipe",
  "name": "Iron Sword Recipe",
  "type": "CRAFTING",
  "description": "Craft an iron sword",
  "ingredients": [
    {"item": "iron_ingot", "amount": 2},
    {"item": "wooden_stick", "amount": 1}
  ],
  "result": {
    "item": "iron_sword",
    "amount": 1
  },
  "craftingTime": 5.0,
  "requirements": {
    "craftingLevel": 10,
    "station": "anvil"
  }
}
```

### Registry System

Manage collections of content with the `DataRegistry`:

```java
import com.evokerking.evokerapi.content.registry.DataRegistry;

// Create registries
DataRegistry<Item> itemRegistry = new DataRegistry<>("Items");
DataRegistry<Recipe> recipeRegistry = new DataRegistry<>("Recipes");

// Register items
itemRegistry.register("iron_sword", ironSword);
itemRegistry.register("health_potion", healthPotion);

// Look up items
Item sword = itemRegistry.get("iron_sword");

// Query items
List<Item> weapons = itemRegistry.findAll(item -> "WEAPON".equals(item.getType()));
```

### Batch Loading

Load multiple items or recipes from a single file:

```java
import com.evokerking.evokerapi.content.registry.ContentLoader;

DataRegistry<Item> itemRegistry = new DataRegistry<>("Items");

// Load all items from a JSON file
int count = ContentLoader.loadFromArrayFile(
    itemRegistry,
    Item.class,
    "items.json"
);

System.out.println("Loaded " + count + " items");
```

Example `items.json`:
```json
{
  "items": [
    {
      "id": "iron_sword",
      "name": "Iron Sword",
      "type": "WEAPON",
      ...
    },
    {
      "id": "health_potion",
      "name": "Health Potion",
      "type": "CONSUMABLE",
      ...
    }
  ]
}
```

### Creating Custom Content

Extend the base models or create your own:

```java
@DataDriven
public class Monster {
    @DataField @Required
    private String id;
    
    @DataField @Required
    private String name;
    
    @DataField
    private int health;
    
    @DataField
    private Map<String, Object> abilities;
    
    // Getters, setters, etc.
}

// Use the same loading and registry system
DataRegistry<Monster> monsters = new DataRegistry<>("Monsters");
ContentLoader.loadFromArrayFile(monsters, Monster.class, "monsters.json");
```

## Examples

The framework includes several example programs in the `examples/` directory:

### Basic Example
Demonstrates core features including Map loading and Builder pattern.

```bash
cd examples
javac -cp "../build/libs/*" BasicExample.java
java -cp ".:../build/libs/*" com.evokerking.evokerapi.examples.BasicExample
```

### JSON Example (Gson)
Shows JSON file loading using Gson.

```bash
cd examples
javac -cp "../build/libs/*" JsonExample.java
java -cp ".:../build/libs/*" com.evokerking.evokerapi.examples.JsonExample
```

### Content System Example
Demonstrates data-driven items, recipes, and registry system.

```bash
cd examples
javac -cp "../build/libs/*" ContentSystemExample.java
java -cp ".:../build/libs/*" com.evokerking.evokerapi.examples.ContentSystemExample
```

## Building from Source

```bash
# Clone the repository
git clone https://github.com/evokerking1/EvokerAPI.git
cd EvokerAPI

# Build with Gradle
./gradlew build

# Run tests
./gradlew test

# Generate documentation
./gradlew javadoc
```

## Dependencies

- **Gson 2.10.1** - JSON parsing
- **SnakeYAML 2.2** - YAML parsing
- **JUnit Jupiter 5.10.1** - Testing

## Use Cases

- **Configuration Management** - Load application settings from files
- **API Clients** - Parse API responses into Java objects
- **Test Data** - Load test fixtures from data files
- **Plugin Systems** - Configure plugins from external data
- **Data-Driven Testing** - Parameterize tests with external data
- **Microservices** - Externalized configuration for cloud-native apps
- **Game Development** - Create items, recipes, monsters, and other game content from JSON/YAML files
- **Content Management** - Manage large collections of data-driven content with registries
- **Modding Systems** - Allow users to create custom content via data files

## Best Practices

1. **Use `@Required` for critical fields** - Fail fast on missing data
2. **Provide defaults** - Use `defaultValue` for optional configuration
3. **Keep classes simple** - One config class per concern
4. **Validate after loading** - Add custom validation if needed
5. **Use builders for testing** - Easy to create test configurations

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## Support

For issues and questions, please open an issue on GitHub:
https://github.com/evokerking1/EvokerAPI/issues
